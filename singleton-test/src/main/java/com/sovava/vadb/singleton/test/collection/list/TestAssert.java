package com.sovava.vadb.singleton.test.collection.list;

/**
 * Description: assert测试，需要在vm option上加-ea，当断言错误时产生AssertionError
 *
 * @author: ykn
 * @date: 2023年12月26日 4:41 PM
 **/
public class TestAssert {

    private static void test(int a) {
        assert a > 0;
        System.out.println(a);
    }

    public static void main(String[] args) {
        int a1 = 2;
        int a2 = -1;
        test(a1);
        test(a2);
    }
}
