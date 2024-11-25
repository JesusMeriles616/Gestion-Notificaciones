package com.gestion.microservice_notification_management.event;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.*;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;


import java.util.*;

@Configuration
@EnableKafka
public class KafkaConsumerConfig {
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Bean
    public ConsumerFactory<String, FlightStatusUpdateEvent> consumerFactory() {
        JsonDeserializer<FlightStatusUpdateEvent> valueDeserializer = new JsonDeserializer<>(FlightStatusUpdateEvent.class);
        valueDeserializer.addTrustedPackages("*");  // Trust all packages if you're using multiple packages for your events

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class.getName());
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, valueDeserializer.getClass().getName());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-kafka-group");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new ErrorHandlingDeserializer<>(valueDeserializer));
    }


    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, FlightStatusUpdateEvent> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, FlightStatusUpdateEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        // Configure the error handler with a backoff
        CommonErrorHandler errorHandler = new DefaultErrorHandler(new FixedBackOff(1000L, 2L));
        factory.setCommonErrorHandler(errorHandler);

        return factory;
    }

}
