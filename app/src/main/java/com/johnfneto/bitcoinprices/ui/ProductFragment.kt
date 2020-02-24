package com.johnfneto.bitcoinprices.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.databinding.FragmentProductBinding

class ProductFragment : Fragment() {
    private val TAG = javaClass.simpleName

    private val args: ProductFragmentArgs by navArgs()

    private lateinit var binding : FragmentProductBinding

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

        val product = args.product
        val operation = args.operation

        setTitle(product)

        Log.d(TAG, "product = $product")
        Log.d(TAG, "operation = ${operation.name}")
    }

    private fun setTitle(title: String) {
        (requireActivity() as MainActivity).supportActionBar?.title = title
    }
}