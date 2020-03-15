package com.johnfneto.bitcoinprices.models

import com.johnfneto.bitcoinprices.utils.SingleToArray
import com.squareup.moshi.Json

data class Products(
    @SingleToArray
    @Json(name = "USD")
    val uSD: BitcoinModel,
    @Json(name = "AUD")
    val aUD: BitcoinModel,
    @Json(name = "BRL")
    val bRL: BitcoinModel,
    @Json(name = "CAD")
    val cAD: BitcoinModel,
    @Json(name = "CHF")
    val cHF: BitcoinModel,
    @Json(name = "CLP")
    val cLP: BitcoinModel,
    @Json(name = "CNY")
    val cNY: BitcoinModel,
    @Json(name = "DKK")
    val dKK: BitcoinModel,
    @Json(name = "EUR")
    val eUR: BitcoinModel,
    @Json(name = "GBP")
    val gBP: BitcoinModel,
    @Json(name = "HKD")
    val hKD: BitcoinModel,
    @Json(name = "INR")
    val iNR: BitcoinModel,
    @Json(name = "ISK")
    val iSK: BitcoinModel,
    @Json(name = "JPY")
    val jPY: BitcoinModel,
    @Json(name = "KRW")
    val kRW: BitcoinModel,
    @Json(name = "NZD")
    val nZD: BitcoinModel,
    @Json(name = "PLN")
    val pLN: BitcoinModel,
    @Json(name = "RUB")
    val rUB: BitcoinModel,
    @Json(name = "SEK")
    val sEK: BitcoinModel,
    @Json(name = "SGD")
    val sGD: BitcoinModel,
    @Json(name = "THB")
    val tHB: BitcoinModel,
    @Json(name = "TWD")
    val tWD: BitcoinModel
) {
    private val currencyUSD = uSD.also {
        it.currency = "USD"
    }
    private val currencyAUD = aUD.also {
        it.currency = "AUD"
    }
    private val currencyBRL = bRL.also {
        it.currency = "BRL"
    }
    private val currencyCAD = cAD.also {
        it.currency = "CAD"
    }
    private val currencyCHF = cHF.also {
        it.currency = "CHF"
    }
    private val currencyCLP = cLP.also {
        it.currency = "CLP"
    }
    private val currencyCNY = cNY.also {
        it.currency = "CNY"
    }
    private val currencyDKK = dKK.also {
        it.currency = "DKK"
    }
    private val currencyEUR = eUR.also {
        it.currency = "EUR"
    }
    private val currencyGBP = gBP.also {
        it.currency = "GBP"
    }
    private val currencyHKD = hKD.also {
        it.currency = "HKD"
    }
    private val currencyINR = iNR.also {
        it.currency = "INR"
    }
    private val currencyISK = iSK.also {
        it.currency = "ISK"
    }
    private val currencyJPY = jPY.also {
        it.currency = "JPY"
    }
    private val currencyKRW = kRW.also {
        it.currency = "KRW"
    }
    private val currencyNZD = nZD.also {
        it.currency = "NZD"
    }
    private val currencyPLN = pLN.also {
        it.currency = "PLN"
    }
    private val currencyRUB = rUB.also {
        it.currency = "RUB"
    }
    private val currencySEK = sEK.also {
        it.currency = "SEK"
    }
    private val currencySGD = sGD.also {
        it.currency = "SGD"
    }
    private val currencyTHB = tHB.also {
        it.currency = "THB"
    }
    private val currencyTWD = tWD.also {
        it.currency = "TWD"
    }

    val productsList: MutableList<BitcoinModel?> =
        mutableListOf(
            currencyUSD,
            currencyAUD,
            currencyBRL,
            currencyCAD,
            currencyCHF,
            currencyCLP,
            currencyCNY,
            currencyDKK,
            currencyEUR,
            currencyGBP,
            currencyHKD,
            currencyINR,
            currencyISK,
            currencyJPY,
            currencyKRW,
            currencyNZD,
            currencyPLN,
            currencyRUB,
            currencySEK,
            currencySGD,
            currencyTHB,
            currencyTWD
        )
}