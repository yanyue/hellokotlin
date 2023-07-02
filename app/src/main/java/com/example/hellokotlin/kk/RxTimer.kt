package com.example.hellokotlin.kk

import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RxTimer {

    private var job: Job? = null
    companion object {
        private const val TAG = "RxTimer"
        private val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "CoroutineExceptionHandler got $exception")
        }
        private var mScope: CoroutineScope = CoroutineScope(Dispatchers.Default + SupervisorJob() + handler)

        @JvmStatic
        fun init(scope: CoroutineScope) {
            mScope.cancel()
            mScope = scope
        }
    }

    fun time(time: Long, action: () -> Unit) {
        mScope.launch {
            Log.i(TAG, "counting down: $time")
            delay(time)
            withContext(Dispatchers.Main) {
                action()
            }
        }
    }

    fun cancel() {
        Log.i(TAG, "cancel: $job")
        job?.cancel()
    }
}