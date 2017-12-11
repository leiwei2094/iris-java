package com.leibangzhu.iris.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Foo {

    @Autowired
    private ScanClass1 class1;

    @Autowired
    private ScanClass2 class2;

    public void run(){
        class1.print();
        class2.sayHello("leo");
    }
}
