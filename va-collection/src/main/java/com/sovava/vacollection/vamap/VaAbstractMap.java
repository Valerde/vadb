package com.sovava.vacollection.vamap;

import com.sovava.vacollection.api.VaCollection;
import com.sovava.vacollection.api.VaIterator;
import com.sovava.vacollection.api.VaMap;
import com.sovava.vacollection.api.VaSet;
import com.sovava.vacollection.collections.VaAbstractCollection;
import com.sovava.vacollection.vaset.VaAbstractSet;


/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2023年12月21日 10:46 PM
 **/
public abstract class VaAbstractMap<K, V> implements VaMap<K, V> {

    transient VaSet<K> keySet;
    transient VaCollection<V> values;

    protected VaAbstractMap() {
    }

    @Override
    public int size() {
        return entrySet().size();
    }

    @Override
    public boolean isEmpty() {
        return entrySet().isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        VaIterator<VaEntry<K, V>> vit = entrySet().vaIterator();
        if (key == null) {
            while (vit.hasNext()) {
                if (vit.next().getKey() == null) {
                    return true;
                }
            }
        } else {
            while (vit.hasNext()) {
                if (key.equals(vit.next().getKey())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean containValue(Object value) {
        VaIterator<VaEntry<K, V>> vit = entrySet().vaIterator();
        if (value == null) {
            while (vit.hasNext()) {
                if (vit.next().getValue() == null) {
                    return true;
                }
            }
        } else {
            while (vit.hasNext()) {
                if (value.equals(vit.next().getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public V get(Object key) {
        VaIterator<VaEntry<K, V>> vit = entrySet().vaIterator();
        if (key == null) {
            while (vit.hasNext()) {
                VaEntry<K, V> entry = vit.next();
                if (entry.getKey() == null) {
                    return entry.getValue();
                }
            }
        } else {
            while (vit.hasNext()) {
                VaEntry<K, V> entry = vit.next();
                if (key.equals(entry.getKey())) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        throw new UnsupportedOperationException("该map不允许新增元素");
    }

    @Override
    public V remove(Object key) {
        VaIterator<VaEntry<K, V>> vit = entrySet().vaIterator();
        VaEntry<K, V> removedEntry = null;
        if (key == null) {
            while (vit.hasNext()) {
                VaEntry<K, V> entry = vit.next();
                if (entry.getKey() == null) {
                    removedEntry = entry;
                    break;
                }
            }
        } else {
            while (vit.hasNext()) {
                VaEntry<K, V> entry = vit.next();
                if (key.equals(entry.getKey())) {
                    removedEntry = entry;
                    break;
                }
            }
        }

        V removedValue = null;
        if (removedEntry != null) {
            removedValue = removedEntry.getValue();
            vit.remove();
        }
        return removedValue;
    }

    @Override
    public void putAll(VaMap<? extends K, ? extends V> m) {
        for (VaEntry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        entrySet().clear();
    }

    @Override
    public VaSet<K> keySet() {
        VaSet<K> ks = keySet;
        if (ks == null) {
            ks = new VaAbstractSet<K>() {
                @Override
                public VaIterator<K> vaIterator() {
                    return new VaIterator<K>() {

                        private VaIterator<VaEntry<K, V>> i = entrySet().vaIterator();

                        @Override
                        public boolean hasNext() {
                            return i.hasNext();
                        }

                        @Override
                        public K next() {
                            return i.next().getKey();
                        }

                        @Override
                        public void remove() {
                            i.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return VaAbstractMap.this.size();
                }

                @Override
                public boolean isEmpty() {
                    return VaAbstractMap.this.isEmpty();
                }

                @Override
                public void clear() {
                    VaAbstractMap.this.clear();
                }

                @Override
                public boolean contains(Object o) {
                    return VaAbstractMap.this.containsKey(o);
                }
            };
            keySet = ks;
        }

        return ks;
    }

    @Override
    public VaCollection<V> values() {
        VaCollection<V> vals = values;

        if (vals == null) {
            vals = new VaAbstractCollection<V>() {
                @Override
                public VaIterator<V> vaIterator() {
                    return new VaIterator<V>() {
                        private VaIterator<VaEntry<K, V>> vit = entrySet().vaIterator();

                        @Override
                        public boolean hasNext() {
                            return vit.hasNext();
                        }

                        @Override
                        public V next() {
                            return vit.next().getValue();
                        }

                        @Override
                        public void remove() {
                            vit.remove();
                        }
                    };
                }

                @Override
                public int size() {
                    return VaAbstractMap.this.size();
                }

                @Override
                public boolean contains(Object o) {
                    return VaAbstractMap.this.containValue(o);
                }

                @Override
                public boolean isEmpty() {
                    return VaAbstractMap.this.isEmpty();
                }

                @Override
                public void clear() {
                    VaAbstractMap.this.clear();
                }


            };
            values = vals;
        }
        return vals;
    }

    @Override
    public abstract VaSet<VaEntry<K, V>> entrySet();
}
