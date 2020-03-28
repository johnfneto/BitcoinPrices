package com.johnfneto.bitcoinprices.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.databinding.ItemListProductBinding
import com.johnfneto.bitcoinprices.models.BitcoinModel
import com.johnfneto.bitcoinprices.utils.TradeType

class ProductsAdapter(
    private var productsList: MutableList<BitcoinModel?>,
    private val callback: (BitcoinModel?, TradeType) -> Unit
)
    : RecyclerView.Adapter<ProductsAdapter.DataBindingViewHolder>() {
    private val TAG = javaClass.simpleName

    private lateinit var binding: ItemListProductBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBindingViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        binding = DataBindingUtil.inflate(inflater, R.layout.item_list_product, parent, false)
        return DataBindingViewHolder(binding)
    }

    override fun getItemCount() = productsList.size

    override fun onBindViewHolder(holder: DataBindingViewHolder, position: Int) {
        val item = productsList[position]
        holder.bind(item)
        with(holder.itemView) {
            tag = position
        }
    }

    inner class DataBindingViewHolder(private val binding: ItemListProductBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BitcoinModel?) {
            binding.sellButton.tag = this
            binding.buyButton.tag = this
            binding.apply {
                product = item

                sellButton.setOnClickListener {
                    callback(item, TradeType.SELL)
                }
                buyButton.setOnClickListener {
                    callback(item, TradeType.BUY)
                }
            }
        }
    }
}