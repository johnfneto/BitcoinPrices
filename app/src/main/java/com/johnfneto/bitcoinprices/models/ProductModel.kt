package com.johnfneto.bitcoinprices.models

import com.google.gson.annotations.SerializedName

class ProductModel (

    @SerializedName("15m") val _15m : Double,
    @SerializedName("last") val last : Double,
    @SerializedName("buy") val buy : Double,
    @SerializedName("sell") val sell : Double,
    @SerializedName("symbol") val symbol : String,
    var currency : String
)