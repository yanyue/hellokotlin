package com.example.hellokotlin.learnjava;

import android.util.Log;
import com.example.apt_annotation.ExecutorTest;
import com.example.apt_annotation.Learning;
import java.util.ArrayList;
import java.util.List;

@ExecutorTest
public class LearnGeneric implements Learning {

    private static final String TAG = "LearnGeneric";

    @Override
    public void run() {
        Log.i(TAG, "LearnGeneric");
        test(new ArrayList<Integer>(), new ArrayList<Double>());
    }

    public void test(List<? extends Number> dest, List<? extends Number> src) {

    }

    interface A { }
    interface B  { }
    class MultipleLimit<T extends A & B> {

    }
}
