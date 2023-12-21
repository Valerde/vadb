package com.sovava.vacollection.api;

import com.sovava.vacollection.api.function.VaBiConsumer;
import com.sovava.vacollection.api.function.VaBiFunction;
import com.sovava.vacollection.api.function.VaFunction;

/**
 * description: map类接口
 *
 * TODO:some default methods is unsolved
 *
 * @Author sovava
 * @Date 12/21/23 11:44 PM
 */
public interface VaMap<K, V> {
    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containValue(Object value);

    V get(Object key);

    V put(K key, V value);

    V remove(Object key);


    void putAll(VaMap<? extends K, ? extends V> m);

    void clear();

    VaSet<K> keySet();

    VaCollection<V> values();

    VaSet<VaEntry<K, V>> entrySet();

    boolean equals(Object o);

    int hashCode();

    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return (((v = get(key)) != null) || containsKey(key)) ? v : defaultValue;
    }

    default void forEach(VaBiConsumer<? super K, ? super V> action) {
    }

    default void replaceAll(VaBiFunction<? super K, ? super V, ? extends V> action) {
    }

    default V putIfAbsent(K key, V value) {
        V v = get(key);
        if (null == v) {
            v = put(key, value);
        }
        return v;
    }

    default boolean remove(Object key, Object value) {
        return true;
    }

    /**
     * description: 当Map中key对应的value与oldValue对应时才将其替换为newValue
     *
     * @return boolean
     * @Author sovava
     * @Date 12/18/23 9:50 PM
     * @param: key - [K]
     * @param: oldValue - [V]
     * @param: newValue - [V]
     */
    default boolean replace(K key, V oldValue, V newValue) {
        return true;
    }

    /**
     * description: 当key存在时，才替换
     *
     * @return V
     * @Author sovava
     * @Date 12/18/23 9:52 PM
     * @param: key - [K]
     * @param: value - [V]
     */
    default V replace(K key, V value) {
        return value;
    }

    /**
     * description: 如果map中不存在给定的key，那么就用给定的方法计算并插入map中
     *
     * @return V
     * @Author sovava
     * @Date 12/18/23 9:56 PM
     * @param: key - [K]
     * @param: mappingFunction - [com.sovava.vacollection.api.function.VaFunction<?superK]
     */
    default V computeIfAbsent(K key, VaFunction<? super K, ? extends V> mappingFunction) {
        return null;
    }

    /**
     * description: 如果给定的key在map中且值非空，就计算
     *
     * @return V
     * @Author sovava
     * @Date 12/18/23 9:59 PM
     * @param: key - [K]
     * @param: remappingFunction - [com.sovava.vacollection.api.function.VaBiFunction<?superK]
     */
    default V computeIfPresent(K key, VaBiFunction<? super K, ? extends V, ? extends V> remappingFunction) {
        return null;
    }

    /**
     * description: 为指定key计算新值，
     *
     * @return V
     * @Author sovava
     * @Date 12/18/23 10:07 PM
     * @param: key - [K]
     * @param: remappingFunction - [com.sovava.vacollection.api.function.VaBiFunction<?superK]
     */
    default V compute(K key, VaBiFunction<? super K, ? extends V, ? extends V> remappingFunction) {
        return null;
    }

    /**
     * description:
     * 如果指定的键尚未与值关联或与 null 关联，则将其与给定的非 null 值关联。
     * 如果指定的key已经存在，用给定重新映射函数的结果替换关联值，如果结果为null则删除
     *
     * @return V
     * @Author sovava
     * @Date 12/18/23 10:11 PM
     * @param: key - [K]
     * @param: value - [V]
     * @param: remappingFunction - [com.sovava.vacollection.api.function.VaBiFunction<?superK]
     */
    default V merge(K key, V value, VaBiFunction<? super K, ? extends V, ? extends V> remappingFunction) {
        return null;
    }


    interface VaEntry<K, V> {
        K getKey();

        V getValue();

        V setValue(V value);

        boolean equals(Object o);

        int hashCode();
    }
}
