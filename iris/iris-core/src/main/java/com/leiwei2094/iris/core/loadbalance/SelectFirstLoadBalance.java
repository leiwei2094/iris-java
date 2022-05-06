package com.leiwei2094.iris.core.loadbalance;

import java.util.Map;

public class SelectFirstLoadBalance implements ILoadBalance{
    @Override
    public int select(Map<String, String> config, int amount) throws Exception {
        System.out.println("SelectFirstLoadBalance to select ...");
        return 0;
    }
}
