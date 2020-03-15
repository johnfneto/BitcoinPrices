package com.johnfneto.bitcoinprices.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.appContext
import com.johnfneto.bitcoinprices.models.Products
import com.johnfneto.bitcoinprices.ui.FLASH_DURATION
import com.johnfneto.bitcoinprices.ui.FLASH_OFFSET
import com.johnfneto.bitcoinprices.ui.FLASH_REPEAT_COUNT
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response

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

    fun buildErrorResponse(message: String?): Response<Products> = Response.error(
        400,
        "{\"key\":[$message]}"
            .toResponseBody("application/json".toMediaTypeOrNull())
    )

    fun textFlash(context: Context, textView: TextView) {
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FLASH_DURATION
        anim.startOffset = FLASH_OFFSET
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = FLASH_REPEAT_COUNT
        textView.setTextColor(context.resources.getColor(R.color.flashGreenColor, null))
        textView.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                textView.setTextColor(context.resources.getColor(R.color.textGreyColor, null))
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
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
    fun formatAmountLabel(symbol: String) = appContext.getString(R.string.amount_currency, symbol)

    @JvmStatic
    fun formatPrice(price: Double) = String.format("%.2f", price)

    @JvmStatic
    fun formatUnits(price: Double) = String.format("%.4f", price)

    @JvmStatic
    fun formatLevel1Text(): Spanned =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(appContext.getString(R.string.level1_label), Html.FROM_HTML_MODE_COMPACT)
        } else {
                Html.fromHtml(appContext.getString(R.string.level1_label))
        }
}