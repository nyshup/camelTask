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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class EndToEndTestIT {

    public static final String TEST_BODY = "TEST BODY askldfjaslkf~!@#$%^&*(asjfhdlaksdhaksjdfhalks";
    @Value("${output.file.dir}")
    private String testFileDir;


    @Autowired
    JmsTemplate jmsTemplate;

    @Test(timeout = 10000)
    public void testMessagePost() throws Exception {
        deleteOutputDirectory();
        Destination destination = ActiveMQDestination.createDestination("serviceQueue", ActiveMQDestination.QUEUE_TYPE);
        jmsTemplate.send(destination, session -> {
            TextMessage message = session.createTextMessage();
            message.setText(TEST_BODY);
            message.setStringProperty("MyAction", "TEST Action");
            return message;
        });
        Thread.sleep(2000);
        Path path = Paths.get(testFileDir);
        assertTrue("Directory should be created", Files.exists(path));
        List<Path> files = Files.list(path).collect(Collectors.toList());
        assertEquals("One file should be created", 1, files.size());
        assertEquals(TEST_BODY, fileToString(files.get(0)));
    }

    private void deleteOutputDirectory() throws IOException {
        File directory = new File(testFileDir);
        FileUtils.deleteDirectory(directory);
    }

    private String fileToString(Path file) throws IOException {
        return new String(Files.readAllBytes(file));
    }

}
