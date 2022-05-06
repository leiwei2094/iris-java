package com.leiwei2094.iris.registry.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.KV;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.kv.GetResponse;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

@Ignore
public class PutTest {

    @Test
    public void test() throws Exception {

        Client client = Client.builder().endpoints("http://localhost:2379").build();
        KV kvClient = client.getKVClient();

        ByteSequence key = ByteSequence.fromString("abc");
        ByteSequence value = ByteSequence.fromString("240");

        // put the key-value
        kvClient.put(key, value).get();
// get the CompletableFuture
        CompletableFuture<GetResponse> getFuture = kvClient.get(key);
// get the value from CompletableFuture
        GetResponse response = getFuture.get();
// delete the key
        //DeleteResponse deleteRangeResponse = kvClient.delete(key).get();
    }
}
