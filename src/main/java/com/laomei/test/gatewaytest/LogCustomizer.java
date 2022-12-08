package com.laomei.test.gatewaytest;

import org.springframework.cloud.gateway.config.HttpClientCustomizer;
import org.springframework.stereotype.Component;
import reactor.netty.channel.BootstrapHandlers;
import reactor.netty.http.client.HttpClient;

/**
 * @author luobo.hwz on 2022/12/08 10:09 AM
 */
@Component
public class LogCustomizer implements HttpClientCustomizer {
    @Override
    public HttpClient customize(HttpClient client) {
        return client.tcpConfiguration(tcpClient ->
                tcpClient.bootstrap(b -> BootstrapHandlers.updateConfiguration(b, "log",
                        ((connectionObserver, channel) -> channel.pipeline().addFirst("log", new LoggingHandler())))));
    }
}
