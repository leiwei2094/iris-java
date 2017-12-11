package com.leibangzhu.iris.spring;

import com.leibangzhu.iris.core.annotation.Reference;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessBeforeInitialization...");
        if (beanName.toLowerCase().contains("bar")){

            if(bean.getClass().getDeclaredFields()[0].isAnnotationPresent(Reference.class)){
                Reference annotation = bean.getClass().getDeclaredFields()[0].getAnnotation(Reference.class);
                Field field = bean.getClass().getDeclaredFields()[0];
                System.out.println("beanName has @Reference annotation field: " + field.getName());
                field.setAccessible(true);
                try {
                    field.set(bean,new SimpleHelloService());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("postProcessBeforeInitialization......before [foo] bean");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("postProcessAfterInitialization...");
        if (beanName.toLowerCase().contains("bar")){
            System.out.println("postProcessAfterInitialization......before [foo] bean");
        }
        return bean;
    }
}
