package com.santo.javacamp.week03.apigw.outbound;

import com.santo.javacamp.week03.apigw.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.MalformedURLException;

public interface OutboundHandler {
    public void handle(final FullHttpRequest fullRequest, final ChannelHandlerContext ctx, HttpRequestFilter filter)
            throws InterruptedException, MalformedURLException;
}