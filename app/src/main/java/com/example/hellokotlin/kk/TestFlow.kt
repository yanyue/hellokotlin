package com.example.hellokotlin.kk

import android.util.Log
import com.example.apt_annotation.ExecutorTest
import com.example.apt_annotation.Learning
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@ExecutorTest
class TestFlow : Learning {
    companion object {
        private const val TAG = "TestFlow"
    }
    override fun run() {
        Log.d(TAG, "run")

        runBlocking<Unit> {

            (1..5).asFlow()
                .filter {
                    Log.d(TAG, "Filter $it")
                    it % 2 == 0
                }
                .map {
                    Log.d(TAG, "Map $it")
                    "string $it"
                }.collect {
                    Log.d(TAG, "Collect $it")
                }
        }
    }
}