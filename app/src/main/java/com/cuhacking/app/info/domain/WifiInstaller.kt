package com.cuhacking.app.info.domain

import android.annotation.TargetApi
import android.content.ClipData
import android.content.ClipboardManager
import android.net.wifi.WifiConfiguration
import android.net.wifi.WifiManager
import android.net.wifi.WifiNetworkSpecifier
import android.net.wifi.WifiNetworkSuggestion
import android.os.Build
import com.cuhacking.app.info.data.WifiInfo
import javax.inject.Inject

class WifiInstaller @Inject constructor(
    private val wifiManager: WifiManager,
    private val clipboardManager: ClipboardManager
) {
    fun installWifi(wifiInfo: WifiInfo) {
        if (Build.VERSION.SDK_INT < 29) {
            saveNetwork(wifiInfo)
        } else {
            suggestNetwork(wifiInfo)
        }
    }

    // On Android versions before SDK 29 Wifi is installed directly
    @Suppress("DEPRECATION")
    @TargetApi(28)
    private fun saveNetwork(wifiInfo: WifiInfo): Boolean {
        val config = WifiConfiguration().apply {
            SSID = "\"${wifiInfo.ssid}\""
            preSharedKey = "\"${wifiInfo.password}\""
        }

        if (!wifiManager.isWifiEnabled) {
            wifiManager.isWifiEnabled = true
        }
        var success = false
        val netId = wifiManager.addNetwork(config)
        if (netId != -1) {
            wifiManager.enableNetwork(netId, true)
            success = true
        } else {
            val clip: ClipData = ClipData.newPlainText("wifi_password", wifiInfo.password)
            clipboardManager.setPrimaryClip(clip)
        }

        return success
    }

    @TargetApi(29)
    private fun suggestNetwork(wifiInfo: WifiInfo): Boolean {
        val suggestion = WifiNetworkSuggestion.Builder()
            .setSsid(wifiInfo.ssid)
            .setWpa2Passphrase(wifiInfo.password)
            .build()

        var status = wifiManager.addNetworkSuggestions(listOf(suggestion))

        if (status == WifiManager.STATUS_NETWORK_SUGGESTIONS_ERROR_ADD_DUPLICATE) {
            wifiManager.removeNetworkSuggestions(listOf(suggestion))
            status = wifiManager.addNetworkSuggestions(listOf(suggestion))
        }

        return status == WifiManager.STATUS_NETWORK_SUGGESTIONS_SUCCESS
    }
}