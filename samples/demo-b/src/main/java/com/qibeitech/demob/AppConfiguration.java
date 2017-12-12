package com.qibeitech.demob;

import com.leibangzhu.iris.client.IRpcClient;
import com.leibangzhu.iris.client.RpcClient;
import com.leibangzhu.iris.registry.EtcdRegistry;
import com.leibangzhu.iris.registry.IRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfiguration {

    @Bean
    public IRegistry registry() throws Exception {
        IRegistry registry = new EtcdRegistry("http://127.0.0.1:2379");
        return registry;
    }

    @Bean
    public IRpcClient rpcClient(IRegistry registry){
        RpcClient client = new RpcClient(registry);
        return client;
    }
}
