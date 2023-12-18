package com.sovava.vacollection.api.function;

/**
 * description: 表示一个参数的断言
 *
 * @Author sovava
 * @Date 12/18/23 5:57 PM
 */
@FunctionalInterface
public interface VaPredicate<T> {
    /**
     * description: 判断输入的参数t是否满足断言
     *
     * @Author sovava
     * @Date 12/18/23 6:02 PM
     * @param: t - [T]
     * @return boolean
     */
    boolean test(T t);
}
