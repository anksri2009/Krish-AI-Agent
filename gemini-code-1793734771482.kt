// Inside MainActivity.kt when user taps "Apply Voice & Activate Krish"
private fun startAlwaysOnKrishService() {
    // 1. Request Android Doze / Battery Optimization Exemption (1-tap system dialog)
    val pm = getSystemService(POWER_SERVICE) as android.os.PowerManager
    if (!pm.isIgnoringBatteryOptimizations(packageName)) {
        val batteryIntent = Intent(android.provider.Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS).apply {
            data = android.net.Uri.parse("package:$packageName")
        }
        startActivity(batteryIntent)
    }

    // 2. Launch the Always-On Lockscreen Foreground Service
    val gender = if (genderSpinner.selectedItemPosition == 0) "male" else "female"
    val localeTag = when (langSpinner.selectedItemPosition) {
        0 -> "en-IN"
        1 -> "hi-IN"
        2 -> "en-US"
        3 -> "es-ES"
        else -> "fr-FR"
    }

    val serviceIntent = Intent(this, KrishWakeService::class.java).apply {
        putExtra("API_KEY", apiKeyInput.text.toString().trim())
        putExtra("GENDER", gender)
        putExtra("LOCALE", localeTag)
    }

    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        startForegroundService(serviceIntent)
    } else {
        startService(serviceIntent)
    }

    statusText.text = "Krish Sentinel Active! You can now lock your phone screen."
}