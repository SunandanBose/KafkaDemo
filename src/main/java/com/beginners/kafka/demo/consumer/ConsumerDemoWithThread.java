package com.beginners.kafka.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CountDownLatch;

public class ConsumerDemoWithThread {

    public static void main(String[] args) {

        new ConsumerDemoWithThread().run();
    }

    private ConsumerDemoWithThread() {
    }

    private void run() {
        Logger logger = LoggerFactory.getLogger(ConsumerDemoWithThread.class.getName());
        final String bootstrapServer = "127.0.0.1:9092";
        final String groupName = "my-first-application";
        final String resetConfig = "earliest";
        final String topic = "first_topic";

        CountDownLatch latch = new CountDownLatch(1);
        logger.info("Creating Consumer Thread");
        Runnable myConsumerRunnable = new ConsumerRunnable(
                latch,
                bootstrapServer,
                groupName,
                resetConfig,
                topic
        );

        Thread myThread = new Thread(myConsumerRunnable);
        myThread.start();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            logger.info("Caught Shutdown Hook");
            ((ConsumerRunnable) myConsumerRunnable).shutdown();
        }));
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
            logger.error("Application got interrupted");
        } finally {
            logger.info("Application is closing");
        }
    }

    public class ConsumerRunnable implements Runnable {

        private CountDownLatch countDownLatch;
        private KafkaConsumer<String, String> consumer;
        Logger logger = LoggerFactory.getLogger(ConsumerRunnable.class.getName());

        public ConsumerRunnable(CountDownLatch countDownLatch,
                                String bootstrapServer,
                                String groupName,
                                String resetConfig,
                                String topic) {
            this.countDownLatch = countDownLatch;

            //create consumer config
            Properties properties = new Properties();
            properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
            properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
            properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
            properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, resetConfig);

            // create consumer
            this.consumer = new KafkaConsumer<String, String>(properties);
            consumer.subscribe(Collections.singleton(topic));
        }

        @Override
        public void run() {
            try {
                while (true) {
                    ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
                    for (ConsumerRecord<String, String> record : records) {
                        logger.info("key : " + record.key() + "\n"
                                + "value : " + record.value() + "\n"
                                + "partition : " + record.partition() + "\n"
                                + "offset : " + record.offset());
                    }
                }
            } catch (WakeupException e) {
                logger.info("Received shutdown signal !");
            } finally {
                consumer.close();
                //we are done with the consumer
                countDownLatch.countDown();
            }
        }

        public void shutdown() {
            consumer.wakeup();
        }
    }
}
