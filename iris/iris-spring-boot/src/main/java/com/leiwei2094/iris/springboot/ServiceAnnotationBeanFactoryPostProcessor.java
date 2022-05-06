package com.leiwei2094.iris.springboot;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class ServiceAnnotationBeanFactoryPostProcessor implements BeanFactoryPostProcessor,ApplicationContextAware {

    private ApplicationContext applicationContext;
    private String scanBasePackage;

    public ServiceAnnotationBeanFactoryPostProcessor(String scanBasePackage){
        this.scanBasePackage = scanBasePackage;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        ServiceAnnotationScanner scanner = new ServiceAnnotationScanner((BeanDefinitionRegistry) beanFactory);
        scanner.setResourceLoader(this.applicationContext);
        scanner.scan(scanBasePackage);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
