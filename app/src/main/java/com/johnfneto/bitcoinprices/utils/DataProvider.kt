package com.johnfneto.bitcoinprices.utils

import androidx.lifecycle.MutableLiveData
import com.johnfneto.bitcoinprices.models.ProductModel

object DataProvider {

    var productsList = MutableLiveData<List<ProductModel>>()

    var errorStatus: MutableLiveData<Boolean> = MutableLiveData()
}