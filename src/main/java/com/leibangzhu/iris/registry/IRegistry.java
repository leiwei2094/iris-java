package com.leibangzhu.iris.registry;


import com.leibangzhu.iris.core.Endpoint;

import java.util.List;

public interface IRegistry {

    // 注册服务
    void register(String serviceName, int port) throws Exception;

    // 取消注册服务
    void unRegistered(String serviceName);

    List<Endpoint> find(String serviceName) throws Exception;

    void watch(IEventCallback callback);

    void keepAlive();

    //void watch();
}
