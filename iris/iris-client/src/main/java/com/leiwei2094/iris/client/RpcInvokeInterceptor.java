package com.leiwei2094.iris.client;

import com.leiwei2094.iris.protocol.RpcRequest;
import io.netty.channel.Channel;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;
import java.util.UUID;

public class RpcInvokeInterceptor {

    private IConnectManager connectManager;

    public RpcInvokeInterceptor(IConnectManager connectManager) {
        this.connectManager = connectManager;
    }

    @RuntimeType
    public Object intercept(@AllArguments Object[] args, @Origin Method method) throws Exception {
        String name = method.getDeclaringClass().getName();
        System.out.println(name);
        // create rpc request
        RpcRequest request = new RpcRequest();
        request.setRequestId(UUID.randomUUID().toString());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setParameters(args);
        // get a connect from connect manager
        Channel channel = connectManager.getChannel(method.getDeclaringClass().getName());
        // send the rpc request via the connect

        RpcFuture future = new RpcFuture();
        RpcRequestHolder.put(request.getRequestId(), future);

        channel.writeAndFlush(request);

        Object result = null;
        try {
            result = future.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
