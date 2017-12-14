package com.leibangzhu.iris.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "iris.registry")
public class RegistryProperties {

    private String address = "http://127.0.0.1:2379";

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
