package com.nyshup.routes;


import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class JmsRoute extends RouteBuilder {

    private static final String MY_ACTION = "MyAction";
    private static final String MY_BODY = "MyBody";

    @Override
    public void configure() throws Exception {
        from("activemq:queue:serviceQueue").routeId("jmsRoute")
                .transacted()
                .setHeader(MY_ACTION, header("MyAction"))
                .setHeader(MY_BODY, body())
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .to("http4:{{http.server.host}}:{{http.server.port}}/service")
                .log("Received : ${body}");
    }
}
