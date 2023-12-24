package com.sovava.vacollection.api;

import java.util.Iterator;

/**
 * description: TODO:
 *
 * @Author sovava
 * @Date 12/18/23 5:35 PM
 */
public interface VaIterator<E> extends Iterator<E> {
    boolean hasNext();
    E next();
    void remove();

}
