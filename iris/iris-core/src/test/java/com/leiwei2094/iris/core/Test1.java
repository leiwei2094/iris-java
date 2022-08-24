package com.leiwei2094.iris.core;

import org.junit.Test;

import java.util.Random;

public class Test1 {

    @Test
    public void test_random() {
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            System.out.println(random.nextInt(10));
        }
    }
}
