package com.johnfneto.bitcoinprices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.bitcoinprices.models.ProductsList
import com.johnfneto.bitcoinprices.services.ProductsApi
import com.johnfneto.bitcoinprices.services.Service
import com.johnfneto.bitcoinprices.ui.POOL_FREQUENCY
import com.johnfneto.bitcoinprices.utils.Utils.buildErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.io.IOException
import java.util.*

class ProductsViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val timer = Timer()

    private val productsApi: ProductsApi = Service().createService(ProductsApi::class.java)

    init {
        startTimer()
    }

    fun getProductsList() {
        viewModelScope.launch {   // Dispatchers.Main
            val response: Response<ProductsList>? = getProductsFromServer()
            response?.let {
                if (response.isSuccessful) {
                    ProductsRepository.productsList.postValue(response.body()!!)
                } else {
                    ProductsRepository.errorStatus.postValue(true)
                }
            }
        }
    }

    private suspend fun getProductsFromServer() = withContext(Dispatchers.IO) {
        try {
            productsApi.getProductsListAsync()
        } catch (e: IOException) {
            Log.e(TAG, "Error on calling getProductsListAsync(): ${e.message}")
            buildErrorResponse(e.message)
        }
    }

    private fun startTimer() {
        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    getProductsList()
                }
            },
            0, POOL_FREQUENCY
        )
    }

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
}