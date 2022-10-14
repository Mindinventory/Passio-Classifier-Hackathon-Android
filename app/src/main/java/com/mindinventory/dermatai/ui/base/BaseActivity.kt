package com.mindinventory.dermatai.ui.base

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkRequest
import android.os.Bundle
import android.view.LayoutInflater
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    private var noInternetDialog: Dialog? = null
    private lateinit var mToolbar: Toolbar

    private val connectivityManager: ConnectivityManager by lazy {
        getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkRequestBuilder: NetworkRequest.Builder by lazy {
        NetworkRequest.Builder()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateLayout(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    @CallSuper
    protected open fun initViews() {

    }

    abstract fun inflateLayout(layoutInflater: LayoutInflater): VB

}