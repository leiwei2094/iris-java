package com.leiwei2094.iris.core;

/**
 * @author wei.lei
 */
public class KeyValue<K, V> {
    private K key;
    private V value;

    public KeyValue() {}

    public KeyValue(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
