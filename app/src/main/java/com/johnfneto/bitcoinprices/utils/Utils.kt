package com.johnfneto.bitcoinprices.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan

object Utils {

    fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        networkCapabilities.apply {
            return when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    }

    @JvmStatic
    fun formatPriceSize(price: Double): SpannableString {
        val value = formatPrice(price)
        val text = SpannableString(value)
        text.setSpan(
            RelativeSizeSpan(1f), 0, value.indexOf("."),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        text.setSpan(
            RelativeSizeSpan(0.8f), value.indexOf(".") + 1, value.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return text
    }

    @JvmStatic
    fun calculateSpread(buyPrice: Double, sellPrice: Double) =
        String.format("%.2f", (buyPrice - sellPrice))

    @JvmStatic
    fun formatPrice(price: Double) = String.format("%.2f", price)

    @JvmStatic
    fun formatUnits(price: Double) = String.format("%.4f", price)
}