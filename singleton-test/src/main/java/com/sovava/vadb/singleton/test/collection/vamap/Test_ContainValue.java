package com.sovava.vadb.singleton.test.collection.vamap;

import java.util.HashMap;
import java.util.Map;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2023年12月23日 9:44 PM
 **/
public class Test_ContainValue {
    public static void main(String[] args) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            map.put(i, 100 - i);
        }

        System.out.println(map.containsValue(13));

    }


}
