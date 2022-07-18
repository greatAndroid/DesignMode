package com.uniapp.designmode.single;

import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 单例模式
 * 1. 单例类只能有一个实例
 * 2. 单例类必须自己创建自己的唯一实例
 * 3. 单例类必须给所有其他对象提供这一实例
 *
 * 应用场景
 * 频繁访问数据库或文件的对象
 * 工具类对象
 * 创建对象时耗时过多或耗费资源过多，但又经常用到的对象
 *
 * 优点
 * 内存中只存在一个对象，节省了系统资源
 * 避免对资源的多重占用，例如一个文件操作，由于只有一个实例存在内存中，避免对同一资源文件的同时操作
 *
 * 缺点
 * 获取对象时不能用 new
 * 单例对象如果持有 Context，那么很容易引发内存泄漏
 * 单例模式一般没有接口，扩展很困难，若要扩展，只能修改代码来实现
 */
public class Singleton implements Serializable {
    private static boolean flag = true;

    // 构造方法为 private，防止外部代码直接通过 new 来构造多个对象
    private Singleton() {
        // 防止反射破坏单例
        // 通过第二次调用构造函数时抛出异常来防止反射破坏单例
        if(flag) {
            flag = !flag;
        } else {
            throw new RuntimeException("单例模式被破坏");
        }
    }

    // 饿汉式
    // 优点：写法简单，线程安全
    // 缺点：没有懒加载的效果，如果没有使用过的话会造成内存浪费
    // 在类初始化时，已经自行实例化，所以线程是安全的
    private static final Singleton singleton =  new Singleton();

    // 通过 getInstance() 方法获取实例对象
    public static Singleton getInstance() {
        return singleton;
    }

    // 懒汉式（线程不安全）
    // 优点：实现类懒加载的效果
    // 缺点：线程不安全
    private static Singleton singleton1 = null;

    public static Singleton getInstance1() {
        if (singleton1 == null) {
            // 在第一次调用 getInstance1() 时才实例化，实现懒加载，所以叫懒汉式
            singleton1 = new Singleton();
        }
        return singleton1;
    }

    // 懒汉式（线程安全）
    // 优点：实现了懒加载，线程安全
    // 缺点：使用 synchronized 会造成不必要的同步开销，而且大部分时我们是用不到同步的
    // 加上 synchronized 同步
    public static synchronized Singleton getInstance2() {
        if (singleton1 == null) {
            singleton1 = new Singleton();
        }
        return singleton1;
    }

    // 双重检查锁定（DCL）
    // 优点：懒加载，线程安全，效率较高
    // 缺点：volatile 影响一点性能，高并发下有一定的缺陷，某些情况下 DCL 会失效，虽然概率较小
    // 使用 volatile 能够防止代码的重排序，保证得到的对象是初始化过
    private volatile static Singleton singleton2;

    public static Singleton getSingleton2() {
        if (singleton2 == null) { // 第一次检查，避免不必要的同步
            synchronized (Singleton.class) { // 同步
                if (singleton2 == null) { // 第二次检查，为 null 时才创建实例
                    singleton2 = new Singleton();
                }
            }
        }
        return singleton2;
    }

    // 静态内部类
    //优点：懒加载，线程安全，推荐使用
    public static Singleton getInstance3() {
        // 第一次调用 getInstance3() 方法时才加载 SingletonHolder 并初始化 sInstance
        return SingletonHolder.sInstance;
    }

    private static class SingletonHolder {
        private static final Singleton sInstance = new Singleton();
    }

    // 枚举单例
    // 优点：线程安全，写法简单，能防止反序列化重新创建新的对象
    // 缺点：可读性不高，枚举会比静态常量多那么一丁点内存
    public enum Singleton1 {
        // 定义一个枚举的元素，它就是 Singleton 的一个实例
        INSTANCE;

        public void doSomething() {

        }
    }

    // 使用容器实现单例模式
    // 优点：方便管理
    // 缺点：写法稍复杂
    public static class SingletonManager {
        private static Map<String, Object> objectMap = new HashMap<>();

        public static void  registerService(String key, Object instance) {
            if (!objectMap.containsKey(key)) {
                objectMap.put(key, instance); // 添加单例
            }
        }

        public static Object getService(String key) {
            return objectMap.get(key);// 获取单例
        }
    }

    // 反序列化时也会破坏单例模式，可以通过重写 readResolve 方式避免，以饿汉式为例
    private Object readResolve() throws ObjectStreamException {
        return singleton;
    }
}
