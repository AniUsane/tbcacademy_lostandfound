package com.example.lostfound.presentation.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB: ViewBinding>(
    private val inflater: (LayoutInflater, ViewGroup?, Boolean) -> VB):
    Fragment(){
    private var _binding: VB? = null
    val binding get() = _binding!!

    abstract fun start()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflater(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //function for language change events. It refreshes fragment to apply new language
    private val languageChangeReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            parentFragmentManager.beginTransaction()
                .detach(this@BaseFragment)
                .commitAllowingStateLoss()

            parentFragmentManager.beginTransaction()
                .attach(this@BaseFragment)
                .commitAllowingStateLoss()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(languageChangeReceiver, IntentFilter("LANGUAGE_CHANGED"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(requireContext())
            .unregisterReceiver(languageChangeReceiver)
    }
}