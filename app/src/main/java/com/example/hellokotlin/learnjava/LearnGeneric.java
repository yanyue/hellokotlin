package com.example.hellokotlin.learnjava;

import android.util.Log;
import com.example.apt_annotation.ExecutorTest;
import com.example.apt_annotation.Learning;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@ExecutorTest
public class LearnGeneric implements Learning {

    private static final String TAG = "LearnGeneric";

    class Food {}
    class Fruit extends Food {}
    class Apple extends Fruit {}
    class RedApple extends Apple {}
    class GreenApple extends Apple {}

    /**
     * 能取（取出来都是父类型），不能存
     */
    private void upperLimit() {
        Fruit apple = new Apple();
        // compile error
        // List<Fruit> plate = new ArrayList<Apple>();
        // Good
        List<? extends Fruit> plate = new ArrayList<Apple>();

        // compile error
        // plate.add(apple);
        // good
        Fruit fruit = plate.get(0);
    }

    /**
     * 能存（存的都是子类型），不能取（取出来都是object类型，不能安全的使用）
     */
    private void lowerLimit() {
        // 定义了一个plate，它的下界为 ? super Apple
        List<? super Apple> plate = new ArrayList<>();

        plate.add(new Apple());
        plate.add(new RedApple());
        plate.add(new GreenApple());

        // error
//        plate.add(new Fruit());
//        plate.add(new Food());
    }

    /**
     * Producer Extends，Consumer Super，也就是说，如果一个参数类型是生产者的话，我们将采用? extends T上界，
     * 如果一个参数类型是消费者的话，那么就采用的是? super T下界
     * @param <E>
     */
    class MyStack<E> extends Stack<E> {
        public void pushAll(List<? extends E> fruits) {
            for (E item : fruits) {
                push(item);
            }
        }

        public void popAll(List<? super E> fruits) {
            while (!isEmpty()) {
                fruits.add(pop());
            }
        }
    }
    private void testArgs() {
        MyStack<Fruit> t = new MyStack<>();
        List<Apple> apples = new ArrayList<>();
        apples.add(new Apple());
        apples.add(new Apple());
        // ok!!
        apples.add(new GreenApple());

        // apples 作为生产者
        t.pushAll(apples);
        // error
        // t.popAll(apples);

        // f 作为
        List<Food> f = new ArrayList<>();
        t.popAll(f);
    }
    @Override
    public void run() {
        Log.i(TAG, "LearnGeneric");
        upperLimit();
        lowerLimit();
    }
}
