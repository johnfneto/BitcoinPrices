package com.johnfneto.bitcoinprices.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.databinding.FragmentProductBinding
import com.johnfneto.bitcoinprices.models.BitcoinModel
import com.johnfneto.bitcoinprices.utils.TradeType
import com.johnfneto.bitcoinprices.utils.Utils
import com.johnfneto.bitcoinprices.utils.Utils.textFlash
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

        confirmButton.isEnabled = false
    }

    private fun setupDataObserver() {
        ProductsRepository.products.observe(viewLifecycleOwner, Observer { products ->
            products.productsList.find { country ->
                country!!.currency == productCurrency
            }?.let { updatedProduct ->
                product = updatedProduct
                binding.product = updatedProduct
                latestBuyPrice = updatedProduct.buy
                latestSellPrice = updatedProduct.sell
            }
        })

        ProductsRepository.flashPrice.observe(viewLifecycleOwner, Observer { flashPrice ->
            if (flashPrice) {
                textFlash(requireContext(), binding.buyPrice)
                textFlash(requireContext(), binding.sellPrice)
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
                if (editUnits.text.isNotEmpty()) {
                    when (operation) {
                        TradeType.SELL -> editAmount.setText(Utils.formatPrice(editUnits.text.toString().toDouble() * product.sell))
                        TradeType.BUY -> editAmount.setText(Utils.formatPrice(editUnits.text.toString().toDouble() * product.buy))
                    }
                }
            }
        }
    }

    private fun setTitle(title: String) {
        (requireActivity() as MainActivity).supportActionBar?.title = title
    }
}