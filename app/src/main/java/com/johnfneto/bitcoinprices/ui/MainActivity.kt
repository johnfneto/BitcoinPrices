package com.johnfneto.bitcoinprices.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.johnfneto.bitcoinprices.R
import com.johnfneto.bitcoinprices.utils.Utils
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
        if (Utils.isInternetAvailable(this)) {
            viewModel.getProductsList()
        } else {
            Toast.makeText(this, resources.getString(R.string.no_internet), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun navSetup() {
        setSupportActionBar(toolbar)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return (Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp())
    }
}