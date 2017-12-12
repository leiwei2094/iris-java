package com.qibeitech.demob;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class App {

    @Autowired
    private Foo foo;

//    @Autowired
//    private Bar bar;

    @RequestMapping("/")
    String home() throws Exception {
//        String s = foo.hello();
        String s = foo.hello();
        return s;
    }

    public static void main(String[] args){
        SpringApplication.run(App.class,args);
    }
}
