package com.example.hellokotlin.learnjava;

import android.util.Log;
import com.example.apt_annotation.ExecutorTest;
import com.example.apt_annotation.Learning;

@ExecutorTest
public class LearnGeneric implements Learning {

    private static final String TAG = "LearnGeneric";

    @Override
    public void run() {
        Log.i(TAG, "LearnGeneric");
    }
}
