package com.johnfneto.bitcoinprices

import android.app.Activity
import android.app.Application
import android.content.Context

lateinit var appContext: Context

class BitcoinApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}