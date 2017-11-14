package com.leibangzhu.iris.client;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.leibangzhu.iris.core.Endpoint;
import com.leibangzhu.iris.registry.IRegistry;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ConnectManager implements IConnectManager{

    private IRegistry registry;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private AtomicInteger roundRobin = new AtomicInteger(0);
    private Map<String,List<Channel>> channelsByService = new LinkedHashMap<>();

    public ConnectManager(IRegistry registry){
        this.registry = registry;
    }

    public Channel getChannel(String serviceName) throws Exception {
        if (!channelsByService.containsKey(serviceName)){
            List<Endpoint> endpoints = registry.find(serviceName);
            List<Channel> channels = new ArrayList<>();
            for (Endpoint endpoint : endpoints){
                channels.add(connect(endpoint.getHost(),endpoint.getPort()));
            }
            channelsByService.put(serviceName,channels);
        }

        // select one channel from all available channels
        int size = channelsByService.get(serviceName).size();
        int index = (roundRobin.getAndAdd(1) + size) % size;
        return channelsByService.get(serviceName).get(index);
    }

    private Channel connect(String host,int port) throws Exception {

        Bootstrap b = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());

        Channel channel = b.connect(host,port).sync().channel();
        return channel;
    }
}
