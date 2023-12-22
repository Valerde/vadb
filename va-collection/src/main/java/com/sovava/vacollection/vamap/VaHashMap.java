package com.sovava.vacollection.vamap;

import com.sovava.vacollection.api.VaMap;
import com.sovava.vacollection.api.VaSet;


import java.io.Serializable;
import java.util.Objects;

/**
 * Description:
 *
 * @author: ykn
 * @date: 2023年12月22日 5:11 PM
 **/
public class VaHashMap<K, V> extends VaAbstractMap<K, V> implements VaMap<K, V>, Cloneable, Serializable {
    private static final long serialVersionUID = 1214817153348964519L;

    /**
     * 默认初始容量
     */
    static final int DEFAULT_INITIAL_CAPACITY = 16;

    /**
     * 限定最大容量
     */
    static final int MAXIMUM_CAPACITY = 1 << 30;

    /**
     * 默认负载因子
     */
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

    /**
     * list转tree阈值
     */
    static final int DEFAULT_LIST_TO_TREE_THRESHOLD = 8;

    /**
     * tree转list阈值
     */
    static final int DEFAULT_TREE_TO_LIST_THRESHOLD = 6;

    static final int MIN_TREEIFY_CAPACITY = 64;


    VaNode<K, V>[] table;
    VaSet<VaEntry<K, V>> entrySet;
    int size;
    int modCount;
    /**
     * 临界点，要调整的下一个size（capacity*loadFactor)
     */
    int threshold;

    float loadFactor;

    public VaHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException("不合理的初始容量");

        }
        if (initialCapacity > MAXIMUM_CAPACITY) initialCapacity = MAXIMUM_CAPACITY;
        if (loadFactor <= 0 || Float.isNaN(loadFactor)) {
            throw new IllegalArgumentException("不合理的负载因子");
        }
        this.loadFactor = loadFactor;
        this.threshold = getReshapedCap(initialCapacity);
    }

    public VaHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public VaHashMap() {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
    }

    /**
     * description: 计算hash值，该设计的说明在
     * <p>https://blog.csdn.net/yueaini10000/article/details/108869022
     * <p>该设计的证明在singleton-test中相应位置TestForHashMapHashFunc类中证明
     *
     * @return int
     * @Author sovava
     * @Date 12/22/23 6:40 PM
     * @param: key - [java.lang.Object]
     */
    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

    static final int getReshapedCap(int cap) {
        int n = cap - 1;
        n |= n >>> 1;
        n |= n >>> 2;
        n |= n >>> 4;
        n |= n >>> 8;
        n |= n >>> 16;
        return n >= MAXIMUM_CAPACITY ? MAXIMUM_CAPACITY : n + 1;
    }


    static class VaNode<K, V> implements VaMap.VaEntry<K, V> {
        private int hash;
        final K key;
        V value;
        VaNode<K, V> next;

        VaNode(int hash, K key, V value, VaNode<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return value;
        }

        @Override
        public V setValue(V value) {
            V oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        @Override
        public String toString() {
            return "VaNode: " + key + " = " + value;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) ^ Objects.hashCode(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (obj instanceof VaMap.VaEntry) {
                VaEntry<?, ?> e = (VaEntry<?, ?>) obj;
                return Objects.equals(key, e.getKey()) && Objects.equals(value, e.getValue());
            }
            return false;
        }
    }
}
