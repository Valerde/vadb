package com.sovava.vadb.test.collection.map;

import com.sovava.vacollection.api.VaMap;
import com.sovava.vacollection.api.VaSet;
import com.sovava.vacollection.vamap.VaHashMap;
import com.sovava.vadb.test.collection.utils.RandomObject;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;


/**
 * Description:
 * VaHashMap已支持array+链表的方式存储，这个类测试已有功能
 *
 * @author: ykn
 * @date: 2023年12月24日 6:29 PM
 **/
@Slf4j
public class Test1Part {

    /**
     * description:
     * <p>测试结果：
     * <p>1. 编译错误，VaArrays中copyOf方法错误，需要强转为Object
     * <p>2. 代码优化，getNode方法中起始处定义变量优化语法
     *
     * @Author sovava
     * @Date 12/24/23 7:14 PM
     */
    @Test
    public void testBasicFunc() {
        VaMap<String, String> map = new VaHashMap<>();

        map.put("ykn", "vbf");
        map.put("cuh", "uabi");
        map.put("zzz", "dage");
        String zzz = map.get("zzz");
        Assert.assertEquals("dage", zzz);
        map.put("zzz", "fage2");
        Assert.assertEquals("fage2", map.get("zzz"));

        map.putIfAbsent("ykn", "test");
        Assert.assertEquals("vbf", map.getOrDefault("ykn", "def"));
        Assert.assertTrue(map.containsKey("ykn"));
        map.remove("ykn");
        Assert.assertNull(map.get("ykn"));
        Assert.assertEquals("def", map.getOrDefault("ykn", "def"));
        Assert.assertEquals(2, map.size());
        map.clear();
        Assert.assertTrue(map.isEmpty());
        Assert.assertEquals(0, map.size());

        map.putIfAbsent("ykn", "test");
        Assert.assertEquals(map.get("ykn"), "test");
        map.replace("ykn", "test", "test1");
        Assert.assertEquals(map.get("ykn"), "test1");
        map.replace("ykn", "test", "test2");
        Assert.assertEquals(map.get("ykn"), "test1");
        map.replace("ykn", "test3");
        Assert.assertEquals(map.get("ykn"), "test3");
    }

    /**
     * description:这个方法没有Assert，debug进入方法内看到了链表存在通过验证
     * <p>测试结果：putValue方法中 p = newNode(hash, key, value, null); -> node.next = newNode(hash, key, value, null);
     *
     * @Author sovava
     * @Date 12/24/23 7:16 PM
     */
    @Test
    public void testLinkedList() {
        VaMap<Integer, Integer> vmap = new VaHashMap<>();
        Map<Integer, Integer> map = new HashMap<>();
        Random rd = new Random();
        for (int i = 0; i < 100; i++) {
            int key = rd.nextInt();
            int value = rd.nextInt();
            vmap.put(key, value);
            map.put(key, value);
        }


        for (int i = 0; i < 10; i++) {
            int key = rd.nextInt();
            int value = rd.nextInt();
            vmap.put(key, value);
            map.put(key, value);
        }
    }

    /**
     * description:
     * 测试结果：resize方法中
     * <p>1. else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && oldCap > DEFAULT_INITIAL_CAPACITY) 忽略oldCap = Default_*
     * <p>2. float thr = newCap * loadFactor;                                  newCap需要强转类型(float)
     * <p>3. ((newCap -1) & head.hash)                                         rehash条件判断错误
     *
     * @Author sovava
     * @Date 12/24/23 8:01 PM
     */
    @Test
    public void testResize() {

        VaMap<String, String> vmap = new VaHashMap<>();
        Map<String, String> map2 = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            String key = RandomObject.getRandomString(10);
            String value = RandomObject.getRandomString(5);
            vmap.put(key, value);
            map2.put(key, value);
        }

        Set<String> ks = map2.keySet();
        for (String key : ks) {

            if (!Objects.equals(vmap.get(key), map2.get(key))) {
                Assert.assertEquals(vmap.get(key), map2.get(key));
            }
        }
    }

    /**
     * description:
     * <p>1. VaIterator 继承iterator以支持加强for
     * <p>3. vaCollection中的iterator方法默认返回vaIterator方法
     * <p>2. vahashmap中各个set的vaIterator方法new新值
     *
     * @Author sovava
     * @Date 12/24/23 8:26 PM
     */
    @Test
    public void testPutMapEntries() {
        VaMap<String, String> from = new VaHashMap<>();
        for (int i = 0; i < 100; i++) {
            String key = RandomObject.getRandomString(10);
            String value = RandomObject.getRandomString(5);
            from.put(key, value);
        }

        VaMap<String, String> to = new VaHashMap<>();
        for (int i = 0; i < 100; i++) {
            String key = RandomObject.getRandomString(10);
            String value = RandomObject.getRandomString(5);
            to.put(key, value);
        }

        to.putAll(from);

        VaSet<VaMap.VaEntry<String, String>> es = from.entrySet();
        for (VaMap.VaEntry<String, String> e : es) {
            log.info("{},{}", e.getKey(), e.getValue());
            Assert.assertEquals(e.getValue(), to.get(e.getKey()));
            Assert.assertTrue(to.containValue(e.getValue()));
        }

        VaSet<String> fromks = from.keySet();
        for (String key : fromks) {
            Assert.assertTrue(to.containsKey(key));
            Assert.assertEquals(to.get(key), from.get(key));
        }
    }

    @Test
    public void testComputeIfAbsent() {
        // 创建一个 HashMap 并添加一些键值对
        VaMap<String, Integer> map = new VaHashMap<>();
        map.put("a", 1);
        map.put("b", 2);

        // 使用 computeIfAbsent 方法计算新值并放入 Map
        map.computeIfAbsent("c", key -> 3); // 如果 "c" 不存在，则使用 lambda 表达式计算值并放入 map
        map.computeIfAbsent("b", key -> 5); // "b" 已存在，不会执行 lambda 表达式

        // 输出结果
        Assert.assertEquals(3, (int) map.get("c"));
        Assert.assertEquals(2, (int) map.get("b"));
    }

    @Test
    public void testComputeIfPresent() {
        // 创建一个 HashMap 并添加一些键值对
        VaMap<String, Integer> map = new VaHashMap<>();
        map.put("a", 1);
        map.put("b", 2);

        // 使用 computeIfPresent 方法根据已存在的键计算新值并放入 Map
        map.computeIfPresent("a", (key, value) -> value * 10); // 如果 "a" 存在，则使用 lambda 表达式计算新值并放入 map
        map.computeIfPresent("c", (key, value) -> value * 10); // "c" 不存在，不会执行 lambda 表达式

        // 输出结果
        Assert.assertEquals(10, (int) map.get("a"));
        Assert.assertEquals(2, (int) map.get("b"));
        System.out.println(map); // 应该输出 {a=10, b=2}
    }

    @Test
    public void testCompute() {
        VaHashMap<String, Integer> hashMap = new VaHashMap<>();
        hashMap.put("A", 1);
        hashMap.put("B", 2);
        hashMap.put("C", 3);

        // 使用compute()方法对指定键进行操作
        hashMap.compute("D", (key, value) -> value == null ? -1 : value + 10);

        // 检查结果
        Assert.assertEquals(-1, (int) hashMap.get("D"));

        // 使用compute()方法对指定键进行操作
        hashMap.compute("A", (key, value) -> value + 10);

        // 检查结果
        Assert.assertEquals(11, (int) hashMap.get("A"));
    }

    @Test
    public void testForeach() {
        VaMap<String, Integer> map = new VaHashMap<>();
        map.put("apple", 10);
        map.put("banana", 20);
        map.put("orange", 30);

        // 使用forEach方法遍历HashMap
        map.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    @Test
    public void testMerge() {
        // 创建HashMap对象并添加初始数据
        VaHashMap<String, Integer> map = new VaHashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);

        // 使用merge()方法将新的键值对与旧的键值对合并
        map.merge("A", 10, Integer::sum);

        // 检查结果
        Assert.assertEquals(11, (int) map.get("A"));

        VaMap<String, String> map2 = new VaHashMap<>();
        Map<String, String> map3 = new HashMap<>();
        // Case 1: 键不存在，直接放入值
        map2.merge("key1", "value1", (oldValue, newValue) -> oldValue + newValue);
        map3.merge("key1", "value1", (oldValue, newValue) -> oldValue + newValue);

        // Case 2: 键存在，根据合并函数处理已存在的值
        map2.merge("key1", "value2", (oldValue, newValue) -> oldValue + newValue);
        map3.merge("key1", "value2", (oldValue, newValue) -> oldValue + newValue);

        // Case 3: 如果合并函数返回null，则删除该键值对
        map2.put("key2", "v");
        map3.put("key2", "v");
        map2.merge("key2", "newValue", (oldValue, newValue) -> null);
        map3.merge("key2", "newValue", (oldValue, newValue) -> null);

        // Case 4: 如果合并函数返回新值，则更新键对应的值
        map2.merge("key3", "value3", (oldValue, newValue) -> "newValue");
        map3.merge("key3", "value3", (oldValue, newValue) -> "newValue");

        // 打印Map中的内容
        map3.forEach((key, value) -> Assert.assertEquals(value, map2.get(key)));
        map2.forEach((key, value) -> System.out.println(key + " : " + value));
        map3.forEach((key, value) -> System.out.println(key + " : " + value));
    }

    @Test
    public void testValues() {
        Map<String, String> map = new HashMap<>();
        VaMap<String, String> vamap = new VaHashMap<>();
        for (int i = 0; i < 100; i++) {
            String key = RandomObject.getRandomString(10);
            String value = RandomObject.getRandomString(5);
            map.put(key, value);
            vamap.put(key, value);
            Assert.assertTrue(vamap.containValue(value));
        }

        for (String value : map.values()) {
            Assert.assertTrue(vamap.containValue(value));
        }
    }
}
