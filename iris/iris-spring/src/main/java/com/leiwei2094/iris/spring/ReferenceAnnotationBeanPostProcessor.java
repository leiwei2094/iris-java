package com.leiwei2094.iris.spring;

import com.leiwei2094.iris.client.RpcClient;
import com.leiwei2094.iris.core.annotation.Reference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

public class ReferenceAnnotationBeanPostProcessor implements BeanPostProcessor,ApplicationContextAware{
    private ApplicationContext applicationContext;

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        Field[] fields = beanClass.getDeclaredFields();
        for (int i =0;i<fields.length;i++){
            Field field = fields[i];
            if (field.isAnnotationPresent(Reference.class)){
                Reference reference = field.getAnnotation(Reference.class);
                field.setAccessible(true);
                try {
                    field.set(bean,applicationContext.getBean(RpcClient.class).create(reference.interfaceClass()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return bean;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
