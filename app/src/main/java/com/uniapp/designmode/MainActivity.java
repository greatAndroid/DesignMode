package com.uniapp.designmode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.uniapp.designmode.single.Singleton;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    private void singleton() {
        // 注意事项
        // 使用反射能够破坏单例模式，所以应该慎用反射
        // 可以通过当第二次调用构造函数时抛出异常来防止反射破坏单例
        try {
            Constructor con = Singleton.class.getDeclaredConstructor();
            con.setAccessible(true);
            // 通过反射获取实例
            Singleton singleton1 = (Singleton) con.newInstance();
            Singleton singleton2 = (Singleton) con.newInstance();
            // 结果为 false, singleton1 和 singleton2 将是两个不同的实例
            System.out.println(singleton1 == singleton2);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}