package com.leiwei2094.iris.client;

public interface IRpcClient {

    <T> T create(Class<T> clazz) throws Exception;
}
