package com.purushotham.apoontask.ui.help

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.purushotham.apoontask.R
import com.purushotham.apoontask.databinding.FragmentHelpBinding
import com.purushotham.apoontask.util.Progress

class HelpFragment : Fragment() {

    private var _binding: FragmentHelpBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var progress: Progress? = null
    private var isLoaded: Boolean = false
    private var doubleBackToExitPressedOnce = false
    private var webURL = "https://openweathermap.org/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHelpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        binding.webView.settings.javaScriptEnabled = true
        if (!isOnline()) {
            showToast(getString(R.string.no_internet))
            binding.infoTV.text = getString(R.string.no_internet)
            showNoNetSnackBar()
            return
        }

    }

    override fun onResume() {
        if (isOnline() && !isLoaded) loadWebView()
        super.onResume()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun loadWebView() {
        binding.infoTV.text = ""
        binding.webView.loadUrl(webURL)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                setProgressDialogVisibility(true)
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                isLoaded = true
                setProgressDialogVisibility(false)
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                isLoaded = false
                val errorMessage = "Got Error! $error"
                showToast(errorMessage)
                binding.infoTV.text = errorMessage
                setProgressDialogVisibility(false)
                super.onReceivedError(view, request, error)
            }
        }
    }

    /*override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (binding.webView.canGoBack()) {
                    binding.webView.goBack()
                } else {
                    showToastToExit()
                }
                return true
            }
        }
        return super.onKeyDown(keyCode, event)
    }*/

    private fun showToastToExit() {
        when {
            doubleBackToExitPressedOnce -> {
                //onBackPressed()
            }
            else -> {
                doubleBackToExitPressedOnce = true
                showToast(getString(R.string.back_again_to_exit))
                Handler(Looper.myLooper()!!).postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
            }
        }
    }

    private fun setProgressDialogVisibility(visible: Boolean) {
        if (visible) progress = Progress(requireContext(), R.string.please_wait, cancelable = true)
        progress?.apply { if (visible) show() else dismiss() }
    }

    private fun isOnline(): Boolean {
        val connectivityManager =
            requireActivity().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showNoNetSnackBar() {
        val snack = Snackbar.make(binding.clRoot, getString(R.string.no_internet), Snackbar.LENGTH_INDEFINITE)
        snack.setAction(getString(R.string.settings)) {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        snack.show()
    }
}