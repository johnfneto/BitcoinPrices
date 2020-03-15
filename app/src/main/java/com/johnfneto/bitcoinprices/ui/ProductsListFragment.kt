package com.johnfneto.bitcoinprices.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.models.BitcoinModel
import com.johnfneto.bitcoinprices.utils.TradeType
import com.johnfneto.bitcoinprices.viewmodel.ProductsRepository
import kotlinx.android.synthetic.main.fragment_products_list.*

class ProductsListFragment : Fragment(R.layout.fragment_products_list),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = javaClass.simpleName

    private lateinit var productsAdapter: ProductsAdapter
    private var productsList = mutableListOf<BitcoinModel?>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeContainer.setOnRefreshListener(this)
        setupRecyclerView()
        setupDataObserver()
    }

    private fun setupDataObserver() {
        ProductsRepository.products.observe(viewLifecycleOwner, Observer { liveProductsList ->

            if (liveProductsList.productsList != productsList) {
                productsList.clear()
                productsList.addAll(liveProductsList.productsList)
                productsAdapter.notifyDataSetChanged()
            }
            swipeContainer.isRefreshing = false
        })
    }

    private fun setupRecyclerView() {
        val itemDecorator =
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(requireContext().resources.getDrawable(R.drawable.divider, null))

        productsAdapter =
            ProductsAdapter(productsList, onBuyClickListener, onSellClickListener)
        productsRecyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = productsAdapter
            itemAnimator = DefaultItemAnimator()
            isNestedScrollingEnabled = true
            addItemDecoration(itemDecorator)
        }
    }

    private val onBuyClickListener =
        View.OnClickListener { view ->
            gotoProductsListFragment(view, TradeType.BUY)
        }

    private val onSellClickListener =
        View.OnClickListener { view ->
            gotoProductsListFragment(view, TradeType.SELL)
        }

    private fun gotoProductsListFragment(
        view: View,
        type: TradeType
    ) {
        var product: BitcoinModel
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val position = viewHolder.adapterPosition
        ProductsRepository.products.value?.let { productsList ->
            product = productsList.productsList[position]!!
            val action = ProductsListFragmentDirections.actionGotoProduct(
                product.currency,
                type
            )
            findNavController().navigate(action)
        }
    }

    override fun onRefresh() {
        (activity as MainActivity).refreshProductsList()
    }
}