package com.johnfneto.bitcoinprices.services

import com.johnfneto.bitcoinprices.models.ProductsList
import retrofit2.Response
import retrofit2.http.GET

interface ProductsApi {

    @GET("ticker")
    suspend fun getProductsListAsync(): Response<ProductsList>
}