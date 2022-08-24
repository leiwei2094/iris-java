package com.leiwei2094.iris.core;

/**
 * @author wei.lei
 * 代表一个RPC示例，包含主机和端口
 */
public class Endpoint {
    private final String host;
    private final int port;

    public Endpoint(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Endpoint)) {
            return false;
        }
        Endpoint other = (Endpoint)o;
        return other.host.equals(this.host) && other.port == this.port;
    }

    @Override
    public int hashCode() {
        return host.hashCode() + port;
    }
}
