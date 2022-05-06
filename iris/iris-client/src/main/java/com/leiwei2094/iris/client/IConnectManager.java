package com.leiwei2094.iris.client;

import io.netty.channel.Channel;

public interface IConnectManager {
    Channel getChannel(String serviceName) throws Exception;
}
