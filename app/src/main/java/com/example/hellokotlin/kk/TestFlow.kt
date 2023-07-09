package com.example.hellokotlin.kk

import android.util.Log
import com.example.apt_annotation.ExecutorTest
import com.example.apt_annotation.Learning
import kotlinx.coroutines.channels.ticker
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull

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

        runBlocking<Unit> {
            val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0) //创建计时器通道
            var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
            println("Initial element is available immediately: $nextElement") // no initial delay

            nextElement = withTimeoutOrNull(50) { tickerChannel.receive() } // all subsequent elements have 100ms delay
            println("Next element is not ready in 50 ms: $nextElement")

            nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
            println("Next element is ready in 100 ms: $nextElement")

            // 模拟大量消费延迟
            println("Consumer pauses for 150ms")
            delay(150)
            // 下一个元素立即可用
            nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
            println("Next element is available immediately after large consumer delay: $nextElement")
            // 请注意，`receive` 调用之间的暂停被考虑在内，下一个元素的到达速度更快
            nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
            println("Next element is ready in 50ms after consumer pause in 150ms: $nextElement")

            tickerChannel.cancel() // 表明不再需要更多的元素
        }
    }
}