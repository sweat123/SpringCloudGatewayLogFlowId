# 涉及到的改动

- `FallbackController`. 配置 Hystrix 降级处理方式。
- `MdcHystrixConfig`. Hystrix 异步线程和 Gateway 主线程 sfl4j MDC同步。
- `FlowFilter`. 全局 Filter，配置 Gateway 的日志流水号。
- `LoggingHandler`. Netty 支持打印请求、响应报文。
- `LogCustomizer`. 配置 LoggingHandler，使其生效。
- `NettyRoutingFilter`. 进行小部分改动，传递 Gateway 流水号给 Netty 线程。
- `logback.xml`. 增加对流水号的打印。
