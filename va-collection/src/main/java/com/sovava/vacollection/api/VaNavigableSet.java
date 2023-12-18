package com.sovava.vacollection.api;

/**
 * description: navigable - 可导航的，意思就是说这个接口可以通过指定element找到某条件的元素
 *
 * @Author sovava
 * @Date 12/18/23 11:37 PM
 */
public interface VaNavigableSet<E> extends VaSortedSet<E> {

    /**
     * description: 严格小于，如果为空，返回null
     *
     * @return E
     * @Author sovava
     * @Date 12/18/23 11:38 PM
     * @param: e - [E]
     */
    E lower(E e);

    /**
     * description: 小于或等于，如果为空，返回null
     *
     * @return E
     * @Author sovava
     * @Date 12/18/23 11:40 PM
     * @param: e - [E]
     */
    E floor(E e);

    /**
     * description: 严格大于，如果为空，返回null
     *
     * @return E
     * @Author sovava
     * @Date 12/18/23 11:40 PM
     * @param: e - [E]
     */
    E higher(E e);

    /**
     * description: 大于或等于，如果为空，返回null
     *
     * @return E
     * @Author sovava
     * @Date 12/18/23 11:40 PM
     * @param: e - [E]
     */
    E ceiling(E e);


    E pollFirst();

    E pollLast();

    /**
     * description: 逆序
     *
     * @return VaNavigableSet<E>
     * @Author sovava
     * @Date 12/18/23 11:42 PM
     * @param:
     */
    VaNavigableSet<E> descendingSet();

    VaIterator<E> descendingIterator();

    VaNavigableSet<E> subSet(E fromElement, boolean fromInclusive,
                             E toElement, boolean toInclusive);

    VaNavigableSet<E> headSet(E toElement, boolean inclusive);

    VaNavigableSet<E> tailSet(E fromElement, boolean inclusive);

}
