package com.leibangzhu.iris.client;

import io.netty.channel.Channel;

public interface IConnectManager {
    Channel getChannel(String serviceName) throws Exception;
}
