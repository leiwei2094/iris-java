package com.leiwei2094.iris.spring;

import com.leiwei2094.iris.server.RpcServer;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public class IrisApplicationListener implements ApplicationListener,ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(ApplicationEvent applicationEvent) {
        // 所有bean都已经实例化好了
        // 可以启动RpcServer了
        if (applicationEvent instanceof ContextRefreshedEvent){
            RpcServer server = applicationContext.getBean(RpcServer.class);
            try {
                server.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
