package com.leibangzhu.iris.springboot.config;

import com.leibangzhu.iris.client.RpcClient;
import com.leibangzhu.iris.registry.EtcdRegistry;
import com.leibangzhu.iris.registry.IRegistry;
import com.leibangzhu.iris.server.RpcServer;
import com.leibangzhu.iris.spring.IrisApplicationListener;
import com.leibangzhu.iris.spring.ReferenceAnnotationBeanPostProcessor;
import com.leibangzhu.iris.spring.ServiceAnnotationBeanPostProcessor;
import com.leibangzhu.iris.springboot.ServiceAnnotationBeanFactoryPostProcessor;
import com.leibangzhu.iris.springboot.properties.RegistryProperties;
import com.leibangzhu.iris.springboot.properties.ServerProperties;
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
    public ServiceAnnotationBeanFactoryPostProcessor beanFactoryPostProcessor(@Value("${iris.annotation.package}") String packageName){
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
