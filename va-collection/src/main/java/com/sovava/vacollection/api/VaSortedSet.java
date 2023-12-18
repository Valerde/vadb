package com.sovava.vacollection.api;

/**
 * Description: 按元素进行排序
 *
 * @author: ykn
 * @date: 2023年12月18日 11:05 PM
 **/
public interface VaSortedSet<E> extends VaSet<E> {

    VaComparator<? super E> comparator();

    VaSortedSet<E> subSet(E fromElement, E toElement);

    VaSortedSet<E> headSet(E fromElement);

    VaSortedSet<E> tailSet(E toElement);

    E first();

    E last();


}
