package com.johnfneto.bitcoinprices.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.johnfneto.bitcoinprices.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }
}