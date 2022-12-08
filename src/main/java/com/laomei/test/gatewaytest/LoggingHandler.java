package com.laomei.test.gatewaytest;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.UUID;

@Slf4j
public class LoggingHandler extends ChannelDuplexHandler {

    private static final AttributeKey<String> TRACE_ID = AttributeKey.valueOf("traceId");

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf) {
            if (!ctx.channel().hasAttr(TRACE_ID) || ctx.channel().attr(TRACE_ID).get().equals("")) {
                final String uuid = UUID.randomUUID().toString();
                ctx.channel().attr(TRACE_ID).set(uuid);
            }
            final ByteBuf buf = (ByteBuf) msg;
            int length = buf.readableBytes();
            MDC.put("trace", ctx.channel().attr(TRACE_ID).get());
            if (length > 0) {
                final String content = buf.toString(StandardCharsets.UTF_8);
                //换行
                final StringBuilder sb = new StringBuilder();
                Arrays.stream(content.split("\r\n|\n"))
                        .forEach(str -> sb.append(str).append("\n"));
                log.info("[request body]: [{}]", sb.toString());
            }
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            if (ctx.channel().hasAttr(TRACE_ID)) {
                MDC.put("trace", ctx.channel().attr(TRACE_ID).get());
                ctx.channel().attr(TRACE_ID).set("");
            }
            final ByteBuf buf = (ByteBuf) msg;
            int length = buf.readableBytes();
            if (length > 0) {
                String content = buf.toString(StandardCharsets.UTF_8);
                final StringBuilder sb = new StringBuilder(length);
                Arrays.stream(content.split("\r\n|\n"))
                        .forEach(str -> sb.append(str).append("\n"));
                log.info("[response meta]: [{}]", sb.toString());
            }
        }
        super.channelRead(ctx, msg);
    }
}