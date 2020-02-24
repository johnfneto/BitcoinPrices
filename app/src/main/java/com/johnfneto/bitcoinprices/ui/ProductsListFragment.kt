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
import com.johnfneto.bitcoinprices.models.ProductModel
import com.johnfneto.bitcoinprices.utils.DataProvider
import com.johnfneto.bitcoinprices.utils.TradeType
import kotlinx.android.synthetic.main.fragment_products_list.*

class ProductsListFragment : Fragment(R.layout.fragment_products_list),
    SwipeRefreshLayout.OnRefreshListener {
    private val TAG = javaClass.simpleName

    lateinit var productsAdapter: ProductsAdapter
    private var productsList = mutableListOf<ProductModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipeContainer.setOnRefreshListener(this)
        setupRecyclerView()

        DataProvider.productsList.observe(viewLifecycleOwner, Observer { liveProductsList ->
            DataProvider.productsList.value?.let {

                if (DataProvider.productsList.value != productsList) {
                    productsList.clear()
                    productsList.addAll(DataProvider.productsList.value!!)
                    productsAdapter.notifyDataSetChanged()
                }
                swipeContainer.isRefreshing = false
            }
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
            var product: ProductModel
            val viewHolder = view.tag as RecyclerView.ViewHolder
            val position = viewHolder.adapterPosition
            DataProvider.productsList.value?.let { productsList ->
                product = productsList[position]
                val action = ProductsListFragmentDirections.actionGotoProduct(
                    product.currency,
                    TradeType.BUY
                )
                findNavController().navigate(action)
            }
        }

    private val onSellClickListener =
        View.OnClickListener { view ->
            var product: ProductModel
            val viewHolder = view.tag as RecyclerView.ViewHolder
            val position = viewHolder.adapterPosition
            DataProvider.productsList.value?.let { productsList ->
                product = productsList[position]
                val action = ProductsListFragmentDirections.actionGotoProduct(
                    product.currency,
                    TradeType.SELL
                )
                findNavController().navigate(action)
            }
        }

    override fun onRefresh() {
        (activity as MainActivity).refreshProductsList()
    }
}