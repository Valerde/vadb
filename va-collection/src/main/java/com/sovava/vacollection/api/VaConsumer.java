package com.sovava.vacollection.api;

/**
 * description: 通过函数时编程输入一个参数但是不返回结果的操作
 *
 * @Author sovava
 * @Date 12/18/23 5:07 PM
 */
@FunctionalInterface
public interface VaConsumer<T> {
    /**
     * description: 对参数进行操作
     *
     * @Author sovava
     * @Date 12/18/23 5:12 PM
     * @param: t - [T]
     */
    void accept(T t);
}
