package com.siddiqui.reprodeck.data.device

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.BatteryManager
import android.os.Build
import android.os.PowerManager
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import java.util.Locale
import java.util.TimeZone

class AndroidDeviceSnapshotDataSource(context: Context) : DeviceSnapshotDataSource {
    private val appContext = context.applicationContext

    override fun capture(): DeviceSnapshotDraft {
        val resources = appContext.resources
        val metrics = resources.displayMetrics
        val configuration = resources.configuration
        val locale = if (configuration.locales.isEmpty) {
            Locale.getDefault()
        } else {
            configuration.locales[0]
        }

        val packageInfo = getPackageInfo()
        val batteryIntent = appContext.registerReceiver(
            null,
            IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        )
        val batteryLevel = batteryIntent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) ?: -1
        val batteryScale = batteryIntent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1) ?: -1
        val batteryPercent = if (batteryLevel >= 0 && batteryScale > 0) {
            ((batteryLevel * 100f) / batteryScale).toInt().coerceIn(0, 100)
        } else {
            null
        }
        val batteryStatus = batteryIntent?.getIntExtra(
            BatteryManager.EXTRA_STATUS,
            BatteryManager.BATTERY_STATUS_UNKNOWN
        )
        val isCharging = when (batteryStatus) {
            BatteryManager.BATTERY_STATUS_CHARGING,
            BatteryManager.BATTERY_STATUS_FULL -> true
            BatteryManager.BATTERY_STATUS_DISCHARGING,
            BatteryManager.BATTERY_STATUS_NOT_CHARGING -> false
            else -> null
        }

        val connectivityManager =
            appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork
        val capabilities = activeNetwork?.let {
            connectivityManager.getNetworkCapabilities(it)
        }
        val transport = when {
            capabilities == null -> "Offline"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> "VPN"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> "Wi-Fi"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> "Mobile data"
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> "Ethernet"
            else -> "Other"
        }
        val internetValidated = capabilities?.hasCapability(
            NetworkCapabilities.NET_CAPABILITY_VALIDATED
        ) == true

        val powerManager = appContext.getSystemService(Context.POWER_SERVICE) as PowerManager

        return DeviceSnapshotDraft(
            manufacturer = Build.MANUFACTURER.orUnknown(),
            model = Build.MODEL.orUnknown(),
            androidRelease = Build.VERSION.RELEASE.orUnknown(),
            apiLevel = Build.VERSION.SDK_INT,
            securityPatch = Build.VERSION.SECURITY_PATCH.orUnknown(),
            reproDeckVersionName = packageInfo.versionName.orUnknown(),
            reproDeckVersionCode = packageInfoCompatVersionCode(packageInfo),
            screenResolution = "${metrics.widthPixels} × ${metrics.heightPixels}",
            densityDpi = metrics.densityDpi,
            fontScale = configuration.fontScale,
            localeTag = locale.toLanguageTag(),
            timeZoneId = TimeZone.getDefault().id,
            networkTransport = transport,
            internetValidated = internetValidated,
            batteryPercent = batteryPercent,
            isCharging = isCharging,
            isPowerSaveMode = powerManager.isPowerSaveMode
        )
    }

    @Suppress("DEPRECATION")
    private fun packageInfoCompatVersionCode(packageInfo: PackageInfo): Long =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            packageInfo.longVersionCode
        } else {
            packageInfo.versionCode.toLong()
        }

    @Suppress("DEPRECATION")
    private fun getPackageInfo(): PackageInfo =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            appContext.packageManager.getPackageInfo(
                appContext.packageName,
                PackageManager.PackageInfoFlags.of(0)
            )
        } else {
            appContext.packageManager.getPackageInfo(appContext.packageName, 0)
        }

    private fun String?.orUnknown(): String = this?.takeIf { it.isNotBlank() } ?: "Unknown"
}
