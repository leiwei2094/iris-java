package com.leibangzhu.iris.etcd;

import com.coreos.jetcd.Client;
import com.coreos.jetcd.Watch;
import com.coreos.jetcd.data.ByteSequence;
import com.coreos.jetcd.options.WatchOption;
import com.coreos.jetcd.watch.WatchEvent;
import org.junit.Test;

public class WatchTest {

    @Test
    public void testWatchPrefix() throws Exception {

        Client client = Client.builder().endpoints("http://127.0.0.1:2379").build();
        Watch.Watcher watcher = client.getWatchClient().watch(ByteSequence.fromString("abc"),
                WatchOption.newBuilder().withPrefix(ByteSequence.fromString("abc")).build());

        while (true) {
            for (WatchEvent event : watcher.listen().getEvents()) {
                System.out.println(event.getEventType());
                System.out.println(event.getKeyValue().getKey().toStringUtf8());
                System.out.println(event.getKeyValue().getValue().toStringUtf8());
            }
        }
    }
}
