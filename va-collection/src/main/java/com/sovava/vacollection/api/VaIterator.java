package com.sovava.vacollection.api;

/**
 * description: TODO:
 *
 * @Author sovava
 * @Date 12/18/23 5:35 PM
 */
public interface VaIterator<E> {
    boolean hasNext();
    E next();
    void remove();

}
