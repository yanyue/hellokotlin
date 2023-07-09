package com.example.hellokotlin.kk

import com.example.apt_annotation.ExecutorTest
import com.example.apt_annotation.Learning
import kotlin.concurrent.thread

@ExecutorTest
class TestInline : Learning {

    inline fun assWeCan(block: () -> Unit) {
        // codes
    }

    fun boyNextDoor(block: () -> Unit) {
        // codes
    }

    override fun run() {
//        boyNextDoor {
//            // 这种写法是不能通过编译的，因为你不能从一个函数跳出另一个函数。你必须使用 return@assWeCan 或者 return@foo 这种写法。
//            return
//        }
        assWeCan {
            // 这种写法可以通过编译，因为你inline了之后就是直接从外部退出了。
            return
        }
    }

    inline fun test(noinline f: () -> Unit) {
        thread(block = f)
    }
}