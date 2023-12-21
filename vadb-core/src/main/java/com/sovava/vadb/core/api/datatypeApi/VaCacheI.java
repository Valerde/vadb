package com.sovava.vadb.core.api.datatypeApi;


/**
 * description: vadb缓存的最根本接口
 *
 * @Author sovava
 * @Date 12/21/23 7:18 PM
 */
public interface VaCacheI<K, V> {

    /**
     * description: 设置指定key的值为value，默认永不过期
     *
     * @return VaCache<K, V>
     * @Author sovava
     * @Date 12/21/23 7:22 PM
     * @param: key - [K]
     * @param: value - [V]
     */
    VaCacheI<K, V> set(K key, V value);

    /**
     * description: 设置键值及其过期时间
     *
     * @return VaCache<K, V> this
     * @Author sovava
     * @Date 12/21/23 7:35 PM
     * @param: key - [K]
     * @param: value - [V]
     * @param: milliseconds - [long]
     */
    VaCacheI<K, V> setUntil(K key, V value, long milliseconds);

    /**
     * description: 获取一个value
     *
     * @return V
     * @Author sovava
     * @Date 12/21/23 7:37 PM
     * @param: key - [K]
     */
    V get(K key);


    /**
     * description: 根本cache键值对方法
     *
     * @return VaCache<K, V> this
     * @Author sovava
     * @Date 12/21/23 7:10 PM
     * @param: key - [K]
     * @param: milliseconds - [long]
     */
    VaCacheI<K, V> expireUntilMilSec(K key, long milliseconds);

}
