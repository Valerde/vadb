package com.sovava.vacollection.api;

public interface VaListIterator<E> extends VaIterator<E> {

    boolean hasPrevious();

    E previous();

    int nextIndex();

    int previousIndex();

    void set(E e);

    /**
     * description: 将指定元素插入列表,位于previous之后，next之前
     *
     * @return void
     * @Author sovava
     * @Date 12/18/23 6:56 PM
     * @param: e - [E]
     */
    void add(E e);
}
