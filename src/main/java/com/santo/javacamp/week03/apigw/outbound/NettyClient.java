package com.santo.javacamp.week03.apigw.outbound;

import com.santo.javacamp.week03.apigw.filter.HttpRequestFilter;
import com.santo.javacamp.week03.apigw.outbound.OutboundHandler;
import com.santo.javacamp.week03.apigw.router.HttpEndpointRouter;
import com.santo.javacamp.week03.apigw.router.RandomHttpEndpointRouter;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;

import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class NettyClient implements OutboundHandler {

    private List<String> backendUrls;
    private HttpEndpointRouter router = new RandomHttpEndpointRouter();
    public NettyClient(List<String> backendUrls) {
        this.backendUrls = backendUrls;
    }

    @Override
    public void handle(FullHttpRequest fullRequest, ChannelHandlerContext serverCtx, HttpRequestFilter requestFilter) \
            throws InterruptedException, MalformedURLException {
        EventLoopGroup group = new NioEventLoopGroup();
        String backendUrl = router.route(backendUrls);
        URL url = new URL(backendUrl);
        String host = url.getHost();
        int port = url.getPort();
        String path = url.getPath();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .remoteAddress(new InetSocketAddress(host, port))
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline
                                    .addLast(new HttpClientCodec())
                                    .addLast(new HttpObjectAggregator(512 * 1024))
                                    .addLast(new NettyOutboundHandler(path, fullRequest, serverCtx, requestFilter));
                        }
                    });
            ChannelFuture f = b.connect().sync();
            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }
}