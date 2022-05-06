package com.leiwei2094.iris.core.loadbalance;

import com.leiwei2094.coco.Adaptive;
import com.leiwei2094.coco.Extension;

import java.util.Map;

@Extension(defaultValue = "random")
public interface ILoadBalance {
    int select(@Adaptive("loadbalance")Map<String,String> config,int amount) throws Exception;
}
