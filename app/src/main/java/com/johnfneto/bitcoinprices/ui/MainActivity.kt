package com.johnfneto.bitcoinprices.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.utils.DataProvider
import com.johnfneto.bitcoinprices.utils.Utils
import com.johnfneto.bitcoinprices.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val POOL_FREQUENCY = 5000L

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    private val timer =  Timer()
    lateinit var viewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        DataProvider.errorStatus.observe(this, Observer { error ->
            if (error) {
                Toast.makeText(this,resources.getString(R.string.error_message), Toast.LENGTH_SHORT).show()
            }
        })

        timer.scheduleAtFixedRate(
            object : TimerTask() {
                override fun run() {
                    refreshProductsList()
                }
            },
            0, POOL_FREQUENCY
        )
    }

    fun refreshProductsList() {
        if (Utils.isInternetAvailable(this)) {
            viewModel.getProductsList()
        }
        else {
            Toast.makeText(this,resources.getString(R.string.no_internet), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}