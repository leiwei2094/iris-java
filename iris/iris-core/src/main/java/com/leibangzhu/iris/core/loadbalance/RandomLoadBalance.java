package com.leibangzhu.iris.core.loadbalance;

import java.util.Map;
import java.util.Random;

public class RandomLoadBalance implements ILoadBalance {

    private Random random = new Random();

    @Override
    public int select(Map<String, String> config, int amount) throws Exception {
        System.out.println("RandomLoadBalance to select ...");
        if (amount <= 0){
            throw new Exception("RandomLoadBalance: no available items to select");
        }else if (amount == 1){
            return 0;
        }else {
            return random.nextInt(amount - 1);
        }
    }
}
