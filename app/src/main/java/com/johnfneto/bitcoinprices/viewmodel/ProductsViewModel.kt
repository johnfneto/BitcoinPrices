package com.johnfneto.bitcoinprices.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.johnfneto.bitcoinprices.models.ProductModel
import com.johnfneto.bitcoinprices.models.ProductsListModel
import com.johnfneto.bitcoinprices.services.ProductsApi
import com.johnfneto.bitcoinprices.services.Service
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response
import java.util.*
import kotlin.reflect.full.memberProperties

class ProductsViewModel : ViewModel() {
    private val TAG = javaClass.simpleName

    private val productsApi: ProductsApi = Service().createService(ProductsApi::class.java)

    fun callApiMethod() {
        viewModelScope.launch {   // Dispatchers.Main
            val response : Response<ProductsListModel> = getProductsFromServer()
            if (response.isSuccessful) {
                processResponse(response.body()!!)
            }
            else {
                Log.d(TAG, "error ${response.errorBody()}")
            }
        }
    }

    private suspend fun getProductsFromServer() = withContext(Dispatchers.IO) {
        productsApi.getProductsListAsync()
    }

    private fun processResponse(productsListModel: ProductsListModel) {
        val productsList = mutableListOf<ProductModel>()
        val baseClass: Class<*> = ProductsListModel::class.java
        val products = baseClass.declaredFields

        products.forEach { product ->
            val item = productsListModel.getField<ProductModel>(product.name)!!
            item.currency = product.name.toUpperCase(Locale.ROOT) + " - " + item.symbol
            productsList.add(item)
            Log.d(TAG, "product = ${item.currency}")
        }
    }

    @Throws(IllegalAccessException::class, ClassCastException::class)
    inline fun <reified T> Any.getField(fieldName: String): T? {
        this::class.memberProperties.forEach { kCallable ->
            if (fieldName == kCallable.name) {
                return kCallable.getter.call(this) as T?
            }
        }
        return null
    }
}