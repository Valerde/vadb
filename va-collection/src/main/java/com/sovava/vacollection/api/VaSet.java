package com.sovava.vacollection.api;


/**
 * description: set类接口
 *
 * @Author sovava
 * @Date 12/21/23 11:44 PM
 */
public interface VaSet<E> extends VaCollection<E> {

    int size();

    @Override
    VaIterator<E> vaIterator();

    @Override
    boolean contains(Object o);

    @Override
    boolean isEmpty();

    @Override
    Object[] toVaArray();

    @Override
    <T> T[] toVaArray(T[] ts);

    @Override
    boolean add(E e);

    @Override
    boolean remove(Object o);


    @Override
    boolean containsAll(VaCollection<?> c);


    @Override
    boolean addAll(VaCollection<? extends E> c);

    @Override
    boolean retainAll(VaCollection<?> c);

    @Override
    boolean removeAll(VaCollection<?> c);

    @Override
    void clear();

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
