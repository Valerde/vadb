package com.sovava.vadb.test.collection.set;

import com.sovava.vacollection.api.VaIterator;
import com.sovava.vacollection.api.VaSet;
import com.sovava.vacollection.vaset.VaHashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

/**
 * Description: 测试VaHashSet，由于HashSet采用hashmap的方式，因此较为简单
 *
 * @author: ykn
 * @date: 2023年12月27日 3:24 PM
 **/
@Slf4j
public class TestHashSet {

    @Test
    public void testBasic() {
        VaSet<String> set1 = new VaHashSet<>();
        set1.add("ykn");
        set1.add("ykn");
        set1.add("nky");
        Assert.assertTrue(set1.contains("ykn"));
        Assert.assertFalse(set1.contains("sdf"));
        Assert.assertEquals(2, set1.size());
        String[] objects = set1.toVaArray(new String[1]);
        for (String s : objects) {
            log.info(s);
        }

        Assert.assertTrue(set1.remove("ykn"));
        Assert.assertFalse(set1.remove("ykn"));
        Assert.assertEquals(1, set1.size());
    }

    @Test
    public void testClone() throws CloneNotSupportedException {
        VaSet<String> set1 = new VaHashSet<>();
        set1.add("ykn");
        set1.add("ykn");
        set1.add("nky");

        VaSet<String> set2 = new VaHashSet<>(set1);

        set1.remove("nky");
        Assert.assertTrue(set2.contains("nky"));

        @SuppressWarnings("all")
        VaHashSet<String> set3 = (VaHashSet<String>) ((VaHashSet<String>) set2).clone();

        VaIterator<String> vit = set3.vaIterator();
        while (vit.hasNext()) {
            log.info(vit.next());
        }
    }
}
