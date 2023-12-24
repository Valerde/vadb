package com.sovava.vacollection.vaset;

import com.sovava.vacollection.api.VaCollection;
import com.sovava.vacollection.api.VaIterator;
import com.sovava.vacollection.api.VaSet;
import com.sovava.vacollection.collections.VaAbstractCollection;

import java.util.Objects;

/**
 * Description: set的抽象类
 *
 * @author: ykn
 * @date: 2023年12月22日 4:38 PM
 **/
public abstract class VaAbstractSet<E> extends VaAbstractCollection<E> implements VaSet<E> {

    protected VaAbstractSet() {
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof VaSet)) {
            return false;
        }

        VaCollection<?> c = (VaCollection<?>) obj;

        if (c.size() != size()) {
            return false;
        }
        try {
            return containsAll(c);
        } catch (ClassCastException | NullPointerException e) {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        VaIterator<E> vit = vaIterator();
        while (vit.hasNext()) {
            E elmt = vit.next();
            if (elmt != null) {

                h += elmt.hashCode();
            }
        }
        return h;
    }

    @Override
    public boolean removeAll(VaCollection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        //小驱动大
        if (size() > c.size()) {
            for (VaIterator<?> vit = c.vaIterator(); vit.hasNext(); ) {
                modified |= remove(vit.next());
            }
        } else {
            for (VaIterator<?> vit = vaIterator(); vit.hasNext(); ) {
                if (c.contains(vit.next())) {
                    vit.remove();
                    modified = true;
                }
            }
        }
        return modified;
    }
}
