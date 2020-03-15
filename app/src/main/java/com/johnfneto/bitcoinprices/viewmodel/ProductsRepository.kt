package com.johnfneto.bitcoinprices.viewmodel

import androidx.lifecycle.MutableLiveData
import com.johnfneto.bitcoinprices.models.Products

object ProductsRepository {

    var products = MutableLiveData<Products>()

    var flashPrice: MutableLiveData<Boolean> = MutableLiveData()

    var errorStatus: MutableLiveData<Boolean> = MutableLiveData()
}