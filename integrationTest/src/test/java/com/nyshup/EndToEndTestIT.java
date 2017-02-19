package com.nyshup;

import org.apache.activemq.command.ActiveMQDestination;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.jms.Destination;
import javax.jms.TextMessage;
import java.io.File;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EndToEndTestIT {

    @Value("${file.dir}")
    private String testFileDir;


    @Autowired
    JmsTemplate jmsTemplate;

    @Test(timeout = 1000000)
    public void testMessagePost() throws Exception {
        File directory = new File(testFileDir);
        FileUtils.deleteDirectory(directory);
        Destination destination = ActiveMQDestination.createDestination("serviceQueue", ActiveMQDestination.QUEUE_TYPE);
        jmsTemplate.send(destination, session -> {
            TextMessage message = session.createTextMessage();
            message.setText("TEST BODY");
            message.setStringProperty("MyAction", "TEST Action");
            return message;
        });
        Thread.sleep(2000);
        File[] listOfFiles = directory.listFiles();
    }

}
