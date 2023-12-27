package com.sovava.vadb.test.collection.list;

import com.sovava.vacollection.api.VaList;
import com.sovava.vacollection.api.VaListIterator;
import com.sovava.vacollection.valist.VaArrayList;
import com.sovava.vadb.test.collection.utils.RandomObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * Description: 测试VaArrayList方法
 *
 * @author: ykn
 * @date: 2023年12月27日 12:09 PM
 **/
@Slf4j
public class TestVaArrayList {

    /**
     * description:
     * <p>测试结果：
     * <p>remove(idx) element[size--] = null;
     * <p>rangeCheck private in VaAbstractList private in VaArrayList
     *
     * @Author sovava
     * @Date 12/27/23 1:31 PM
     */
    @Test
    public void testBasicFunc() {
        VaList<String> list1 = new VaArrayList<>();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String elem = RandomObject.getRandomString(10);
            list1.add(elem);
            list2.add(elem);
        }

        Assert.assertEquals(100, list1.size());

        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(list1.get(i), list2.get(i));
            Assert.assertTrue(list1.contains(list2.get(i)));
        }

        list1.add(3, "ykn");
        Assert.assertEquals(101, list1.size());
        Assert.assertEquals("ykn", list1.get(3));
        Assert.assertEquals("ykn", list1.remove(3));
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(list1.get(i), list2.get(i));
        }

        list1.add(3, "ykn");
        list1.remove("ykn");
        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(list1.get(i), list2.get(i));
        }
        list1.add("ykn");
        Assert.assertEquals("ykn", list1.get(100));
        list2.add("ykn");
        Assert.assertEquals("ykn", list2.get(100));

        list1.clear();
        list1.forEach(System.out::println);
        Assert.assertEquals(0, list1.size());

        Assert.assertThrows("抛出异常", IndexOutOfBoundsException.class, () -> list1.get(0));

        list1.add("ykn");
        list1.add("tom");
        list1.add("ykn");

        Assert.assertEquals(0, list1.indexOf("ykn"));
        Assert.assertEquals(2, list1.lastIndexOf("ykn"));
        Assert.assertTrue(list1.removeIf("tom"::equals));
        list1.forEach(log::info);

    }

    /**
     * description:
     * <p>测试结果：
     * <p>addAll: rangeCheckForAdd
     * <p>VaList中的默认replaceAll方法和sort方法
     * <p>VaComparator继承Comparator接口 以兼容还 TODO 未写完的Arrays.sort()
     *
     * @Author sovava
     * @Date 12/27/23 2:07 PM
     */
    @Test
    public void testBatch() {
        VaList<String> list1 = new VaArrayList<>();
        VaList<String> list3 = new VaArrayList<>();
        List<String> list2 = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String elem = RandomObject.getRandomString(10);
            list1.add(elem);
            list2.add(elem);
        }

        list3.addAll(list1);

        for (int i = 0; i < 100; i++) {
            Assert.assertEquals(list3.get(i), list2.get(i));
        }
        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                list3.remove(list2.get(i));
            }
        }
        list1.removeAll(list3);
        for (int i = 0; i < 50; i++) {
            Assert.assertEquals(list1.get(i), list2.get((i << 1)));
        }
        list1.addAll(list1.size(), list3);
        list1.retainAll(list3);
        for (int i = 0; i < 50; i++) {
            Assert.assertEquals(list1.get(i), list2.get((i << 1) + 1));
        }

        list1.replaceAll((s) -> s + " replaceAll");
//        list1.forEach(log::info);

        VaList<String> list4 = new VaArrayList<>(list1);

        VaList<String> subList = list4.subList(10, 40);
        subList.replaceAll((s) -> s + " testsub");
//        list4.forEach(log::info);

        subList.sort(String::compareTo);
        list4.forEach(log::info);

    }

    /**
     * description:
     * 测试结果：iterator.hasPrevious()判断去掉 cursor == 0
     *
     * @Author sovava
     * @Date 12/27/23 2:44 PM
     */
    @Test
    public void testListIterator() {
        VaList<String> list = new VaArrayList<>();
        list.add("apple");
        list.add("banana");
        list.add("orange");

        List<String> list2 = new ArrayList<>();
        list2.add("apple");
        list2.add("banana");
        list2.add("orange");

        // 获取ListIterator对象
        VaListIterator<String> iterator = list.listIterator();
        ListIterator<String> iterator2 = list2.listIterator();

        // 遍历列表
        while (iterator.hasNext()) {
            Assert.assertEquals(iterator2.next(), iterator.next());
        }

        // 反向遍历列表
        while (iterator.hasPrevious()) {
            Assert.assertEquals(iterator2.previous(), iterator.previous());
        }

        // 添加元素
        iterator.add("grape");
        iterator2.add("grape");
        while (iterator.hasNext()) {
            Assert.assertEquals(iterator2.next(), iterator.next());
        }
        // 修改元素
        iterator.set("peach");
        iterator2.set("peach");
        while (iterator.hasPrevious()) {
            Assert.assertEquals(iterator2.previous(), iterator.previous());
        }
        // 删除元素
        iterator.next();
        iterator2.next();
        iterator.remove();
        iterator2.remove();

        // 获取迭代器当前位置
        Assert.assertEquals(iterator.nextIndex(), iterator2.nextIndex());


        // 获取迭代器前一个元素的位置
        Assert.assertEquals(iterator.previousIndex(), iterator2.previousIndex());


        // 判断是否还有下一个元素
        Assert.assertEquals(iterator.hasNext(), iterator2.hasNext());

        // 判断是否还有上一个元素
        Assert.assertEquals(iterator.hasPrevious(), iterator2.hasPrevious());
        // 获取下一个元素
        while (iterator.hasNext()) {
            Assert.assertEquals(iterator2.next(), iterator.next());
        }

        // 获取上一个元素
        while (iterator.hasPrevious()) {
            Assert.assertEquals(iterator2.previous(), iterator.previous());
        }
    }
}