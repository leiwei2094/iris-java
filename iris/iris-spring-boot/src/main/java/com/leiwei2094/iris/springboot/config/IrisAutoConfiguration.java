package com.leiwei2094.iris.springboot.config;

import com.leiwei2094.iris.client.RpcClient;
import com.leiwei2094.iris.registry.EtcdRegistry;
import com.leiwei2094.iris.registry.IRegistry;
import com.leiwei2094.iris.server.RpcServer;
import com.leiwei2094.iris.spring.IrisApplicationListener;
import com.leiwei2094.iris.spring.ReferenceAnnotationBeanPostProcessor;
import com.leiwei2094.iris.spring.ServiceAnnotationBeanPostProcessor;
import com.leiwei2094.iris.springboot.ServiceAnnotationBeanFactoryPostProcessor;
import com.leiwei2094.iris.springboot.properties.RegistryProperties;
import com.leiwei2094.iris.springboot.properties.ServerProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({RegistryProperties.class,ServerProperties.class})
public class IrisAutoConfiguration {

    @Bean
    public IRegistry registry(RegistryProperties properties) throws Exception {
        EtcdRegistry registry = new EtcdRegistry(properties.getAddress());
        return registry;
    }

    @ConditionalOnProperty(prefix = "iris.server", value = "enable",havingValue = "true")
    @Bean
    public RpcServer rpcServer(IRegistry registry, ServerProperties properties){
        RpcServer server = new RpcServer(registry);
        server.port(properties.getPort());
        return server;
    }

    @ConditionalOnProperty(prefix = "iris.server", value = "enable",havingValue = "true")
    @Bean
    public static ServiceAnnotationBeanFactoryPostProcessor beanFactoryPostProcessor(@Value("${iris.annotation.package}") String packageName){
        ServiceAnnotationBeanFactoryPostProcessor processor = new ServiceAnnotationBeanFactoryPostProcessor(packageName);
        return processor;
    }

    @ConditionalOnProperty(prefix = "iris.server", value = "enable",havingValue = "true")
    @Bean
    public ServiceAnnotationBeanPostProcessor serviceAnnotationBeanPostProcessor(){
        ServiceAnnotationBeanPostProcessor processor = new ServiceAnnotationBeanPostProcessor();
        return processor;
    }

    @ConditionalOnProperty(prefix = "iris.client", value = "enable",havingValue = "true")
    @Bean
    public ReferenceAnnotationBeanPostProcessor referenceAnnotationBeanPostProcessor(){
        ReferenceAnnotationBeanPostProcessor processor = new ReferenceAnnotationBeanPostProcessor();
        return processor;
    }

    @ConditionalOnProperty(prefix = "iris.server", value = "enable",havingValue = "true")
    @Bean
    public IrisApplicationListener applicationListener(){
        IrisApplicationListener applicationListener = new IrisApplicationListener();
        return applicationListener;
    }

    @ConditionalOnProperty(prefix = "iris.client", value = "enable",havingValue = "true")
    @Bean
    public RpcClient client(IRegistry registry){
        RpcClient client = new RpcClient(registry);
        return client;
    }
}
