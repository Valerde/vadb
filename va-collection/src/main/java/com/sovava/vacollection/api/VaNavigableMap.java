package com.sovava.vacollection.api;


/**
 * description: navigable - 可导航的，意思就是说这个接口可以通过指定key找到某条件的元素
 *
 * @Author sovava
 * @Date 12/18/23 11:12 PM
 */
public interface VaNavigableMap<K, V> extends VaSortedMap<K, V> {

    /**
     * description: 返回严格小于给定key的最大key的key-value entry,不存在返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:15 PM
     * @param: key - [K]
     */
    VaEntry<K, V> lowerEntry(K key);

    /**
     * description: 返回严格小于指定key的最大key，不存在返回null
     *
     * @return K
     * @Author sovava
     * @Date 12/18/23 11:17 PM
     * @param: key - [K]
     */
    K lowerKey(K key);

    /**
     * description: 返回小于或等于指定key的最大key-value，不存在返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:18 PM
     * @param: key - [K]
     */
    VaEntry<K, V> floorEntry(K key);

    /**
     * description: 返回小于或等于指定key的最大key，不存在返回null
     *
     * @return K
     * @Author sovava
     * @Date 12/18/23 11:19 PM
     * @param: key - [K]
     */
    K floorKey(K key);

    /**
     * description: 返回大于或等于指定key的最大key-value，不存在返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:20 PM
     * @param: key - [K]
     */
    VaEntry<K, V> ceilingEntry(K key);

    /**
     * description: 返回大于或等于指定key的最大key，不存在返回null
     *
     * @return K
     * @Author sovava
     * @Date 12/18/23 11:21 PM
     * @param: key - [K]
     */
    K ceilingKey(K key);

    /**
     * description: 返回严格大于给定key的最大key的key-value entry,不存在返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:22 PM
     * @param: key - [K]
     */
    VaEntry<K, V> higherEntry(K key);

    /**
     * description: 返回严格大于给定key的最大key的key,不存在返回null
     *
     * @return K
     * @Author sovava
     * @Date 12/18/23 11:23 PM
     * @param: key - [K]
     */
    K higherKey(K key);

    /**
     * description: 返回最小的k-v，如果map为空，则返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:24 PM
     */
    VaEntry<K, V> firstEntry();

    /**
     * description: 返回最大的k-v，如果map为空，则返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:25 PM
     * @param:
     */
    VaEntry<K, V> lastEntry();

    /**
     * description: 返回并删除最小的k-v，如果map为空，则返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:26 PM
     */
    VaEntry<K, V> pollFirstEntry();

    /**
     * description: 返回并删除最大的k-v，如果map为空，则返回null
     *
     * @return VaEntry<K, V>
     * @Author sovava
     * @Date 12/18/23 11:27 PM
     */
    VaEntry<K, V> pollLastEntry();

    /**
     * description: 返回逆序Map
     *
     * @return VaNavigableMap<K, V>
     * @Author sovava
     * @Date 12/18/23 11:28 PM
     */
    VaNavigableMap<K, V> descendingMap();

    /**
     * description: 将map的所有key转为类似的VaNavigableSet
     *
     * @return VaNavigableSet<K>
     * @Author sovava
     * @Date 12/18/23 11:30 PM
     */
    VaNavigableSet<K> navigableKeySet();

    /**
     * description: 将map的所有key转为 逆序的 类似的VaNavigableSet
     *
     * @return VaNavigableSet<K>
     * @Author sovava
     * @Date 12/18/23 11:31 PM
     */
    VaNavigableSet<K> descendingKeySet();

    /**
     * description:
     *
     * @return VaNavigableMap<K, V>
     * @Author sovava
     * @Date 12/18/23 11:33 PM
     * @param: fromKey - [K]
     * @param: fromInclusive - [boolean] 左区间是否闭
     * @param: toKey - [K]
     * @param: toInclusive - [boolean] 右区间是否闭
     */
    VaNavigableMap<K, V> subMap(K fromKey, boolean fromInclusive,
                                K toKey, boolean toInclusive);

    /**
     * description:
     *
     * @return VaNavigableMap<K, V>
     * @Author sovava
     * @Date 12/18/23 11:35 PM
     * @param: toKey - [K]
     * @param: inclusive - [boolean] 右区间是否闭
     */
    VaNavigableMap<K, V> headMap(K toKey, boolean inclusive);

    /**
     * description:
     *
     * @return VaNavigableMap<K, V>
     * @Author sovava
     * @Date 12/18/23 11:35 PM
     * @param: fromKey - [K]
     * @param: inclusive - [boolean] 左区间是否闭
     */
    VaNavigableMap<K, V> tailMap(K fromKey, boolean inclusive);

}
