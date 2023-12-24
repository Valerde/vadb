package com.sovava.vadb.singleton.test.collection.vamap;


import java.util.Arrays;


/**
 * 测试hashmap中
 * <pre>{@code
 *     int h;
 *     return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
 * }</pre>
 * <p>
 * 的作用
 * 对于连续整数型的的float，如果不加扰动，在小表中保存时，hash冲突非常大，达到了100%
 * <p>而加了扰动后，hash冲突降为了50%
 * 结果如下：
 * <p>
 * 1
 * <p>502
 *
 */
public class TestForHashMapHashFunc {
    public static void main(String[] args) {

        int h;
        System.out.println(Integer.toBinaryString(1023));

        int[] ints1 = new int[1024];
        int[] ints2 = new int[1024];
        for (Float f = 1f; f < 1000f; f++) {
//            System.out.println(f);
            int hash1 = f.hashCode() & (1023);
//            System.out.println(Integer.toBinaryString(hash1));
            ints1[hash1] = 1;
            int hash2 = ((h = f.hashCode()) ^ (h >>> 16)) & 1023;
//            System.out.println(Integer.toBinaryString(hash2));
            ints2[hash2] = 1;
        }
        System.out.println(Arrays.stream(ints1).sum());
        System.out.println(Arrays.stream(ints2).sum());
    }
}
