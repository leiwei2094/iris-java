package com.leiwei2094.iris.spring;

import com.leiwei2094.iris.core.annotation.Service;
import com.leiwei2094.iris.server.RpcServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceAnnotationBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {
    private ApplicationContext applicationContext;

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (beanClass.isAnnotationPresent(Service.class)) {
            RpcServer rpcServer = applicationContext.getBean(RpcServer.class);
            Service serviceClass = beanClass.getDeclaredAnnotation(Service.class);
            try {
                rpcServer.exposeService(serviceClass.interfaceClass(), bean);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
