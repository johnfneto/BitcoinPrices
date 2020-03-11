package com.johnfneto.bitcoinprices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.bitcoinprices.models.ProductsList
import com.johnfneto.bitcoinprices.services.ProductsApi
import com.johnfneto.bitcoinprices.services.Service
import com.johnfneto.bitcoinprices.utils.DataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ProductsViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val productsApi: ProductsApi = Service().createService(ProductsApi::class.java)

    fun getProductsList() {
        viewModelScope.launch {   // Dispatchers.Main
            val response: Response<ProductsList> = getProductsFromServer()
            if (response.isSuccessful) {
                DataProvider.productsList.postValue(response.body()!!)
            } else {
                DataProvider.errorStatus.postValue(true)
            }
        }
    }

    private suspend fun getProductsFromServer() = withContext(Dispatchers.IO) {
        productsApi.getProductsListAsync()
    }
}