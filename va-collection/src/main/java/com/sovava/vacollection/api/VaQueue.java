package com.sovava.vacollection.api;


public interface VaQueue<E> extends VaCollection<E> {

    @Deprecated
    boolean add(E e);

    /**
     * description: 向队尾添加元素
     *
     * @return boolean
     * @Author sovava
     * @Date 12/18/23 7:09 PM
     * @param: e - [E]
     */
    boolean offer(E e);

    @Deprecated
    E remove();

    /**
     * description: 出队
     *
     * @return E
     * @Author sovava
     * @Date 12/18/23 7:10 PM
     */
    E poll();


    /**
     * description: 检索但不删除，如果队列为空，抛出异常
     *
     * @return E
     * @Author sovava
     * @Date 12/18/23 7:12 PM
     */
    E element();

    /**
     * description: 检索但不删除，如果队列为空，返回null
     *
     * @return E
     * @Author sovava
     * @Date 12/18/23 7:11 PM
     */
    E peek();
}
