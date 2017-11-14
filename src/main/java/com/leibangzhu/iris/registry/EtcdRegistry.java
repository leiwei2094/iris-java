package com.leibangzhu.iris.registry;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Lease;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.data.KeyValue;
import com.coreos.jetcd.kv.GetResponse;
import com.coreos.jetcd.options.GetOption;
import com.coreos.jetcd.options.LeaseOption;
import com.coreos.jetcd.options.PutOption;
import com.coreos.jetcd.options.WatchOption;
import com.coreos.jetcd.watch.WatchEvent;
import com.leibangzhu.iris.core.Endpoint;
import com.leibangzhu.iris.core.IpHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class EtcdRegistry implements IRegistry{

    private String registryAddress = "http://127.0.0.1:2379";
    private final String rootPath = "iris";
    private Lease lease;
    private KV kv;
    private Watch watch;
    private long leaseId;

    private Map<String,List<Endpoint>> endpointsByService = new LinkedHashMap<>();
    private IEventCallback callback;

    public EtcdRegistry(String registryAddress) throws Exception {
        this.registryAddress = registryAddress;
        Client client = Client.builder().endpoints(registryAddress).build();
        this.lease   = client.getLeaseClient();
        this.kv      = client.getKVClient();
        this.watch   = client.getWatchClient();
        this.leaseId = lease.grant(30).get().getID();
        System.out.println("New lease, id:" + leaseId + ", Hex format: " + Long.toHexString(leaseId));
        keepAlive();
        watch();
    }

    // 向ETCD中注册服务
    public void register(String serviceName,int port) throws Exception {
        // 服务注册的key为:    /iris/com.some.package.IHelloService/192.168.100.100:2000
        String strKey = MessageFormat.format("/{0}/{1}/{2}:{3}",rootPath,serviceName,IpHelper.getHostIp(),String.valueOf(port));
        ByteSequence key = ByteSequence.fromString(strKey);
        ByteSequence val = ByteSequence.fromString("");     // 目前只需要创建这个key,对应的value暂不使用,先留空
        kv.put(key,val, PutOption.newBuilder().withLeaseId(leaseId).build()).get();
        System.out.println("Register a new service at:" + strKey);
    }

    // 发送心跳到ETCD,表明该host是活着的
    private void keepAlive(){
        Executors.newSingleThreadExecutor().submit(
                () -> {
                    try {
                        Lease.KeepAliveListener listener = lease.keepAlive(leaseId);
                        listener.listen();
                        System.out.println("KeepAlive lease:" + leaseId + "; Hex format:" + Long.toHexString(leaseId));
                    } catch (Exception e) { e.printStackTrace(); }
                }
        );
    }

    // 取消注册服务
    public void unRegistered(String serviceName){

    }

    public List<Endpoint> find(String serviceName) throws Exception {

        if(endpointsByService.containsKey(serviceName)){
            return endpointsByService.get(serviceName);
        }

        String strKey = MessageFormat.format("/{0}/{1}",rootPath,serviceName);   //    /iris/com.leibangzhu.IHelloService
        ByteSequence key  = ByteSequence.fromString(strKey);
        GetResponse response = kv.get(key, GetOption.newBuilder().withPrefix(key).build()).get();

        List<Endpoint> endpoints = new ArrayList<>();

        for (KeyValue kv : response.getKvs()){
            String s = kv.getKey().toStringUtf8();
            int index = s.lastIndexOf("/");
            String endpointStr = s.substring(index + 1,s.length());

            String host = endpointStr.split(":")[0];
            int port = Integer.valueOf(endpointStr.split(":")[1]);

            //System.out.println(host);
            //System.out.println(port);

            endpoints.add(new Endpoint(host,port));
        }
        endpointsByService.put(serviceName,endpoints);
        return endpoints;
    }

    @Override
    public void watch(IEventCallback callback) {
        this.callback = callback;
    }

    private void watch(){
        Watch.Watcher watcher = watch.watch(ByteSequence.fromString("/" + rootPath),
                WatchOption.newBuilder().withPrefix(ByteSequence.fromString("/" + rootPath)).build());

        Executors.newSingleThreadExecutor().submit((Runnable) () -> {
            while (true) {
                try {
                    for (WatchEvent event : watcher.listen().getEvents()) {
                        System.out.println(event.getEventType());
                        System.out.println(event.getKeyValue().getKey().toStringUtf8());
                        System.out.println(event.getKeyValue().getValue().toStringUtf8());

                        // /iris/com.leibangzhu.IHelloService/192.168.41.215:2000

                        String s = event.getKeyValue().getKey().toStringUtf8();
                        String serviceName = s.split("/")[2];
                        String endpoint = s.split("/")[3];

                        String host = endpoint.split(":")[0];
                        int port = Integer.valueOf(endpoint.split(":")[1]);

                        endpointsByService.get(serviceName).remove(new Endpoint(host,port));

                        if (null != callback){
                            RegistryEvent registryEvent = RegistryEvent
                                    .newBuilder()
                                    .eventType(RegistryEvent.EventType.valueOf(event.getEventType().toString()))
                                    .key(event.getKeyValue().getKey().toStringUtf8())
                                    .value(event.getKeyValue().getValue().toStringUtf8())
                                    .build();

                            callback.execute(registryEvent);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
