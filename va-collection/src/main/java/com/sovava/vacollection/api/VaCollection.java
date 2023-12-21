package com.sovava.vacollection.api;

import com.sovava.vacollection.api.function.VaPredicate;

import java.util.Iterator;
import java.util.Objects;


/**
 * description: 集合层次结构中的根接口
 *
 * @Author sovava
 * @Date 12/18/23 5:29 PM
 */
public interface VaCollection<E> extends Iterable<E> {

    int size();

    boolean isEmpty();

    boolean contains(Object o);

    VaIterator<E> vaIterator();

    @Deprecated
    @SuppressWarnings("all")
    default Iterator<E> iterator() {
        return null;
    }

    Object[] toVaArray();

    <T> T[] toVaArray(T[] ts);

    boolean add(E e);

    boolean remove(Object o);

    boolean containsAll(VaCollection<?> c);

    boolean addAll(VaCollection<? extends E> c);

    boolean removeAll(VaCollection<?> c);

    default boolean removeIf(VaPredicate<? super E> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        VaIterator<E> each = vaIterator();
        while (each.hasNext()) {
            if (filter.test(each.next())) {
                each.remove();
                removed = true;
            }
        }
        return removed;
    }

    /**
     * description: 仅保留此集合中包含在指定集合中的元素
     *
     * @return boolean 如果此时集合发生了改变，则为true
     * @Author sovava
     * @Date 12/18/23 6:07 PM
     * @param: c - [com.sovava.vacollection.api.VaCollection<?>]
     */
    boolean retainAll(VaCollection<?> c);

    void clear();

    boolean equals(Object o);

    int hashCode();


    //TODO spliterator 和 stream

}
