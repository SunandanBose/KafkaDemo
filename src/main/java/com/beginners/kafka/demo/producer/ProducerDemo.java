package com.beginners.kafka.demo.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.protocol.types.Field;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {
    public static void main(String[] args) {
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

        // 3. Create Producer Record
        ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "hello world");

        // 4. send data
        producer.send(record);

        producer.flush();
        producer.close();
    }
}
