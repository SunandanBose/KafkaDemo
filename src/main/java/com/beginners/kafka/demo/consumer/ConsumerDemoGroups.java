package com.beginners.kafka.demo.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemoGroups {

    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ConsumerDemoGroups.class.getName());

        Properties properties = new Properties();

        final String bootstrapServer = "127.0.0.1:9092";
        final String groupName = "my-first-application";
        final String resetConfig = "earliest";
        final String topic = "first_topic";

        //create consumer config
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, groupName);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, resetConfig);

        // create consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        //subscribe consumer for topic
        //singleton means we are subscribing to only one topic;
        consumer.subscribe(Collections.singleton(topic));
        //subscribe to more than one topic
        //consumer.subscribe(Arrays.asList(topic1, topic2));

        //poll for new data
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                logger.info("key : " + record.key() + "\n"
                        + "value : " + record.value() + "\n"
                        + "partition : " + record.partition() + "\n"
                        + "offset : " + record.offset());
            }
        }


    }
}
