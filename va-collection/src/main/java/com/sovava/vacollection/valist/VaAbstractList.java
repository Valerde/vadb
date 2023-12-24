package com.sovava.vacollection.valist;

import com.sovava.vacollection.api.VaCollection;
import com.sovava.vacollection.api.VaIterator;
import com.sovava.vacollection.api.VaList;
import com.sovava.vacollection.api.VaListIterator;
import com.sovava.vacollection.collections.VaAbstractCollection;
import com.sovava.vacollection.exception.VaNoSuchElementException;

import java.util.RandomAccess;


/**
 * Description: 此类提供了List接口的骨架实现，以最大限度地减少实现由“随机访问”数据存储（例如数组）支持的接口所需的工作量。对于顺序访问数据（例如链表），应优先使用AbstractSequentialList而不是此类。
 * 要实现不可修改的列表，程序员只需扩展此类并提供get(int)和size()方法的实现。
 * 要实现可修改列表，程序员必须另外重写set(int, E)方法（否则会抛出UnsupportedOperationException ）。如果列表是可变大小的，程序员必须另外重写add(int, E)和remove(int)方法。
 * 程序员通常应该根据Collection接口规范中的建议提供 void（无参数）和集合构造函数。
 * 与其他抽象集合实现不同，程序员不必提供迭代器实现；迭代器和列表迭代器由此类在“随机访问”方法之上实现： get(int) 、 set(int, E) 、 add(int, E)和remove(int)
 *
 * @author: ykn
 * @date: 2023年12月20日 4:50 PM
 **/
public abstract class VaAbstractList<E> extends VaAbstractCollection<E> implements VaList<E> {
    /**
     * description: 对于子类构造函数的调用，通常是隐式的
     *
     * @Author sovava
     * @Date 12/20/23 4:52 PM
     */
    protected VaAbstractList() {
    }

    public void add(int idx, E e) {
        throw new UnsupportedOperationException("该集合不允许对某一位置元素进行add操作");
    }

    public boolean add(E e) {
        add(size(), e);
        return true;
    }


    abstract public E get(int idx);

    public E set(int idx, E e) {
        throw new UnsupportedOperationException("该集合不允许对某一位置元素进行set操作");
    }


    public E remove(int idx) {
        throw new UnsupportedOperationException("该集合不允许对某一位置元素进行remove操作");
    }

    public int indexOf(Object o) {
        VaListIterator<E> vlit = listIterator();
        if (o == null) {
            while (vlit.hasNext()) {
                if (vlit.next() == null) {
                    return vlit.previousIndex();
                }
            }
        } else {
            while (vlit.hasNext()) {
                if (o.equals(vlit.next())) {
                    return vlit.previousIndex();
                }
            }
        }
        return -1;
    }


    @Override
    public boolean addAll(int index, VaCollection<? extends E> c) {
        rangeCheck(index);
        boolean modified = false;
        for (E e : c) {
            add(index, e);
            index++;
            modified = true;
        }
        return modified;
    }

    @Override
    public int lastIndexOf(Object o) {
        VaListIterator<E> vlit = listIterator(size());
        if (o == null) {
            while (vlit.hasPrevious()) {
                if (vlit.previous() == null) {
                    return vlit.nextIndex();
                }
            }
        } else {
            while (vlit.hasPrevious()) {
                if (o.equals(vlit.previous())) {
                    return vlit.nextIndex();
                }
            }
        }
        return -1;
    }

    @Override
    public VaIterator<E> vaIterator() {
        return new VaItr();
    }

    @Override
    public void clear() {
        removeRange(0, size());
    }

    @Override
    public VaListIterator<E> listIterator() {
        return new VaListItr(0);
    }

    @Override
    public VaListIterator<E> listIterator(int index) {
        rangeCheck(index);
        return new VaListItr(index);
    }

    public VaList<E> subList(int fromIndex, int toIndex) {

        if (this instanceof RandomAccess) {
            //TODO VaRandomAccessSubList
        } else {
            //TODO SubList
        }
        return null;
    }


    /**
     * description: 关于选择31作为乘子的原因：
     * https://stackoverflow.com/questions/299304/why-does-javas-hashcode-in-string-use-31-as-a-multiplier
     *
     * @return int
     * @Author sovava
     * @Date 12/20/23 7:06 PM
     */
    @Override
    public int hashCode() {
        int hashCode = 1;
        for (E e : this) {
            hashCode = 31 * hashCode + (e == null ? 0 : e.hashCode());
        }
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof VaList)) {
            return false;
        }
        VaListIterator<E> vlit1 = listIterator();
        VaListIterator<?> vlit2 = ((VaList<?>) obj).listIterator();
        while (vlit1.hasNext() && vlit2.hasNext()) {
            E e1 = vlit1.next();
            Object o = vlit2.next();
            if (e1 == null) {
                if (o != null) {
                    return false;
                }
            } else {
                if (!e1.equals(o)) {
                    return false;
                }
            }
        }
        return !(vlit1.hasNext() || vlit2.hasNext());
    }

    protected void removeRange(int fromIndex, int toIndex) {
        VaListIterator<E> vlit = listIterator(fromIndex);
        while (fromIndex < toIndex) {
            vlit.next();
            vlit.remove();
            fromIndex++;
        }
    }

    protected final void rangeCheck(int idx) {
        if (idx < 0 || idx > size()) {
            throw new IndexOutOfBoundsException("Index: " + idx + ", Size: " + size());
        }
    }

    private class VaItr implements VaIterator<E> {

        /*
         * 默认指针指向第0号元素前的位置，cursor表示在操作逻辑中（下一个要操作的元素idx）
         */
        int cursor = 0;

        /*
         * lastRet表示操作逻辑中（上一个操作的元素idx）如果remove删除该元素或add某元素，则重置为-1
         */
        int lastRet = -1;

        @Override
        public boolean hasNext() {
            return cursor != size();
        }

        @Override
        public E next() {
            try {
                E e = get(cursor);
                lastRet = cursor;
                cursor++;
                return e;
            } catch (IndexOutOfBoundsException e) {
                throw new VaNoSuchElementException();
            }
        }

        @Override
        public void remove() {
            if (lastRet < 0) {
                throw new IllegalStateException();
            }
            try {
                VaAbstractList.this.remove(lastRet);
                if (lastRet < cursor) cursor--; //如果是正向迭代
                lastRet = -1;
            } catch (IndexOutOfBoundsException e) {
                //TODO 应对并发修改的快速失败
            }
        }
    }

    private class VaListItr extends VaItr implements VaListIterator<E> {

        VaListItr(int idx) {
            cursor = idx;
        }

        @Override
        public boolean hasPrevious() {
            return cursor != 0;
        }

        @Override
        public E previous() {
            --cursor;
            E previous = get(cursor);
            lastRet = cursor;
            return previous;

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
                throw new IllegalStateException();
            }

            try {
                VaAbstractList.this.set(lastRet, e);

            } catch (IndexOutOfBoundsException ex) {
                //TODO
            }
        }

        @Override
        public void add(E e) {
            try {
                VaAbstractList.this.add(cursor, e);
                lastRet = -1;
                cursor++;
            } catch (IndexOutOfBoundsException ex) {
                //TODO
            }
        }
    }


}
