package com.sovava.vacollection.vamap;

import com.sovava.vacollection.api.*;
import com.sovava.vacollection.api.function.VaBiConsumer;
import com.sovava.vacollection.api.function.VaBiFunction;
import com.sovava.vacollection.api.function.VaFunction;
import com.sovava.vacollection.collections.VaAbstractCollection;
import com.sovava.vacollection.vaset.VaAbstractSet;


import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Description:
 * 考虑到后续不会应用于不安全的场景，因此不设置快速失败
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
                resize();
            }
            for (VaEntry<? extends K, ? extends V> entry : map.entrySet()) {
                K key = entry.getKey();
                V value = entry.getValue();
                putValue(hash(key), key, value, false);
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
        VaNode<K, V>[] tab;
        VaNode<K, V> node;
        int n, i;
        if ((tab = table) == null || (n = tab.length) == 0) {
            tab = resize();
            n = tab.length;
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
            resize();
        }
        return null;
    }

    VaNode<K, V> newNode(int hash, K key, V value, VaNode<K, V> next) {
        return new VaNode<K, V>(hash, key, value, next);
    }

    @SuppressWarnings("unchecked")
    VaNode<K, V>[] resize() {
        VaNode<K, V>[] oldTab = table;
        int oldCap = oldTab == null ? 0 : oldTab.length;
        int oldThr = threshold;
        int newCap = 0, newThr = 0;
        if (oldCap > 0) {
            if (oldCap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return oldTab;
            } else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap > DEFAULT_INITIAL_CAPACITY) {
                newThr = oldThr << 1;
            }
        } else if (oldThr > 0) {
            newCap = oldThr;
        } else {
            newCap = DEFAULT_INITIAL_CAPACITY;
            newThr = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
        }

        if (newThr == 0) {
            float thr = newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && thr < (float) MAXIMUM_CAPACITY) ? (int) thr : Integer.MAX_VALUE;
        }

        VaNode<K, V>[] newTab = (VaNode<K, V>[]) new VaNode[newCap];
        threshold = newThr;
        table = newTab;
        if (oldTab != null) {
            for (int i = 0; i < oldCap; i++) {
                VaNode<K, V> head = oldTab[i];
                if (head != null) {
                    oldTab[i] = null;
                    if (head.next == null) {
                        newTab[(newCap - 1) & head.hash] = head;
                    } else if (head instanceof VaTreeNode) {
                        //树扩容
                    } else {
                        VaNode<K, V> lHead = null;
                        VaNode<K, V> hHead = null;
                        VaNode<K, V> lTail = null;
                        VaNode<K, V> hTail = null;
                        VaNode<K, V> next = null;
                        do {
                            next = head.next;
                            if (((newCap - 1) & head.hash) == 0) {
                                if (lTail == null) {
                                    lHead = head;
                                } else {
                                    lTail.next = head;
                                }
                                lTail = head;
                            } else {
                                if (hTail == null) {
                                    hHead = head;
                                } else {
                                    hTail.next = head;
                                }
                                hTail = head;
                            }
                        } while ((head = next) != null);
                        if (lTail != null) {
                            lTail.next = null;
                            newTab[i] = lHead;
                        }
                        if (hTail != null) {
                            hTail.next = null;
                            newTab[i + oldCap] = hHead;
                        }

                    }

                }

            }
        }
        return newTab;
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

    VaNode<K, V> removeNode(int hash, Object key, Object value, boolean removeIfMatch, boolean movable) {
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
            ks = new VaKeySet();
            keySet = ks;
        }
        return ks;
    }


    @Override
    public VaCollection<V> values() {
        VaCollection<V> vs = values;
        if (vs == null) {
            vs = new VaValues();
            values = vs;
        }
        return vs;
    }

    @Override
    public VaSet<VaEntry<K, V>> entrySet() {
        VaSet<VaEntry<K, V>> es = entrySet;
        if (es == null) {
            es = new VaEntrySet();
            entrySet = es;
        }
        return es;
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


    @Override
    public V getOrDefault(Object key, V defaultValue) {
        VaNode<K, V> res = getNode(hash(key), key);
        return res == null ? defaultValue : res.value;
    }

    @Override
    public V putIfAbsent(K key, V value) {
        return putValue(hash(key), key, value, true);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return removeNode(hash(key), key, value, true, true) != null;
    }

    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        VaNode<K, V> node = getNode(hash(key), key);
        if (node != null && Objects.equals(node.value, oldValue)) {
            node.value = newValue;
            return true;
        }
        return false;
    }

    @Override
    public V replace(K key, V value) {
        VaNode<K, V> node = getNode(hash(key), key);
        if (node != null) {
            V oldV = node.value;
            node.value = value;
            return oldV;
        }
        return null;
    }

    /**
     * description: 如果map中不存在给定的key，那么就用给定的方法计算并插入map中
     *
     * @param key
     * @param mappingFunction
     * @return V
     * @Author sovava
     * @Date 12/24/23 1:26 PM
     * @param: key - [K]
     * @param: mappingFunction - [com.sovava.vacollection.api.function.VaFunction<?superK]
     */
    @Override
    public V computeIfAbsent(K key, VaFunction<? super K, ? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        VaNode<K, V>[] tab;
        int n;
        int hash = hash(key);
        VaNode<K, V> head, oldNode = null;
        VaTreeNode<K, V> root = null;
        if ((tab = table) == null || size > threshold || (n = tab.length) == 0) {
            tab = resize();
            n = tab.length;
        }
        if ((head = tab[(n - 1) & hash]) != null) {//寻找旧value
            if (head instanceof VaTreeNode) {
                //TODO 树化
            } else {
                VaNode<K, V> prev = head;
                do {
                    if (prev.hash == hash && (Objects.equals(key, prev.key))) {
                        oldNode = prev;
                        break;
                    }
                } while ((prev = prev.next) != null);
            }
            if (oldNode != null && oldNode.value != null) {
                return oldNode.value;
            }
        }
        V newV = mappingFunction.apply(key);
        if (newV == null) {
            return null;
        } else if (oldNode != null) {
            oldNode.value = newV;
            return newV;
        } else if (root != null) {
            //TODO 插入树中
        } else {
            //新建头结点
            tab[(n - 1) & hash] = newNode(hash, key, newV, null);
            //TODO 判断是否树化
        }
        ++modCount;
        ++size;
        return newV;
    }

    /**
     * description: 如果给定的key在map中且值非空，就计算
     *
     * @param key
     * @param remappingFunction
     * @return V
     * @Author sovava
     * @Date 12/24/23 1:26 PM
     * @param: key - [K]
     * @param: remappingFunction - [com.sovava.vacollection.api.function.VaBiFunction<?superK]
     */
    @Override
    public V computeIfPresent(K key, VaBiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        VaNode<K, V> node;
        V oldV;
        int hash = hash(key);
        if ((node = getNode(hash, key)) != null && (oldV = node.value) != null) {
            V newV = remappingFunction.apply(key, oldV);
            if (newV != null) {
                node.value = newV;
                return newV;
            } else {
                removeNode(hash, key, null, false, true);
            }
        }
        return null;
    }

    /**
     * description: 为指定key计算新值，
     *
     * @param key
     * @param remappingFunction
     * @return V
     * @Author sovava
     * @Date 12/24/23 1:26 PM
     * @param: key - [K]
     * @param: remappingFunction - [com.sovava.vacollection.api.function.VaBiFunction<? super K>]
     */
    @Override
    public V compute(K key, VaBiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int hash = hash(key);
        VaNode<K, V>[] tab;
        VaNode<K, V> head, oldN = null;
        int n;
        VaTreeNode<K, V> root = null;
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0) {
            tab = resize();
            n = tab.length;
        }
        if ((head = tab[(n - 1) & hash]) != null) {
            if (head instanceof VaTreeNode) {
                //树查找
            } else {
                VaNode<K, V> prev = head;
                do {
                    if (prev.hash == hash && Objects.equals(prev.key, key)) {
                        oldN = prev;
                        break;
                    }
                } while ((prev = prev.next) != null);
            }
        }
        V oldV = oldN == null ? null : oldN.value;
        V newV = remappingFunction.apply(key, oldV);
        if (oldN != null) {
            if (newV != null) {
                oldN.value = newV;
            } else {
                removeNode(hash, key, null, false, true);
            }
        } else if (newV != null) {
            if (root != null) {
                //树插入
            } else {
                tab[(n - 1) & hash] = newNode(hash, key, newV, head);
                //TODO 判断树化
            }
            ++modCount;
            ++size;
        }
        return newV;
    }


    public void forEach(VaBiConsumer<? super K, ? super V> action) {
        Objects.requireNonNull(action);
        VaNode<K, V>[] tab = table;
        if (size > 0 && tab != null) {
            for (int i = 0; i < tab.length; i++) {
                for (VaNode<K, V> node = tab[i]; node != null; node = node.next) {
                    action.apply(node.key, node.value);
                }
            }
        }
    }


    public void replaceAll(VaBiFunction<? super K, ? super V, ? extends V> action) {
        Objects.requireNonNull(action);
        VaNode<K, V>[] tab = table;
        if (size > 0 && tab != null) {
            for (int i = 0; i < tab.length; i++) {
                for (VaNode<K, V> node = tab[i]; node != null; node = node.next) {
                    node.value = action.apply(node.key, node.value);
                }
            }
        }
    }

    /**
     * description:
     * 如果指定的键尚未与值关联或与 null 关联，则将其与给定的非 null 值关联。
     * 如果指定的key已经存在，用给定重新映射函数的结果替换关联值，如果结果为null则删除
     *
     * @param key
     * @param value
     * @param remappingFunction
     * @return V
     * @Author sovava
     * @Date 12/18/23 10:11 PM
     * @param: key - [K]
     * @param: value - [V]
     * @param: remappingFunction - [com.sovava.vacollection.api.function.VaBiFunction<?superK]
     */
    @Override
    public V merge(K key, V value, VaBiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(value);
        Objects.requireNonNull(remappingFunction);
        int hash = hash(key);
        VaNode<K, V>[] tab;
        VaNode<K, V> head, oldN = null;
        int n;
        VaTreeNode<K, V> root = null;
        if (size > threshold || (tab = table) == null || (n = tab.length) == 0) {
            tab = resize();
            n = tab.length;
        }
        if ((head = tab[(n - 1) & hash]) != null) {
            if (head instanceof VaTreeNode) {
                //树查找
            } else {
                VaNode<K, V> prev = head;
                do {
                    if (prev.hash == hash && Objects.equals(prev.key, key)) {
                        oldN = prev;
                        break;
                    }
                } while ((prev = prev.next) != null);
            }
        }
        if (oldN != null) {
            V newV;
            if (oldN.value != null) {
                newV = remappingFunction.apply(oldN.value, value);
            } else {
                newV = value;
            }
            if (newV != null) {
                oldN.value = newV;
            } else {
                removeNode(hash, key, null, false, true);
            }
            return newV;
        } else if (value != null) {
            if (root != null) {
                //插入树
            } else {
                tab[(n - 1) & hash] = newNode(hash, key, value, head);
                //TODO 判断树化
            }
            ++modCount;
            ++size;
            return value;
        }
        return null;
    }


    @Override
    @SuppressWarnings("unchecked")
    protected Object clone() throws CloneNotSupportedException {
        VaHashMap<K, V> clone;
        clone = (VaHashMap<K, V>) super.clone();

        clone.reinit();
        clone.putMapEntries(this);
        return clone;
    }

    private void reinit() {
        table = null;
        entrySet = null;
        size = 0;
        modCount = 0;
        threshold = 0;
        values = null;
        keySet = null;
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

    class VaKeySet extends VaAbstractSet<K> {
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

    class VaValues extends VaAbstractCollection<V> {

        public VaIterator<V> vaIterator() {
            //TODO 
            return null;
        }

        public int size() {
            return size;
        }

        public void clear() {
            VaHashMap.this.clear();
        }

        public boolean contains(Object o) {
            return containValue(o);
        }

        public void forEach(VaConsumer<? super V> action) {
            Objects.requireNonNull(action);
            VaNode<K, V>[] tab = table;
            if (size > 0 && tab != null) {
                for (VaNode<K, V> kvVaNode : tab) {
                    for (VaNode<K, V> node = kvVaNode; node != null; node = node.next) {
                        action.accept(node.value);
                    }
                }
            }
        }
    }

    class VaEntrySet extends VaAbstractSet<VaMap.VaEntry<K, V>> {


        public VaIterator<VaEntry<K, V>> vaIterator() {
            return null;
        }

        public int size() {
            return size;
        }

        public void forEach(VaConsumer<? super VaEntry<K, V>> action) {
            Objects.requireNonNull(action);
            VaNode<K, V>[] tab = table;
            if (size > 0 && tab != null) {
                for (VaNode<K, V> kvVaNode : tab) {
                    for (VaNode<K, V> node = kvVaNode; node != null; node = node.next) {
                        action.accept(node);
                    }
                }
            }
        }

        public boolean contains(Object o) {
            if (!(o instanceof VaMap.VaEntry)) {
                return false;
            }

            VaEntry<?, ?> entry = (VaEntry<?, ?>) o;
            VaNode<K, V> res = getNode(hash(entry.getKey()), entry.getKey());
            return res != null && res.equals(entry);
        }

        public void clear() {
            VaHashMap.this.clear();
        }

        public boolean remove(Object o) {
            if (!(o instanceof VaMap.VaEntry)) {
                return false;
            }

            VaEntry<?, ?> entry = (VaEntry<?, ?>) o;
            return removeNode(hash(entry.getKey()), entry.getKey(), entry.getValue(), true, true) != null;
        }
    }

    abstract class HashIterator {
        VaNode<K, V> next;
        VaNode<K, V> current;
        int index;

        HashIterator() {
            VaNode<K, V>[] t = table;
            current = next = null;
            index = 0;
            if (t != null && size > 0) {
                while (index < t.length && (next = t[index++]) == null) ;
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final VaNode<K, V> nextNode() {
            VaNode<K, V>[] tab = table;
            VaNode<K, V> e = next;
            if (e == null) {
                throw new NoSuchElementException();
            }
            if ((next = (current = e).next) == null && (tab = table) != null) {
                while (index < tab.length && (next = tab[index++]) == null) ;
            }
            return e;
        }

        public final void remove() {
            VaNode<K, V> p = current;
            if (p == null) {
                throw new IllegalStateException();
            }
            current = null;
            K key = p.key;
            removeNode(hash(key), key, null, false, false);
        }

    }

    final class KeyIterator extends HashIterator implements VaIterator<K> {
        public K next() {
            return nextNode().key;
        }
    }

    final class ValueIterator extends HashIterator implements VaIterator<V> {
        public V next() {
            return nextNode().value;
        }
    }

    final class EntryIterator extends HashIterator implements VaIterator<VaMap.VaEntry<K, V>> {
        public VaEntry<K, V> next() {
            return nextNode();
        }
    }
}
