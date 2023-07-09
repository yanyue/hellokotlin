package com.example.hellokotlin.kk

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

fun AppCompatActivity.launch(action: () -> Unit) {
    this.lifecycleScope.launch {
        action()
    }
}