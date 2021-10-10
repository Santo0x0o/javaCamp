package com.santo.javacamp.week03.apigw.inbound;

import com.santo.javacamp.week03.apigw.filter.HeaderHttpRequestFilter;
import com.santo.javacamp.week03.apigw.filter.HttpRequestFilter;
import com.santo.javacamp.week03.apigw.outbound.OutboundHandler;
import com.santo.javacamp.week03.apigw.outbound.NettyClient;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.List;

public class HttpInboundHandler extends ChannelInboundHandlerAdapter {
    private OutboundHandler client;
    private HttpRequestFilter filter = new HeaderHttpRequestFilter();
    public HttpInboundHandler(List<String> proxyServer) {
        try {
            this.client = new NettyClient(proxyServer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext serverCtx, Object msg) throws Exception {
        try {
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            client.handle(fullRequest, serverCtx, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}