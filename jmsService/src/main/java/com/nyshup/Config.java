package com.nyshup;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.activemq.pool.PooledConnectionFactory;
import org.apache.camel.component.jms.JmsConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

import javax.jms.ConnectionFactory;

@Configuration
public class Config {

    @Value("${jms.broker.url}")
    private String jmsBrokerUrl;

    @Bean
    ActiveMQComponent activemq() {
        ActiveMQComponent activeMQComponent = ActiveMQComponent.activeMQComponent();
        activeMQComponent.setConfiguration(jmsConfig());
        activeMQComponent.setTransacted(true);
        activeMQComponent.setTransactionManager(transactionManager());
        return activeMQComponent;
    }

    @Bean(initMethod = "start", destroyMethod = "stop")
    ConnectionFactory pooledConnectionFactory() {
        PooledConnectionFactory pooledConnectionFactory =
                new PooledConnectionFactory(new ActiveMQConnectionFactory(jmsBrokerUrl));
        pooledConnectionFactory.setMaxConnections(8);
        return pooledConnectionFactory;
    }

    @Bean
    JmsConfiguration jmsConfig() {
        JmsConfiguration config = new JmsConfiguration(pooledConnectionFactory());
        config.setConcurrentConsumers(10);
        return config;
    }

    @Bean
    JmsTransactionManager transactionManager() {
        return new JmsTransactionManager(pooledConnectionFactory());
    }
}
