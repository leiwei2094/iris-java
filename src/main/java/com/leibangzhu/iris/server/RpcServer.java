package com.leibangzhu.iris.server;

import com.leibangzhu.iris.registry.IRegistry;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.LinkedHashMap;
import java.util.Map;

public class RpcServer {

    private String host = "127.0.0.1";
    private IRegistry registry;
    private int port = 2100;

    private Map<String,Object> handlerMap = new LinkedHashMap<>();

    public RpcServer(IRegistry registry){
        this.registry = registry;
    }

    public RpcServer exposeService(Class<?> clazz,Object handler) throws Exception {
        handlerMap.put(clazz.getName(),handler);
        registry.register(clazz.getName(),port);
        return this;
    }

    public RpcServer port(int port){
        this.port = port;
        return this;
    }

    public void run() throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new RpcServerInitializer(handlerMap))
                .option(ChannelOption.SO_BACKLOG,128)
                .childOption(ChannelOption.SO_KEEPALIVE,true);
        ChannelFuture future = bootstrap.bind(port).sync();
        future.channel().closeFuture().sync();
    }
}
