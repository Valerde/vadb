package com.sovava.vacollection.valist;

import com.sovava.vacollection.api.*;
import com.sovava.vacollection.api.function.VaPredicate;
import com.sovava.vacollection.api.function.VaUnaryOperator;
import com.sovava.vacollection.collections.VaArrays;

import java.io.Serializable;
import java.util.RandomAccess;

/**
 * Description: TODO
 *
 * @author: ykn
 * @date: 2023年12月25日 8:32 PM
 **/
public class VaArrayList<E> extends VaAbstractList<E> implements VaList<E>, RandomAccess, Cloneable, Serializable {
    private static final long serialVersionUID = 5716796482536510252L;

    /**
     * 默认大小， 10
     */
    private static final int DEFAULT_CAPACITY = 10;

    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - 8;

    /**
     * 用于空实例的共享数组实例
     */
    private static final Object[] EMPTY_ELEMENT_DATA = {};
    /**
     * 用于默认大小的空实例
     */
    private static final Object[] DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA = {};

    Object[] elementData;

    private int size;

    public VaArrayList(int initialCapacity) {
        if (initialCapacity > 0) {
            this.elementData = new Object[initialCapacity];
        } else if (initialCapacity == 0) {
            this.elementData = EMPTY_ELEMENT_DATA;
        } else {
            throw new IllegalArgumentException("不合理的容量：" + initialCapacity);
        }
    }

    public VaArrayList() {
        this.elementData = DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA;
    }

    public VaArrayList(VaCollection<? extends E> c) {
        elementData = c.toVaArray();
        if ((size = elementData.length) != 0) {
            if (elementData.getClass() != Object[].class) {
                elementData = VaArrays.copyOf(elementData, size, Object[].class);
            }
        } else {
            this.elementData = EMPTY_ELEMENT_DATA;
        }
    }

    @SuppressWarnings("unchecked")
    E elementData(int idx) {
        return (E) elementData[idx];
    }

    public int size() {
        return size;
    }

    public E set(int idx, E e) {
        rangeCheck(idx);
        E oldV = elementData(idx);
        elementData[idx] = e;
        return oldV;
    }

    @Override
    public E get(int idx) {
        rangeCheck(idx);
        return elementData(idx);
    }

    @Override
    public boolean add(E e) {
        ensureCapInternal(size + 1);
        elementData[size++] = e;
        return true;
    }

    @Override
    public void add(int idx, E e) {
        rangeCheck(idx);
        ensureCapInternal(size + 1);
        System.arraycopy(elementData, idx, elementData, idx + 1, size - idx);
        elementData[idx] = e;
        size++;
    }

    @Override
    public E remove(int idx) {
        rangeCheck(idx);
        E oldV = elementData(idx);
        int numMoved = size - idx - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, idx + 1, elementData, idx, numMoved);
        }
        return oldV;
    }

    @Override
    public boolean remove(Object o) {
        if (null == o) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    fastRemove(i);
                    return true;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    fastRemove(i);
                    return true;
                }
            }
        }
        return false;
    }

    private void fastRemove(int idx) {
        int numMoved = size - idx - 1;
        if (numMoved > 0) {
            System.arraycopy(elementData, idx + 1, elementData, idx, numMoved);
        }
        elementData[--size] = null;
    }


    public void clear() {
        for (int i = 0; i < size; i++) {
            elementData[i] = null;
        }
        size = 0;
    }


    public boolean addAll(VaCollection<? extends E> c) {
        Object[] cs = c.toVaArray();
        int csSize = c.size();
        if (csSize == 0) return false;
        ensureCapInternal(size + csSize);
        System.arraycopy(cs, 0, elementData, size, csSize);
        size += csSize;
        return true;
    }

    public boolean addAll(int index, VaCollection<? extends E> c) {
        rangeCheck(index);
        Object[] cs = c.toVaArray();
        int csSize = c.size();
        if (csSize == 0) return false;
        ensureCapInternal(size + csSize);
        int numMoved = size - index;
        if (numMoved > 0) {
            System.arraycopy(elementData, index, elementData, index + csSize, numMoved);
        }
        System.arraycopy(cs, 0, elementData, index, csSize);
        size += csSize;
        return true;
    }

    protected void removeRange(int fromIndex, int toIndex) {
        System.arraycopy(elementData, toIndex, elementData, fromIndex, toIndex - fromIndex);
        int newSize = size - (toIndex - fromIndex);
        for (int i = newSize; i < size; i++) {
            elementData[i] = null;
        }
        size = newSize;
    }

    public void ensureCapInternal(int minCap) {
        ensureLegalCap(calcCap(elementData, minCap));
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int indexOf(Object o) {
        if (null == o) {
            for (int i = 0; i < size; i++) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Object o) {
        return indexOf(o) != -1;
    }

    @Override
    public Object[] toVaArray() {
        return VaArrays.copyOf(elementData, size);
    }

    @Override
    public <T> T[] toVaArray(T[] ts) {
        if (ts.length < size) {
            return (T[]) VaArrays.copyOf(ts, size, ts.getClass());
        }
        System.arraycopy(elementData, 0, ts, 0, size);
        if (ts.length > size) {
            ts[size] = null;
        }
        return ts;
    }

    @Override
    public int lastIndexOf(Object o) {
        if (null == o) {
            for (int i = size - 1; i > 0; i--) {
                if (elementData[i] == null) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i > 0; i--) {
                if (o.equals(elementData[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        VaArrayList<?> clone = (VaArrayList<?>) super.clone();
        clone.elementData = VaArrays.copyOf(this.elementData, size);
        return clone;
    }

    @Override
    public boolean removeAll(VaCollection<?> c) {
        //TODO
    }

    @Override
    public boolean retainAll(VaCollection<?> c) {
        //TODO
    }

    @Override
    public void replaceAll(VaUnaryOperator<E> operator) {
        //TODO
    }

    @Override
    public void forEach(VaConsumer<? super E> action) {

    }

    @Override
    public boolean removeIf(VaPredicate<? super E> filter) {

    }

    @Override
    public VaIterator<E> vaIterator() {


    }

    @Override
    public VaListIterator<E> listIterator(int index) {

    }

    @Override
    public VaListIterator<E> listIterator() {

    }

    @Override
    public void sort(VaComparator<? extends E> c) {
        VaArrays.s

    }

    @Override
    public VaList<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex,toIndex,size);
        //TODO
    }

    public void trimToSize() {

    }

    private void subListRangeCheck(int fromIdx, int toIdx, int size) {
        if (fromIdx < 0) {
            throw new IndexOutOfBoundsException("fromidx = " + fromIdx);
        }
        if (toIdx > size) {
            throw new IndexOutOfBoundsException("toIdx = " + toIdx);
        }
        if (fromIdx > toIdx) {
            throw new IllegalArgumentException("fromIdx(" + fromIdx + ") > toIdx(" + toIdx + ")");
        }
    }

    private static int calcCap(Object[] elementData, int minCap) {
        if (elementData == DEFAULT_CAPACITY_EMPTY_ELEMENT_DATA) {
            return Math.max(DEFAULT_CAPACITY, minCap);
        }
        return minCap;
    }

    private void ensureLegalCap(int minCap) {
        if (minCap > elementData.length) {
            growArr(minCap);
        }
    }

    private void growArr(int minCap) {
        int oldCap = elementData.length;
        int newCap = oldCap + (oldCap >> 1);
        if (newCap < minCap) {
            newCap = minCap;
        }
        if (newCap > MAX_ARRAY_SIZE) {
            newCap = getHugeCap(minCap);
        }
        elementData = Arrays.copyOf(elementData, newCap);
    }

    private int getHugeCap(int minCap) {
        if (minCap < 0) {
            throw new OutOfMemoryError();
        }
        return minCap > MAX_ARRAY_SIZE ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }
}
