package com.laomei.test.gatewaytest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

/**
 * Hystrix降级
 * @author luobo.hwz on 2022/05/07 11:28 AM
 */
@Slf4j
@RestController
public class FallbackController {

    @GetMapping("/fallback")
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public String fallback(ServerWebExchange exchange) {
        Exception exception = exchange.getAttribute(ServerWebExchangeUtils.HYSTRIX_EXECUTION_EXCEPTION_ATTR);
        if (exception != null) {
            log.error("[fallback] request error", exception);
        }
        return "fallback, hello world";
    }
}
