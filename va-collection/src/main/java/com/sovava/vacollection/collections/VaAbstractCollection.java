package com.sovava.vacollection.collections;

import com.sovava.vacollection.api.VaCollection;
import com.sovava.vacollection.api.VaIterator;

import java.lang.reflect.Array;
import java.util.Objects;


/**
 * Description: 实现了Collection接口的基本方法
 *
 * @author: ykn
 * @date: 2023年12月19日 5:21 PM
 **/
public abstract class VaAbstractCollection<E> implements VaCollection<E> {

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    protected VaAbstractCollection() {
    }

    @Override
    public abstract VaIterator<E> vaIterator();

    @Override
    public abstract int size();

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public boolean contains(Object o) {
        VaIterator<E> vit = vaIterator();
        if (null == o) {
            while (vit.hasNext()) {
                if (vit.next() == null) {
                    return true;
                }
            }
        } else {
            while (vit.hasNext()) {
                if (o.equals(vit.next())) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * description: TODO 为什么会出现实际size与size值不一致的情况？
     *
     * @return Object
     * @Author sovava
     * @Date 12/19/23 7:41 PM
     */
    @Override
    public Object[] toVaArray() {

        int size = this.size();
        Object[] os = new Object[size];
        VaIterator<E> vit = vaIterator();
        for (int len = 0; len < size; len++) {
            if (!vit.hasNext()) {
                return VaArrays.copyOf(os, len);
            }
            os[len] = vit.next();
        }

        return vit.hasNext() ? finishToArray(os, vit) : os;
    }

    @SuppressWarnings("unchecked")
    private static <T> T[] finishToArray(T[] res, VaIterator vit) {
        int len = res.length;
        while (vit.hasNext()) {
            int cap = res.length;
            if (len == cap) {
                //动态扩容，每次扩容为原长度的1.5
                int newCap = cap + 1 + (cap >> 1);
                if (newCap > MAX_ARRAY_SIZE) {
                    newCap = hugeCapacity(cap + 1);
                }
                res = VaArrays.copyOf(res, newCap);
            }
            res[len++] = (T) vit.next();
        }
        //删除多余分配的空间
        return (len == res.length) ? res : VaArrays.copyOf(res, len);
    }

    private static int hugeCapacity(int minCapacity) {
        if (minCapacity < 0) {
//            Integer.MAX_VALUE+1 <0
            throw new OutOfMemoryError("需要的数组太大了");
        }
        return (minCapacity > MAX_ARRAY_SIZE) ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    /**
     * description: 将集合中的元素转化为指定的类型数组，此时参数ts的作用相当于是提供一个模板，规定了类型和大小以及可能可以复用的空间
     * 如果给定的参数数组size大于集合size，则将紧跟在集合元素的下一个位置置为null
     *
     * @return T
     * @Author sovava
     * @Date 12/19/23 8:01 PM
     * @param: ts - [T]
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T[] toVaArray(T[] ts) {

        int size = this.size();
        T[] res;
        if (ts.length >= size) {
            res = ts;
        } else {
            //使用反射创建对应泛型的数组
            res = (T[]) Array.newInstance(ts.getClass().getComponentType(), size);
        }
        VaIterator<E> vit = vaIterator();

        for (int i = 0; i < res.length; i++) {
            if (!vit.hasNext()) {
                if (ts == res) {
                    res[i] = null;
                } else if (ts.length < i) {
                    return VaArrays.copyOf(res, i);
                } else {
                    System.arraycopy(res, 0, ts, 0, i);
                    if (ts.length > i) {
                        ts[i] = null;
                    }
                }
                return ts;
            }
            res[i] = (T) vit.next();
        }

        return vit.hasNext() ? finishToArray(res, vit) : res;
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException("此集合不支持添加元素");
    }

    @Override
    public boolean remove(Object o) {

        VaIterator<E> vit = vaIterator();
        if (o == null) {
            while (vit.hasNext()) {
                if (vit.next() == null) {
                    vit.remove();
                    return true;
                }
            }
        } else {
            while (vit.hasNext()) {
                if (o.equals(vit.next())) {
                    vit.remove();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean containsAll(VaCollection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(VaCollection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (add(e))
                modified = true;
        }
        return modified;
    }

    @Override
    public boolean removeAll(VaCollection<?> c) {
        Objects.requireNonNull(c);
        VaIterator<E> vit = vaIterator();
        boolean modified = false;
        while (vit.hasNext()) {
            if (c.contains(vit.next())) {
                vit.remove();
                modified = true;
            }
        }
        return modified;
    }


    @Override
    public boolean retainAll(VaCollection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        VaIterator<E> vit = vaIterator();
        while (vit.hasNext()) {
            if (!c.contains(vit.next())) {
                vit.remove();
                modified = true;
            }
        }
        return modified;
    }

    @Override
    public void clear() {
        VaIterator<E> vit = vaIterator();
        while (vit.hasNext()) {
            vit.next();
            vit.remove();
        }
    }


    @Override
    public String toString() {

        VaIterator<E> vit = vaIterator();
        if (!vit.hasNext()) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');

        while (true) {
            sb.append(vit.next());
            if (!vit.hasNext()) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
        }
    }
}
