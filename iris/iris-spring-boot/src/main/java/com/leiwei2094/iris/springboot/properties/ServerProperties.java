package com.leiwei2094.iris.springboot.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "iris.server")
public class ServerProperties {

    private int port = 2017;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
