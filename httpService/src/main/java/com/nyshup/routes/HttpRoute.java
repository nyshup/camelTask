package com.nyshup.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import static org.apache.camel.builder.PredicateBuilder.and;

@Component
public class HttpRoute extends RouteBuilder {
    public static final String MY_BODY = "MyBody";
    public static final String MY_ACTION = "MyAction";

    @Override
    public void configure() throws Exception {

        from("jetty:http://{{http.server.host}}:{{http.server.port}}/service").routeId("httpRoute")
                .onException(IllegalStateException.class)
                    .handled(true)
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(400))
                    .transform(constant("Bad request"))
                .end()
                .onException(Exception.class)
                    .handled(true)
                    .transform(constant("Internal server error"))
                    .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .end()
                .choice()
                    .when(and(header(MY_ACTION).isNotNull(), header(MY_BODY).isNotNull()))
                        .transform(simple("${header.MyBody}"))
                        .to("file://{{output.file.dir}}")
                        .log(LoggingLevel.INFO, "File saved to {{output.file.dir}}")
                        .setBody(simple(null))
                    .otherwise()
                        .throwException(new IllegalArgumentException("Wrong parameters"))
                .end();
    }
}
