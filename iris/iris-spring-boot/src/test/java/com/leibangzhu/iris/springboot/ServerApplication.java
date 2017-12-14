package com.leibangzhu.iris.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        InputStream in = ClientApplication.class.getClassLoader().getResourceAsStream("application-server.properties");
        properties.load(in);
        SpringApplication app = new SpringApplication(ClientApplication.class);
        app.setDefaultProperties(properties);
        app.run(args);
    }
}
