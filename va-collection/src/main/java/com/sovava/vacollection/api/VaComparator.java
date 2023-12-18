package com.sovava.vacollection.api;

@FunctionalInterface
public interface VaComparator<T> {

    int compare(T o1, T o2);

    boolean equals(Object o);
}
