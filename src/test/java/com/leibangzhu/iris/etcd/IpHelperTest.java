package com.leibangzhu.iris.etcd;

import com.leibangzhu.iris.core.IpHelper;
import org.junit.Test;

import java.text.MessageFormat;

public class IpHelperTest {

    @Test
    public void test() throws Exception {
        String ip = IpHelper.getHostIp();
        System.out.println(ip);

        System.out.println(MessageFormat.format("a{0}b{1}c{2}","foo","bar",100));
    }
}
