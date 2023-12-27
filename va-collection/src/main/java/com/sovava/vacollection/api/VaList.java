package com.sovava.vacollection.api;

import com.sovava.vacollection.api.function.VaUnaryOperator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

/**
 * description:
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
        VaListIterator<E> vlit = listIterator();
        while(vlit.hasNext()){
            vlit.set(operator.apply(vlit.next()));
        }
    }

    default void sort(VaComparator<? extends E> c) {
        Object[] os = this.toVaArray();
//   TODO     VaArrays.sort(a,(VaComparator) c);

        Arrays.sort(os, (Comparator) c);
        VaListIterator<E> vlit = this.listIterator();
        for (Object e : os) {
            vlit.next();
            vlit.set((E) e);
        }
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
