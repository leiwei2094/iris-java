package com.leibangzhu.iris.client;

public interface IRpcClient {

    <T> T create(Class<T> clazz) throws Exception;
}
