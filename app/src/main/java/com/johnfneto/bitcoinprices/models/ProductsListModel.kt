package com.johnfneto.bitcoinprices.models

import com.google.gson.annotations.SerializedName

class ProductsListModel (

    @SerializedName("USD") val uSD : ProductModel,
    @SerializedName("AUD") val aUD : ProductModel,
    @SerializedName("BRL") val bRL : ProductModel,
    @SerializedName("CAD") val cAD : ProductModel,
    @SerializedName("CHF") val cHF : ProductModel,
    @SerializedName("CLP") val cLP : ProductModel,
    @SerializedName("CNY") val cNY : ProductModel,
    @SerializedName("DKK") val dKK : ProductModel,
    @SerializedName("EUR") val eUR : ProductModel,
    @SerializedName("GBP") val gBP : ProductModel,
    @SerializedName("HKD") val hKD : ProductModel,
    @SerializedName("INR") val iNR : ProductModel,
    @SerializedName("ISK") val iSK : ProductModel,
    @SerializedName("JPY") val jPY : ProductModel,
    @SerializedName("KRW") val kRW : ProductModel,
    @SerializedName("NZD") val nZD : ProductModel,
    @SerializedName("PLN") val pLN : ProductModel,
    @SerializedName("RUB") val rUB : ProductModel,
    @SerializedName("SEK") val sEK : ProductModel,
    @SerializedName("SGD") val sGD : ProductModel,
    @SerializedName("THB") val tHB : ProductModel,
    @SerializedName("TWD") val tWD : ProductModel
)