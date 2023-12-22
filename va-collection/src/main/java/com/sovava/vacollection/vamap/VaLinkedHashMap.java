package com.sovava.vacollection.vamap;

import com.sovava.vacollection.api.VaMap;

/**
 * Description: 维护一个贯穿其所有条目的双向链表
 *
 * @author: ykn
 * @date: 2023年12月22日 9:40 PM
 **/
public class VaLinkedHashMap<K, V> extends VaHashMap<K, V> implements VaMap<K, V> {

    static class VaEntry<K, V> extends VaHashMap.VaNode<K, V> {
        VaEntry<K, V> before, after;

        VaEntry(int hash, K key, V value, VaNode<K, V> next) {
            super(hash, key, value, next);
        }
    }
}
