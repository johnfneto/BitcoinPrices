package com.johnfneto.bitcoinprices.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.utils.KeyboardToggleListener
import com.johnfneto.bitcoinprices.viewmodel.ProductsRepository
import com.johnfneto.bitcoinprices.viewmodel.ProductsViewModel
import kotlinx.android.synthetic.main.activity_main.*


const val POOL_FREQUENCY = 15000L

class MainActivity : AppCompatActivity() {
    private val TAG = javaClass.simpleName

    lateinit var viewModel: ProductsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navSetup()

        viewModel = ViewModelProvider(this).get(ProductsViewModel::class.java)
        ProductsRepository.errorStatus.observe(this, Observer { error ->
            if (error) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.error_message),
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    fun refreshProductsList() {
        viewModel.getProductsList()
    }

    private fun navSetup() {
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            if (destination.label.toString() == resources.getString(R.string.bitcoin_trade)) {
                arguments?.let {
                    viewModel.watchCurrency(arguments.getString("product"))
                }
            }

        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp())
    }

    fun addKeyboardToggleListener(onKeyboardToggleAction: (shown: Boolean) -> Unit): KeyboardToggleListener? {
        val root = findViewById<View>(android.R.id.content)
        val listener = KeyboardToggleListener(root, onKeyboardToggleAction)
        return root?.viewTreeObserver?.run {
            addOnGlobalLayoutListener(listener)
            listener
        }
    }
}