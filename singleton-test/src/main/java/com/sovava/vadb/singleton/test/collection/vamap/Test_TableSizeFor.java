package com.sovava.vadb.singleton.test.collection.vamap;

import java.util.HashMap;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2023年12月22日 8:02 PM
 **/
public class Test_TableSizeFor {

    static final int MAXIMUM_CAPACITY = 1 << 30;

    static final int tableSizeFor(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    public static void main(String[] args) {
        float a = Float.NaN;
        System.out.println(Float.isNaN(a));
        System.out.println(a == a);
        System.out.println(tableSizeFor(7));
    }
}
