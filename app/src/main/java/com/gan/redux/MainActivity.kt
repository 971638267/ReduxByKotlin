package com.gan.redux

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val redux=Test()
        findViewById<View>(R.id.tv).setOnClickListener {
            redux.test()
        }
    }
}