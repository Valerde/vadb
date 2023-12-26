package com.sovava.vacollection.collections;

import java.lang.reflect.Array;

/**
 * Description: 包含各种操作数组的方法
 *
 * @author: ykn
 * @date: 2023年12月19日 5:47 PM
 **/
public class VaArrays {

    /**
     * description: 通过方法私有抑制默认构造器，确保类不可实例化
     *
     * @Author sovava
     * @Date 12/19/23 5:52 PM
     */
    private VaArrays() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T[] copyOf(T[] original, int newLen) {
        return (T[])copyOf(original,newLen,original.getClass());
    }

    @SuppressWarnings("unchecked")
    public static <T, O> T[] copyOf(O[] original, int newLen, Class<? extends T[]> newType) {
        T[] copy;
        if ((Object)newType == (Object)Object[].class) {// 如果喜欢转换的类型是Object[].class,就直接创建，不需要反射
            copy = (T[]) new Object[newLen]; //代码运行到这里一定是Object类型,只不过不强转类型过不去编译
        } else {
            //通过反射创建新数组
            copy = (T[]) Array.newInstance(newType.getComponentType(), newLen);
        }
        //系统底层实现的数组拷贝
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }

    // 以下基本数据类型的拷贝均为减少反射开销
    public static short[] copyOf(short[] original, int newLen) {
        short[] copy = new short[newLen];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }

    public static int[] copyOf(int[] original, int newLen) {
        int[] copy = new int[newLen];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }

    public static long[] copyOf(long[] original, int newLen) {
        long[] copy = new long[newLen];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }

    public static char[] copyOf(char[] original, int newLen) {
        char[] copy = new char[newLen];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }

    public static float[] copyOf(float[] original, int newLen) {
        float[] copy = new float[newLen];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }

    public static double[] copyOf(double[] original, int newLen) {
        double[] copy = new double[newLen];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }

    public static boolean[] copyOf(boolean[] original, int newLen) {
        boolean[] copy = new boolean[newLen];
        System.arraycopy(original, 0, copy, 0, Math.min(original.length, newLen));
        return copy;
    }
    //-----------------------------copyOf end , sort start ---------------------------------------------

}
