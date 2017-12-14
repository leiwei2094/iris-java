package com.leibangzhu.iris.springboot;

import com.leibangzhu.iris.core.annotation.Service;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Set;

public class ServiceAnnotationScanner extends ClassPathBeanDefinitionScanner {
    public ServiceAnnotationScanner(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public void registerDefaultFilters() {
        this.addIncludeFilter(new AnnotationTypeFilter(Service.class));
    }

    public Set<BeanDefinitionHolder> doScan(String... basePackages) {
        Set<BeanDefinitionHolder> beanDefinitions = super.doScan(basePackages);
//        for (BeanDefinitionHolder holder : beanDefinitions) {
//            GenericBeanDefinition definition = (GenericBeanDefinition) holder.getBeanDefinition();
//            definition.getPropertyValues().add("innerClassName", definition.getBeanClassName());
//            definition.setBeanClass(FactoryBeanTest.class);
//        }
        return beanDefinitions;
    }
}
