package com.sovava.vacollection.api;

import com.sovava.vacollection.api.function.VaUnaryOperator;

import java.util.Objects;

/**
 * description: TODO:
 *
 * @Author sovava
 * @Date 12/18/23 6:21 PM
 */
public interface VaList<E> extends VaCollection<E> {

//    boolean addAll(VaCollection<? extends E> c);

    /**
     * description: 将制定集合插入到此列表的指定位置之后，后面的元素顺移
     *
     * @return boolean    true如果此列表因调用而更改
     * @Author sovava
     * @Date 12/18/23 6:24 PM
     * @param: index - [int]
     * @param: c - [com.sovava.vacollection.api.VaCollection<?extendsE>]
     */
    boolean addAll(int index, VaCollection<? extends E> c);

    default void replaceAll(VaUnaryOperator<E> operator) {
        Objects.requireNonNull(operator);

    }

    default void sort(VaComparator<? extends E> c) {
        Object[] os = this.toVaArray();
//        VaArrays.sort(a,(VaComparator) c);

    }


    E get(int index);

    E set(int index, E e);

    void add(int index, E e);

    E remove(int index);

    int indexOf(Object o);

    int lastIndexOf(Object o);

    VaListIterator<E> listIterator();

    VaListIterator<E> listIterator(int index);

    VaList<E> subList(int fromIndex, int toIndex);

}
