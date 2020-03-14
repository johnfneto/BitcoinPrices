package com.johnfneto.bitcoinprices.ui

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.databinding.FragmentProductBinding
import com.johnfneto.bitcoinprices.models.BitcoinModel
import com.johnfneto.bitcoinprices.utils.KeyboardToggleListener
import com.johnfneto.bitcoinprices.utils.TradeType
import com.johnfneto.bitcoinprices.utils.Utils
import com.johnfneto.bitcoinprices.viewmodel.ProductsRepository
import kotlinx.android.synthetic.main.fragment_product.*

const val FLASH_DURATION = 50L
const val FLASH_OFFSET = 20L
const val FLASH_REPEAT_COUNT = 10

class ProductFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private val args: ProductFragmentArgs by navArgs()

    private lateinit var binding: FragmentProductBinding
    private var operation = TradeType.SELL
    private lateinit var product: BitcoinModel
    private lateinit var productCurrency: String
    private var latestSellPrice = 0.0
    private var latestBuyPrice = 0.0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        productCurrency = args.product
        operation = args.operation
        binding.operation = operation

        setTitle(productCurrency)
        setupDataObserver()
        addFocusChangeListener()
        addKeyboardListener()

        sellLayout.setOnClickListener {
            binding.operation = TradeType.SELL
            operation = TradeType.SELL
        }

        buyLayout.setOnClickListener {
            binding.operation = TradeType.BUY
            operation = TradeType.BUY
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            level1_label.text =
                Html.fromHtml(getString(R.string.level1_label), Html.FROM_HTML_MODE_COMPACT)
        } else {
            level1_label.text =
                Html.fromHtml(getString(R.string.level1_label))
        }

        confirmButton.isEnabled = false
    }

    private fun setupDataObserver() {
        ProductsRepository.productsList.observe(viewLifecycleOwner, Observer { productsList ->
            productsList.productsList.find { country ->
                country!!.currency == productCurrency
            }?.let { updatedProduct ->
                product = updatedProduct
                binding.product = updatedProduct
                flashPrices(updatedProduct)
                latestBuyPrice = updatedProduct.buy
                latestSellPrice = updatedProduct.sell
            }
        })
    }

    private fun addFocusChangeListener() {
        val onFocusChangeListener = View.OnFocusChangeListener { view, hasFocus ->
            if (hasFocus) {
                view.setBackgroundResource(R.drawable.edittext_border)
                calculate(view)
            } else {
                view.setBackgroundResource(R.drawable.edittext_no_border)
            }
        }

        editUnits.onFocusChangeListener = onFocusChangeListener
        editAmount.onFocusChangeListener = onFocusChangeListener
    }

    private fun addKeyboardListener() {
        (activity as MainActivity).addKeyboardToggleListener { shown ->
            if (!shown) {
                if (editUnits != null && editAmount != null) {
                    if (editUnits.text.isNotEmpty() && editAmount.text.isNotEmpty()) {
                        confirmButton.isEnabled = true
                    }
                }
            }
        }
    }

    private fun calculate(view: View?) {
        when (view) {
            editUnits -> {
                if (editAmount.text.isNotEmpty()) {
                    when (operation) {
                        TradeType.SELL -> editUnits.setText(Utils.formatUnits(editAmount.text.toString().toDouble() / product.sell))
                        TradeType.BUY -> editUnits.setText(Utils.formatUnits(editAmount.text.toString().toDouble() / product.buy))
                    }
                }
            }
            editAmount -> {
                when (operation) {
                    TradeType.SELL -> editAmount.setText(Utils.formatPrice(editUnits.text.toString().toDouble() * product.sell))
                    TradeType.BUY -> editAmount.setText(Utils.formatPrice(editUnits.text.toString().toDouble() * product.buy))
                }
            }
        }
    }

    private fun flashPrices(updatedProduct: BitcoinModel) {
        if (latestBuyPrice != 0.0) {
            if (updatedProduct.buy != latestBuyPrice) {
                textFlash(binding.buyPrice)
            }
        }

        if (latestSellPrice != 0.0) {
            if (updatedProduct.sell != latestSellPrice) {
                textFlash(binding.sellPrice)
            }
        }
    }

    private fun textFlash(textView: TextView) {
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = FLASH_DURATION
        anim.startOffset = FLASH_OFFSET
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = FLASH_REPEAT_COUNT
        textView.setTextColor(resources.getColor(R.color.flashGreenColor, null))
        textView.startAnimation(anim)
        anim.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                textView.setTextColor(resources.getColor(R.color.textGreyColor, null))
            }

            override fun onAnimationStart(p0: Animation?) {}
        })
    }

    private fun setTitle(title: String) {
        (requireActivity() as MainActivity).supportActionBar?.title = title
    }

    private fun Activity.addKeyboardToggleListener(onKeyboardToggleAction: (shown: Boolean) -> Unit): KeyboardToggleListener? {
        val root = findViewById<View>(android.R.id.content)
        val listener = KeyboardToggleListener(root, onKeyboardToggleAction)
        return root?.viewTreeObserver?.run {
            addOnGlobalLayoutListener(listener)
            listener
        }
    }
}