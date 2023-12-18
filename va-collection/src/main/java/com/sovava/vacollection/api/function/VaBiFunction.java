package com.sovava.vacollection.api.function;

/**
 * description: 接受两个参数并产生结果
 *
 * @Author sovava
 * @Date 12/18/23 7:40 PM
 */
@FunctionalInterface
public interface VaBiFunction<F,S,R> {
    R apply(F f,S s);
}
