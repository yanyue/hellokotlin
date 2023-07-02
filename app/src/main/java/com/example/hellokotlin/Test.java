package com.example.hellokotlin;

import android.util.Log;
import com.example.apt_annotation.ExecutorTest;
import com.example.apt_annotation.Learning;

@ExecutorTest
public class Test implements Learning {

    private static final String TAG = "Test";

    @Override
    public void run() {
        Log.i(TAG, "test");
    }
}
