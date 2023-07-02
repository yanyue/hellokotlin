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
import java.lang.ref.WeakReference

class RxTimer {

    private var job: Job? = null
    companion object {
        private const val TAG = "RxTimer"
        private val handler = CoroutineExceptionHandler { _, exception ->
            Log.e(TAG, "CoroutineExceptionHandler got $exception")
        }
        private var mScope: WeakReference<CoroutineScope> = WeakReference(null)

        @JvmStatic
        fun init(scope: CoroutineScope) {
            mScope = WeakReference(scope)
        }
    }

    fun time(time: Long,
             action: () -> Unit = {},
             h: CoroutineExceptionHandler = handler): RxTimer {
        mScope.get()?.launch(Dispatchers.Default + SupervisorJob() + h) {
            Log.i(TAG, "counting down: $time, id=${this.hashCode()}")
            delay(time)
            withContext(Dispatchers.Main) {
                action()
            }
        }
        return this
    }

    fun cancel() {
        Log.i(TAG, "cancel: $job")
        job?.cancel()
    }
}