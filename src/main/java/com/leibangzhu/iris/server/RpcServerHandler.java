package com.leibangzhu.iris.server;

import com.leibangzhu.iris.protocol.RpcRequest;
import com.leibangzhu.iris.protocol.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.Map;

public class RpcServerHandler extends SimpleChannelInboundHandler<RpcRequest>{

    // key: com.some.package.IHelloService    value: new HelloService();
    private final Map<String,Object> handlerMap;

    public RpcServerHandler(Map<String,Object> handlerMap){
        this.handlerMap = handlerMap;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcRequest rpcRequest) throws Exception {
        RpcResponse response = new RpcResponse();
        response.setRequestId(rpcRequest.getRequestId());

        // invoke method by reflection
        String className = rpcRequest.getClassName();
        Object handlerObj = handlerMap.get(className);

        Class<?> handlerClass = handlerObj.getClass();
        String methodName = rpcRequest.getMethodName();
        Class<?>[] parameterTypes = rpcRequest.getParameterTypes();
        Object[] parameters = rpcRequest.getParameters();

        // JDK reflect
        Method method = handlerClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        Object result = method.invoke(handlerObj, parameters);
        response.setResult(result);
        channelHandlerContext.writeAndFlush(response);
    }
}
