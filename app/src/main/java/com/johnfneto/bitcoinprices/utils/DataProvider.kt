package com.johnfneto.bitcoinprices.utils

import androidx.lifecycle.MutableLiveData
import com.johnfneto.bitcoinprices.models.BitcoinModel
import com.johnfneto.bitcoinprices.models.ProductsList

object DataProvider {

    var productsList = MutableLiveData<ProductsList>()

    var errorStatus: MutableLiveData<Boolean> = MutableLiveData()
}