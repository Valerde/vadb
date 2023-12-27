package com.sovava.vacollection.api;

import java.util.Comparator;

@FunctionalInterface
public interface VaComparator<T> extends Comparator<T> {

    int compare(T o1, T o2);

    boolean equals(Object o);
}
