package com.leiwei2094.iris.client;

import com.leiwei2094.iris.registry.IRegistry;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;

import java.util.LinkedHashMap;
import java.util.Map;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;

public class RpcClient implements IRpcClient {

    ConnectManager connectManager;
    private IRegistry registry;
    private Map<String, Object> proxyByClass = new LinkedHashMap<>();

    public RpcClient(IRegistry registry) {
        this.registry = registry;
        this.connectManager = new ConnectManager(registry);
        //this.registry.watch();
    }

    @Override
    public <T> T create(Class<T> clazz) throws Exception {
        if (!proxyByClass.containsKey(clazz.getName())) {
            T proxy = new ByteBuddy()
                .subclass(clazz)
                .method(isDeclaredBy(clazz)).intercept(MethodDelegation.to(new RpcInvokeInterceptor(connectManager)))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();

            proxyByClass.put(clazz.getName(), proxy);
        }

        return (T)proxyByClass.get(clazz.getName());
    }
}
