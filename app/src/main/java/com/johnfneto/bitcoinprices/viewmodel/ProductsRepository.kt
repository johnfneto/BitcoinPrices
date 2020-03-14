package com.johnfneto.bitcoinprices.viewmodel

import androidx.lifecycle.MutableLiveData
import com.johnfneto.bitcoinprices.models.ProductsList
import com.johnfneto.bitcoinprices.services.ProductsApi
import retrofit2.Response

object ProductsRepository {

    var productsList = MutableLiveData<ProductsList>()

    var errorStatus: MutableLiveData<Boolean> = MutableLiveData()
}