package com.johnfneto.bitcoinprices.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BitcoinModel (

    @Json(name = "15m") val _15m : Double,
    @Json(name = "last") val last : Double,
    @Json(name = "buy") val buy : Double,
    @Json(name = "sell") val sell : Double,
    @Json(name = "symbol") val symbol : String,
    @Transient var currency: String = ""
)