package com.sovava.vacollection.vamap;

import com.sovava.vacollection.api.VaConsumer;
import com.sovava.vacollection.api.VaIterator;
import com.sovava.vacollection.api.VaMap;
import com.sovava.vacollection.api.VaSet;
import com.sovava.vacollection.vaset.VaAbstractSet;


import java.io.Serializable;
import java.util.Objects;
import java.util.function.Consumer;

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

    public VaHashMap(VaMap<? extends K, ? extends V> map) {
        this.loadFactor = DEFAULT_LOAD_FACTOR;
        putMapEntries(map);
    }

    void putMapEntries(VaMap<? extends K, ? extends V> map) {
        int size = map.size();
        if (size > 0) {
            if (table == null) {
                float ft = (float) size / loadFactor;
                int cap = ft < (float) MAXIMUM_CAPACITY ? (int) ft : MAXIMUM_CAPACITY;
                if (cap > threshold) {
                    threshold = getReshapedCap(cap);
                }
            } else if (size > threshold) {
                //resize
            }
            for (VaEntry<? extends K, ? extends V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                //putVal
            }
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }


    public V get(Object key) {
        VaNode<K, V> node;
        node = getNode(hash(key), key);
        return node == null ? null : node.value;
    }

    /**
     * description: 根据提供的hash值和key找到Node
     *
     * @return VaNode<K, V>
     * @Author sovava
     * @Date 12/23/23 8:13 PM
     * @param: hash - [int]
     * @param: key - [java.lang.Object]
     */
    VaNode<K, V> getNode(int hash, Object key) {
        VaNode<K, V>[] tab = table;

        int n = tab.length;
        VaNode<K, V> first = tab[(n - 1) & hash];
        K k;
        if (tab != null && n > 0 && first != null) {
            k = first.key;
            if (first.hash == hash && (Objects.equals(key, k))) {
                return first;
            }
            if (first.next != null) {
                if (first instanceof VaTreeNode) {
                    //TODO 树化查找
                }
                first = first.next;
                do {
                    k = first.key;
                    if (first.hash == hash && Objects.equals(key, k)) return first;
                } while ((first = first.next) != null);
            }
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return getNode(hash(key), key) != null;
    }

    @Override
    public V put(K key, V value) {
        return putValue(hash(key), key, value, false);
    }

    public V putValue(int hash, K key, V value, boolean onlyIfAbsent) {
        VaNode<K, V>[] tab = table;
        VaNode<K, V> node;
        int n = tab.length, i;
        if (tab == null || n == 0) {
            //懒分配容量，即new对象的时候不分配容量，第一次put数据的时候才分配容量
        }
        i = (n - 1) & hash;
        node = tab[i];
        if (node == null) {
            //这个bin的头结点为null，直接设置即可
            tab[i] = newNode(hash, key, value, null);
        } else {
            //从头开始遍历后续节点
            VaNode<K, V> e = null;
            K k = node.key;
            if (node.hash == hash && Objects.equals(k, key)) {
                e = node;
            } else if (node instanceof VaTreeNode) {
                //TODO 红黑树插入
            } else {
                for (int binCount = 1; ; binCount++) {
                    e = node.next;

                    if (e == null) {
                        e = newNode(hash, key, value, null);
                        if (binCount >= DEFAULT_LIST_TO_TREE_THRESHOLD) {
                            //TODO 树化
                            int tmp;
                        }
                        break;
                    }
                    k = e.key;
                    if (e.hash == hash && Objects.equals(k, key)) {
                        break;
                    }
                    node = e;
                }

            }
            if (e != null) {
                V oldV = e.value;
                if (!onlyIfAbsent || oldV == null) {
                    e.value = value;
                }
                return oldV;
            }
        }

        modCount++;
        if (++size > threshold) {
            //重分配
        }
        return null;
    }

    VaNode<K, V> newNode(int hash, K key, V value, VaNode<K, V> next) {
        return new VaNode<K, V>(hash, key, value, next);
    }

    VaNode<K, V> resize() {

    }

    @Override
    public void putAll(VaMap<? extends K, ? extends V> m) {
        putMapEntries(m);
    }

    @Override
    public V remove(Object key) {
        VaNode<K, V> node;
        node = removeNode(hash(key), key, null, false, true);
        return node == null ? null : node.value;
    }

    VaNode<K, V> removeNode(int hash, Object key, V value, boolean removeIfMatch, boolean movable) {
        VaNode<K, V>[] tab = table;
        int n = tab.length;
        VaNode<K, V> q;
        if (tab != null && n != 0 && (q = tab[(n - 1) & hash]) != null) {
            VaNode<K, V> matchNode = null, tmpNode;
            K k = q.key;
            if (q.hash == hash && Objects.equals(k, key)) {
                matchNode = q;
            } else if (q instanceof VaTreeNode) {
                //TODO remove tree
            } else {
                tmpNode = q.next;
                while (tmpNode != null) {
                    k = tmpNode.key;
                    if (tmpNode.hash == hash && Objects.equals(k, key)) {
                        matchNode = tmpNode;
                        break;
                    }
                    q = tmpNode;
                    tmpNode = tmpNode.next;

                }
            }
            V v;
            if (matchNode != null && (!removeIfMatch || (v = matchNode.value) == value || (value != null && value.equals(v)))) {
                if (matchNode instanceof VaTreeNode) {
                    //TODO tree remove
                } else if (matchNode == q) {
                    tab[(n - 1) & hash] = q.next;
                } else {
                    q.next = matchNode.next;
                }
                ++modCount;
                --size;
                return matchNode;
            }
        }

        return null;
    }

    @Override
    public void clear() {
        VaNode<K, V>[] tab = table;
        modCount++;
        if (tab != null && size != 0) {
            size = 0;
            for (int i = 0; i < tab.length; i++) {
                tab[i] = null;
            }
        }
    }

    @Override
    public boolean containValue(Object value) {
        VaNode<K, V>[] tab = table;
        int n = tab.length;
        if (tab != null && n != 0) {
            for (VaNode<K, V> kvVaNode : tab) {
                for (VaNode<K, V> node = kvVaNode; node != null; node = node.next) {
                    if (Objects.equals(node.value, value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public VaSet<K> keySet() {
        VaSet<K> ks = keySet;
        if (ks == null) {
            ks = new KeySet();
            keySet = ks;
        }
        return ks;
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

    static class VaTreeNode<K, V> extends VaLinkedHashMap.VaEntry<K, V> {

        VaTreeNode(int hash, K key, V value, VaNode<K, V> next) {
            super(hash, key, value, next);
        }
    }

    class KeySet extends VaAbstractSet<K> {
        public VaIterator<K> vaIterator() {
            //TODO keyIterator
            return null;
        }

        public int size() {
            return size;
        }

        public boolean contains(Object o) {
            return VaHashMap.this.containsKey(0);
        }

        public void clear() {
            VaHashMap.this.clear();
        }

        public boolean remove(Object o) {
            return VaHashMap.this.removeNode(hash(o), o, null, false, true) != null;
        }

        public void forEach(VaConsumer<? super K> action) {
            Objects.requireNonNull(action);
            VaNode<K, V>[] tab = table;
            if (size > 0 && tab != null) {
                for (VaNode<K, V> kvVaNode : tab) {
                    for (VaNode<K, V> node = kvVaNode; node != null; node = node.next) {
                        action.accept(node.key);
                    }
                }
            }
        }
    }
}
