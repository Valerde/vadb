package com.sovava.vacollection.api;

/**
 * description: 按key进行排序
 *
 * @Author sovava
 * @Date 12/18/23 10:35 PM
 */
public interface VaSortedMap <K,V> extends VaMap<K,V> {

    /**
     * description: 返回用于对map中的key进行排序的comparator，如果是按照自然排序，则返回null
     *
     * @Author sovava
     * @Date 12/18/23 10:39 PM
     * @return VaComparator<?superK>
     */
    VaComparator<? super K> comparator();

    /**
     * description: 返回key满足： fromKey <= key < toKey 的子Map，区间左闭右开
     *
     * @Author sovava
     * @Date 12/18/23 10:42 PM
     * @param: fromKey - [K]
     * @param: toKey - [V]
     * @return VaSortedMap<K, V>
     */
    VaSortedMap<K,V> subMap(K fromKey,V toKey);

    /**
     * description: 返回key满足：  key < toKey 的子Map，不包含toKey
     *
     * @Author sovava
     * @Date 12/18/23 10:44 PM
     * @param: toKey - [K]
     * @return VaSortedMap<K, V>
     */
    VaSortedMap<K,V> headMap(K toKey);


    /**
     * description: 返回key满足： fromKey <= key  的子Map，区间左闭
     *
     * @Author sovava
     * @Date 12/18/23 10:45 PM
     * @param: fromKey - [K]
     * @return VaSortedMap<K, V>
     */
    VaSortedMap<K,V> tailMap(K fromKey);


    /**
     * description: 返回最小的key
     *
     * @Author sovava
     * @Date 12/18/23 10:46 PM
     * @return K
     */
    K firstKey();

    /**
     * description: 返回最大的key
     *
     * @Author sovava
     * @Date 12/18/23 10:47 PM
     * @return K
     */
    K lastKey();

    @Override
    VaSet<VaEntry<K, V>> entrySet();
}
