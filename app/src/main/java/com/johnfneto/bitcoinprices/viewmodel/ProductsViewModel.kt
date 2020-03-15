package com.johnfneto.bitcoinprices.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.appContext
import com.johnfneto.bitcoinprices.models.BitcoinModel
import com.johnfneto.bitcoinprices.models.Products
import com.johnfneto.bitcoinprices.services.ProductsApi
import com.johnfneto.bitcoinprices.services.Service
import com.johnfneto.bitcoinprices.ui.POOL_FREQUENCY
import com.johnfneto.bitcoinprices.utils.Utils
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
    private var currencyToWatch: String? = null
    private var latestSellPrice = 0.0
    private var latestBuyPrice = 0.0

    private val productsApi: ProductsApi = Service().createService(ProductsApi::class.java)

    init {
        startTimer()
    }

    fun getProductsList() {
        if (Utils.isInternetAvailable(appContext)) {
            viewModelScope.launch {   // Dispatchers.Main
                val response: Response<Products>? = getProductsFromServer()
                response?.let {
                    if (response.isSuccessful) {
                        val products: Products? = response.body()
                        ProductsRepository.products.postValue(products!!)
                        currencyToWatch?.let {
                            isPriceChanged(
                                products.productsList.find {
                                    it?.currency == currencyToWatch
                                })
                        }
                    } else {
                        ProductsRepository.errorStatus.postValue(true)
                    }
                }
            }
        } else {
            Toast.makeText(appContext, appContext.resources.getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
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

    private fun isPriceChanged(updatedProduct: BitcoinModel?) {
        if (latestBuyPrice != 0.0) {
            if (updatedProduct?.buy != latestBuyPrice) {
                ProductsRepository.flashPrice.postValue(true)
            }
        } else if (latestSellPrice != 0.0) {
            if (updatedProduct?.sell != latestSellPrice) {
                ProductsRepository.flashPrice.postValue(true)
            }
        }

        latestBuyPrice = updatedProduct?.buy!!
        latestSellPrice = updatedProduct.sell
    }

    fun watchCurrency(currency: String?) {
        currencyToWatch = currency
    }
}