package com.leibangzhu.iris.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.Lease;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.lease.LeaseGrantResponse;
import com.coreos.jetcd.options.PutOption;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

public class LeaseTest {
    @Test
    public void test() throws Exception {
        Client client = Client.builder().endpoints("http://127.0.0.1:2379").build();
        Lease lease = client.getLeaseClient();

        CompletableFuture<LeaseGrantResponse> future = lease.grant(20);
        LeaseGrantResponse response = future.get();
        long leaseId = response.getID();
        System.out.println("New lease, id:" + leaseId + ", Hex format: " + Long.toHexString(leaseId));

        ByteSequence key = ByteSequence.fromString("abc");
        ByteSequence value = ByteSequence.fromString("220");

        KV kv = client.getKVClient();
        kv.put(key,value, PutOption.newBuilder().withLeaseId(leaseId).build());
//
//        CompletableFuture<GetResponse> getResponseFuture = kv.get(key);
//        GetResponse getResponse = getResponseFuture.get();
//        String valueStr = getResponse.getKvs().get(0).getValue().toStringUtf8();
    }


    @Test
    public void test002() throws Exception {
        Client client = Client.builder().endpoints("http://127.0.0.1:2379").build();
        Lease lease = client.getLeaseClient();

        CompletableFuture<LeaseGrantResponse> future = lease.grant(20);
        LeaseGrantResponse response = future.get();
        long leaseId = response.getID();
        System.out.println("New lease, id:" + leaseId + ", Hex format: " + Long.toHexString(leaseId));

        KV kvClient = client.getKVClient();

        for (int i = 0; i< 5;i++){
            ByteSequence testKey = ByteSequence.fromString("abc_key_" + i);
            ByteSequence testVal = ByteSequence.fromString("abc_val_" + i);

            kvClient.put(testKey,testVal,PutOption.newBuilder().withLeaseId(leaseId).build()).get();
            Thread.sleep(3000);
        }

//        ByteSequence key = ByteSequence.fromString("abc");
//        ByteSequence value = ByteSequence.fromString("220");

//        KV kv = client.getKVClient();
//        kv.put(key,value, PutOption.newBuilder().withLeaseId(leaseId).build());
//
//        CompletableFuture<GetResponse> getResponseFuture = kv.get(key);
//        GetResponse getResponse = getResponseFuture.get();
//        String valueStr = getResponse.getKvs().get(0).getValue().toStringUtf8();
    }








}
