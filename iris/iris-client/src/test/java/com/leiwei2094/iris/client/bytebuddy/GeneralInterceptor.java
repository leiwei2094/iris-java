package com.leiwei2094.iris.client.bytebuddy;

import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.reflect.Method;

public class GeneralInterceptor {

    @RuntimeType
    public String intercept(@AllArguments Object[] allArguments, @Origin Method method){
        String name = method.getDeclaringClass().getName();
        System.out.println(name + "." + method.getName());
        return "";
    }
}
