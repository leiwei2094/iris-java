package com.leiwei2094.iris.core;

import java.net.InetAddress;

/**
 * @author wei.lei
 */
public class IpHelper {

    public static String getHostIp() throws Exception {

        String ip = InetAddress.getLocalHost().getHostAddress();
        return ip;
    }
}
