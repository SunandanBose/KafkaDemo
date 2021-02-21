package com.beginners.kafka.demo.producer;

import org.apache.kafka.clients.producer.*;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerDemoKeys {
    public static void main(String[] args) {
        Logger logger = LoggerFactory.getLogger(ProducerDemoKeys.class);
        final String bootstrapServer = "127.0.0.1:9092";
        // 1. Create producer property
        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        // we set serializers to make sure what kind of data we want to send
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // 2. create producer
        // Key and Value both are String
        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(properties);


        for (int i = 0; i < 10; i++) {
            // 3. Create Producer Record
            String topic = "first_topic";
            String value = "hello world" + String.valueOf(i);
            String key = "id_" + String.valueOf(i);

            logger.info("key : " + key);

            //same key should go to the same partition
            ProducerRecord<String, String> record =
                    new ProducerRecord<>(topic, key, value);

            // 4. send data
            producer.send(record, new Callback() {
                @Override
                public void onCompletion(RecordMetadata recordMetadata, Exception exception) {
                    //execute every time record is successfully sent or exeption
                    if (exception == null) {
                        //record was sent successfully
                        logger.info("Received Metadata \nTopic : " + recordMetadata.topic() + "\n"
                                + "Partition : " + recordMetadata.partition() + "\n"
                                + "Offset : " + recordMetadata.offset() + "\n"
                                + "TimeStamp : " + recordMetadata.timestamp());

                    } else {
                        logger.error("Error while producing", exception);
                    }
                }
            });
        }

        producer.flush();
        producer.close();
    }
}
