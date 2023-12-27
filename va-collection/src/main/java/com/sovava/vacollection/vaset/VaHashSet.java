package com.sovava.vacollection.vaset;

import com.sovava.vacollection.api.VaCollection;
import com.sovava.vacollection.api.VaIterator;
import com.sovava.vacollection.api.VaSet;
import com.sovava.vacollection.vamap.VaHashMap;

import java.io.Serializable;

/**
 * Description: 基于hashmap的hashset
 *
 * @author: ykn
 * @date: 2023年12月27日 3:06 PM
 **/
public class VaHashSet<E> extends VaAbstractSet<E> implements VaSet<E>, Cloneable, Serializable {
    private static final long serialVersionUID = 5120875045620310227L;

    private VaHashMap<E, Object> map;

    private final Object DEFAULT_VALUE = new Object();


    public VaHashSet() {
        map = new VaHashMap<>();
    }

    public VaHashSet(VaCollection<? extends E> c) {
        map = new VaHashMap<>(Math.max((int) (c.size() / 0.75f), 16));
        addAll(c);
    }

    public VaHashSet(int initCap) {
        map = new VaHashMap<>(initCap);
    }

    public VaHashSet(int initCap, float loadFactor) {
        map = new VaHashMap<>(initCap, loadFactor);
    }

    @Override
    public boolean add(E e) {
        return map.put(e, DEFAULT_VALUE) == null;
    }

    @Override
    public VaIterator<E> vaIterator() {
        return map.keySet().vaIterator();
    }


    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
        return map.remove(o) == DEFAULT_VALUE;
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    @SuppressWarnings("all")
    public Object clone() throws CloneNotSupportedException {
        VaHashSet<E> clone = (VaHashSet<E>) super.clone();
        clone.map = (VaHashMap<E, Object>) map.clone();
        return clone;
    }
}
