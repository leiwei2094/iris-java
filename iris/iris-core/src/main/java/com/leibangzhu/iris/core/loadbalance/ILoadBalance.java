package com.leibangzhu.iris.core.loadbalance;

import com.leibangzhu.coco.Adaptive;
import com.leibangzhu.coco.Extension;

import java.util.Map;

@Extension(defaultValue = "random")
public interface ILoadBalance {
    int select(@Adaptive("loadbalance")Map<String,String> config,int amount) throws Exception;
}
