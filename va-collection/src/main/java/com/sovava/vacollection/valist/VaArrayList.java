package com.sovava.vacollection.valist;

import com.sovava.vacollection.api.*;
import com.sovava.vacollection.api.function.VaPredicate;
import com.sovava.vacollection.api.function.VaUnaryOperator;
import com.sovava.vacollection.collections.VaArrays;

import java.io.Serializable;
import java.util.*;
import java.util.function.Consumer;

/**
 * Description: 由于后续不会将此类用于线程不安全场景，因此暂不对类中的方法做快速失败与对线程安全的一些基本措施
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
    @SuppressWarnings("unchecked")
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
        Objects.requireNonNull(c);
        return batchRemove(c, false);
    }

    private boolean batchRemove(VaCollection<?> c, boolean exit) {
        Object[] dataTmp = this.elementData;
        int toIdx, newSize = 0;
        boolean modified = false;
        for (toIdx = 0; toIdx < size; toIdx++) {
            if (c.contains(dataTmp[toIdx]) == exit) {
                dataTmp[newSize++] = dataTmp[toIdx];
            }
        }
        if (newSize != size) {
            for (int i = newSize; i < size; i++) {
                dataTmp[i] = null;
            }
            size = newSize;
            modified = true;
        }
        return modified;

    }

    @Override
    public boolean retainAll(VaCollection<?> c) {
        Objects.requireNonNull(c);
        return batchRemove(c, true);
    }

    @Override
    public void replaceAll(VaUnaryOperator<E> operator) {
        Objects.requireNonNull(operator);
        int size = this.size;
        for (int i = 0; i < size; i++) {
            elementData[i] = operator.apply(elementData(i));
        }

    }

    @Override
    public void forEach(VaConsumer<? super E> action) {
        Objects.requireNonNull(action);
        int size = this.size;
        for (int i = 0; i < size; i++) {
            action.accept(elementData(i));
        }
    }

    @Override
    public boolean removeIf(VaPredicate<? super E> filter) {
        Objects.requireNonNull(filter);
        int i, j;
        for (i = 0, j = 0; i < size; i++) {
            if (!filter.test(elementData(i))) {
                elementData[j++] = elementData[i];
            }
        }
        boolean modified = j != size;
        int size = this.size;
        this.size = j;
        if (modified) {
            while (j < size) {
                elementData[j++] = null;
            }
        }
        return modified;
    }

    @Override
    public VaIterator<E> vaIterator() {
        return new Itr();
    }

    @Override
    public VaListIterator<E> listIterator(int index) {
        return new ListItr(index);
    }

    @Override
    public VaListIterator<E> listIterator() {
        return new ListItr(0);
    }

    @Override
    public void sort(VaComparator<? extends E> c) {
        Arrays.sort((E[]) elementData, 0, size, (Comparator<? super E>) c);
        //TODO VaArrays.sort()

    }

    @Override
    public VaList<E> subList(int fromIndex, int toIndex) {
        subListRangeCheck(fromIndex, toIndex, size);
        return new SubList(this, 0, fromIndex, toIndex);
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
        elementData = VaArrays.copyOf(elementData, newCap);
    }

    private int getHugeCap(int minCap) {
        if (minCap < 0) {
            throw new OutOfMemoryError();
        }
        return minCap > MAX_ARRAY_SIZE ? Integer.MAX_VALUE : MAX_ARRAY_SIZE;
    }

    private class Itr implements VaIterator<E> {
        int cursor;
        int lastRet = -1;


        @Override
        public boolean hasNext() {
            return cursor != size;
        }

        @Override
        public E next() {
            if (cursor >= size) {
                throw new NoSuchElementException();
            }
            lastRet = cursor;
            cursor++;
            return elementData(lastRet);
        }

        @Override
        public void remove() {
            if (lastRet == -1) {
                throw new IllegalStateException();
            }
            VaArrayList.this.remove(lastRet);
            cursor = lastRet;
            lastRet = -1;
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            for (int i = cursor; i < size; i++) {
                action.accept(elementData(i));
            }
            cursor = size;
            lastRet = cursor - 1;
        }
    }

    private class ListItr extends Itr implements VaListIterator<E> {
        ListItr(int idx) {
            super();
            cursor = idx;
        }

        @Override
        public boolean hasPrevious() {
            return cursor >= 0;
        }

        @Override
        public E previous() {
            int retIdx = cursor - 1;
            if (retIdx < 0) {
                throw new NoSuchElementException();
            }
            cursor = lastRet = retIdx;
            return elementData(lastRet);
        }

        @Override
        public int nextIndex() {
            return cursor;
        }

        @Override
        public int previousIndex() {
            return cursor - 1;
        }

        @Override
        public void set(E e) {
            if (lastRet < 0) {
                throw new NoSuchElementException();
            }
            VaArrayList.this.set(lastRet, e);
        }

        @Override
        public void add(E e) {
            VaArrayList.this.add(cursor, e);
            cursor++;
            lastRet = -1;
        }
    }

    /**
     * description: sublist本质上和其parent是一个内存对象，只不过将其中的一段成为sublist，两者并不独立
     */
    private class SubList extends VaAbstractList<E> implements RandomAccess {

        private final VaAbstractList<E> parent;

        private final int parentOffset;

        private final int offset;
        int size;

        SubList(VaAbstractList<E> parent, int offset, int fromIdx, int toIdx) {
            this.parent = parent;
            this.parentOffset = offset;
            this.offset = offset + fromIdx;
            this.size = toIdx - fromIdx;
        }


        @Override
        public int size() {
            return this.size;
        }

        @Override
        public E get(int idx) {
            rangeCheck(idx);
            return VaArrayList.this.get(offset + idx);
        }

        @Override
        public E set(int idx, E e) {
            rangeCheck(idx);
            E oldV = VaArrayList.this.get(offset + idx);
            VaArrayList.this.set(offset + idx, e);
            return oldV;
        }

        @Override
        public void add(int idx, E e) {
            rangeCheck(idx);
            parent.add(parentOffset + idx, e);
            this.size++;
        }

        @Override
        public E remove(int idx) {
            rangeCheck(idx);
            E oldV = parent.remove(parentOffset + idx);
            this.size--;
            return oldV;
        }

        @Override
        public boolean addAll(VaCollection<? extends E> c) {
            return addAll(this.size, c);
        }

        @Override
        public boolean addAll(int idx, VaCollection<? extends E> c) {
            rangeCheckForAdd(idx);
            int cSize = c.size();
            if (cSize == 0) {
                return false;
            }
            parent.addAll(parentOffset + idx, c);
            this.size += cSize;
            return true;
        }


        @Override
        public VaIterator<E> vaIterator() {
            return listIterator();
        }

        @Override
        public VaListIterator<E> listIterator(int index) {
            rangeCheckForAdd(index);
            return new VaListIterator<E>() {
                int cursor = index;
                int lastRet = -1;

                @Override
                public boolean hasPrevious() {
                    return cursor != 0;
                }

                @Override
                public E previous() {
                    if (cursor - 1 < 0) {
                        throw new NoSuchElementException();
                    }
                    cursor--;
                    lastRet = cursor + 1;
                    return elementData(offset + lastRet);
                }

                @Override
                public int nextIndex() {
                    return cursor;
                }

                @Override
                public int previousIndex() {
                    return cursor - 1;
                }

                @Override
                public void set(E e) {
                    if (lastRet < 0) throw new IllegalStateException();
                    SubList.this.set(lastRet, e);
                }

                @Override
                public void add(E e) {
                    SubList.this.add(cursor, e);
                    cursor++;
                    lastRet = -1;
                }

                @Override
                public boolean hasNext() {
                    return cursor != SubList.this.size;
                }

                @Override
                public E next() {
                    if (cursor >= SubList.this.size) {
                        throw new NoSuchElementException();
                    }
                    cursor++;
                    lastRet = cursor - 1;
                    return elementData(offset + lastRet);
                }

                @Override
                public void remove() {
                    if (lastRet < 0) throw new IllegalStateException();
                    SubList.this.remove(lastRet);
                    cursor = lastRet;
                    lastRet = -1;
                }

                @Override
                public void forEachRemaining(Consumer<? super E> action) {
                    Objects.requireNonNull(action);
                    for (int i = cursor; i < SubList.this.size; i++) {
                        action.accept(elementData(offset + i));
                    }
                    cursor = size;
                    lastRet = cursor;
                }
            };
        }

        @Override
        public VaList<E> subList(int fromIndex, int toIndex) {
            subListRangeCheck(fromIndex, toIndex, this.size);
            return new SubList(this, offset, fromIndex, toIndex);
        }

        protected void rangeCheck(int idx) {
            if (idx < 0 || idx > this.size) {
                throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + this.size);
            }
        }

        private void rangeCheckForAdd(int idx) {
            if (idx < 0 || idx >= this.size) {
                throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + this.size);
            }
        }


    }
}
