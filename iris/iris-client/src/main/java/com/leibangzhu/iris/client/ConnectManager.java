package com.leibangzhu.iris.client;

import com.leibangzhu.coco.ExtensionLoader;
import com.leibangzhu.iris.core.Endpoint;
import com.leibangzhu.iris.core.IrisConfig;
import com.leibangzhu.iris.core.loadbalance.ILoadBalance;
import com.leibangzhu.iris.registry.IEventCallback;
import com.leibangzhu.iris.registry.IRegistry;
import com.leibangzhu.iris.registry.RegistryEvent;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectManager implements IConnectManager,IEventCallback{

    private IRegistry registry;
    private EventLoopGroup eventLoopGroup = new NioEventLoopGroup(4);
    private AtomicInteger roundRobin = new AtomicInteger(0);
    private Map<String,List<ChannelWrapper>> channelsByService = new LinkedHashMap<>();

    public ConnectManager(IRegistry registry){
        this.registry = registry;
        this.registry.watch(this);
    }

    public Channel getChannel(String serviceName) throws Exception {
        if (!channelsByService.containsKey(serviceName)){
            List<Endpoint> endpoints = registry.find(serviceName);
            List<ChannelWrapper> channels = new ArrayList<>();
            for (Endpoint endpoint : endpoints){
                channels.add(connect(endpoint.getHost(),endpoint.getPort()));
            }
            channelsByService.put(serviceName,channels);
        }

        // select one channel from all available channels
        int size = channelsByService.get(serviceName).size();
        ILoadBalance loadBalance = ExtensionLoader.getExtensionLoader(ILoadBalance.class).getAdaptiveInstance();
        if ( 0 == size){
            System.out.println("NO available providers for service: " + serviceName);
        }
        //int index = (roundRobin.getAndAdd(1) + size) % size;
        String loadbalance = IrisConfig.get("iris.loadbalance");
        Map<String,String> map = new LinkedHashMap<>();
        map.put("loadbalance",loadbalance);
        int index = loadBalance.select(map,size);
        ChannelWrapper channelWrapper = channelsByService.get(serviceName).get(index);
        System.out.println("Load balance:" + loadbalance + "; Selected endpoint: " + channelWrapper.toString());
        return channelWrapper.getChannel();
    }

    private ChannelWrapper connect(String host,int port) throws Exception {

        Bootstrap b = new Bootstrap()
                .group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new RpcClientInitializer());

        Channel channel = b.connect(host,port).sync().channel();
        ChannelWrapper channelWrapper = new ChannelWrapper(new Endpoint(host,port),channel);
        return channelWrapper;
    }

    @Override
    public void execute(RegistryEvent event) {
        if (event.getEventType() == RegistryEvent.EventType.DELETE){

            // key:   /iris/com.leibangzhu.iris.bytebuddy.IHelloService/192.168.41.215:2017

            String s = event.getKeyValue().getKey();
            String serviceName = s.split("/")[2];             // com.leibangzhu.iris.bytebuddy.IHelloService
            String endpointStr = s.split("/")[3];

            String host = endpointStr.split(":")[0];          //  192.168.41.215
            int port = Integer.valueOf(endpointStr.split(":")[1]);    // 2017

            Iterator<ChannelWrapper> iterator = channelsByService.get(serviceName).iterator();
            while (iterator.hasNext()){
                Endpoint endpoint = iterator.next().getEndpoint();
                if (endpoint.getHost().equals(host) && (endpoint.getPort() == port)){
                    iterator.remove();
                }
            }
        }

        if (event.getEventType() == RegistryEvent.EventType.PUT){

            // key:   /iris/com.leibangzhu.iris.bytebuddy.IHelloService/192.168.41.215:2017

            String s = event.getKeyValue().getKey();
            String serviceName = s.split("/")[2];             // com.leibangzhu.iris.bytebuddy.IHelloService
            String endpointStr = s.split("/")[3];

            String host = endpointStr.split(":")[0];          //  192.168.41.215
            int port = Integer.valueOf(endpointStr.split(":")[1]);    // 2017

            try {
                channelsByService.get(serviceName).add(connect(host,port));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class ChannelWrapper{
        private Endpoint endpoint;
        private Channel channel;

        public ChannelWrapper(Endpoint endpoint,Channel channel){
            this.endpoint = endpoint;
            this.channel = channel;
        }

        public Endpoint getEndpoint() {
            return endpoint;
        }

        public Channel getChannel() {
            return channel;
        }

        @Override
        public String toString() {
            return endpoint.getHost() + ":" + endpoint.getPort();
        }
    }
}
