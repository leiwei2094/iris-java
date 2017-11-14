package com.leibangzhu.iris.bytebuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.Test;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;


public class ByteBuddyTest {

    @Test
    public void test() throws Exception {
        IHelloService helloService = new ByteBuddy()
                .subclass(IHelloService.class)
                .method(isDeclaredBy(IHelloService.class)).intercept(MethodDelegation.to(new GeneralInterceptor()))
                .make()
                .load(getClass().getClassLoader())
                .getLoaded()
                .newInstance();
        helloService.hello("world");
    }
}
