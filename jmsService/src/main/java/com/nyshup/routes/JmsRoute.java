package com.nyshup.routes;


import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;

@Component
public class JmsRoute extends RouteBuilder {

    private static final String MY_ACTION = "MyAction";
    private static final String MY_BODY = "MyBody";

    @Override
    public void configure() throws Exception {
        from("activemq:queue:serviceQueue").routeId("jmsRoute")
                .transacted()
                .setHeader(Exchange.CONTENT_TYPE, constant("application/x-www-form-urlencoded"))
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        StringBuilder sb = new StringBuilder();
                        sb.append(MY_ACTION).append("=")
                                .append(URLEncoder.encode((String) exchange.getIn().getHeader(MY_ACTION), "UTF-8"))
                                .append("&")
                                .append(MY_BODY).append("=")
                                .append(URLEncoder.encode((String) exchange.getIn().getBody(), "UTF-8"));
                        exchange.getIn().setBody(sb.toString());
                    }
                })
                .to("http4:{{http.server.host}}:{{http.server.port}}/service")
                .choice()
                    .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo("200"))
                        .log(LoggingLevel.INFO, "Posted successfully")
                    .otherwise()
                        .log(LoggingLevel.ERROR, "Error during post request")
                .end()
                .log("Posted status : ${header.CamelHttpResponseCode}");
    }
}
