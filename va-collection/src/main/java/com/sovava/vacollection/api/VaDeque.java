package com.sovava.vacollection.api;

/**
 * description: 双端队列
 *
 * @Author sovava
 * @Date 12/18/23 10:14 PM
 */
public interface VaDeque <E> extends VaQueue<E> {

    boolean offerFirst(E e);

    boolean offerLast(E e);

    /**
     * description: 返回并删除队头元素，如果队列为空，将抛出异常
     *
     * @Author sovava
     * @Date 12/18/23 10:15 PM
     * @return E
     */
    E removeFirst();

    /**
     * description: 返回并删除队尾元素，如果队列为空，将抛出异常
     *
     * @Author sovava
     * @Date 12/18/23 10:16 PM
     * @return E
     */
    E removeLast();

    /**
     * description: 返回并删除队头元素，如果队列为空，将返回null
     *
     * @Author sovava
     * @Date 12/18/23 10:17 PM
     * @return E
     */
    E pollFirst();

    /**
     * description: 返回并删除队尾元素，如果队列为空，将返回null
     *
     * @Author sovava
     * @Date 12/18/23 10:18 PM
     * @return E
     */
    E pollLast();

    /**
     * description: 查看队头元素，如果队列为空，抛出异常
     *
     * @Author sovava
     * @Date 12/18/23 10:19 PM
     * @return E
     */
    E getFirst();

    /**
     * description: 查看队尾元素，如果队列为空，抛出异常
     *
     * @Author sovava
     * @Date 12/18/23 10:20 PM
     * @return E
     */
    E getLast();

    /**
     * description: 查看队头元素，如果队列为空，返回null
     *
     * @Author sovava
     * @Date 12/18/23 10:22 PM
     * @return E
     */
    E peekFirst();

    /**
     * description: 查看队尾元素，如果队列为空，返回null
     *
     * @Author sovava
     * @Date 12/18/23 10:22 PM
     * @return E
     */
    E peekLast();

    /**
     * description: 删除双端队列中第一个给定的元素
     *
     * @Author sovava
     * @Date 12/18/23 10:25 PM
     * @param: o - [java.lang.Object]
     * @return boolean
     */
    boolean removeFirstOccurrence(Object o);

    /**
     * description: 删除双端队列中最后一个给定的元素
     *
     * @Author sovava
     * @Date 12/18/23 10:26 PM
     * @param: o - [java.lang.Object]
     * @return boolean
     */
    boolean removeLastOccurrence(Object o);


    //stack的方法

    void push(E e);

    E pop();


    /**
     * description: 返回逆序迭代器
     *
     * @Author sovava
     * @Date 12/18/23 10:28 PM
     * @return VaIterator<E>
     */
    VaIterator<E> descendingIterator();
}
