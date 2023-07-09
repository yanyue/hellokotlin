package com.example.hellokotlin.kk

import android.util.Log
import com.example.apt_annotation.ExecutorTest
import com.example.apt_annotation.Learning

@ExecutorTest
class TestTimer: Learning {
    companion object {
        private const val TAG = "TestTimer"
    }

    override fun run() {
        RxTimer().time(1000) {
            Log.i(TAG, "TestTimer, timerout")
        }
    }
}