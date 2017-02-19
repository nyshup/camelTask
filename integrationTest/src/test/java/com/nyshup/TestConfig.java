package com.nyshup;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.core.JmsTemplate;

@Configuration
@ComponentScan(basePackages = { "com.nyshup.*" })
@PropertySource(value = "classpath:application.properties")
public class TestConfig {

    @Value("${jms.broker.url}")
    String jmsBrokerUrl;

    @Bean
    JmsTemplate template(){
        return new JmsTemplate(new ActiveMQConnectionFactory(jmsBrokerUrl));
    }
}
