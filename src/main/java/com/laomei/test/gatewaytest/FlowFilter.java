package com.laomei.test.gatewaytest;

import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * 设置Gateway的全局唯一流水
 * @author luobo.hwz on 2022/12/08 10:34 AM
 */
@Component
public class FlowFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String trace = UUID.randomUUID().toString();
        MDC.put("trace", trace);
        return chain.filter(exchange).then(Mono.fromRunnable(MDC::clear));
    }

    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }
}
