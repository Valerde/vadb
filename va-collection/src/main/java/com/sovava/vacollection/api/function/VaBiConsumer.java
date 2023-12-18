package com.sovava.vacollection.api.function;

/**
 * description: 对两个值进行操作，不产生返回值
 *
 * @Author sovava
 * @Date 12/18/23 7:31 PM
 */
@FunctionalInterface
public interface VaBiConsumer<F, S> {
    void apply(F f, S s);
}
