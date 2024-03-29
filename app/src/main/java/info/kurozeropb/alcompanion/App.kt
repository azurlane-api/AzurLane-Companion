package info.kurozeropb.alcompanion

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat


object App {
    const val SHARE_IMAGE = 999
    const val REQUEST_PERMISSION = 998

    // Permissions needed to save and upload images
    val permissions = arrayOf(
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    )

    fun hasPermissions(context: Context?): Boolean {
        if (context != null) {
            for (permission in permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }
        }
        return true
    }

    fun onSharedPreferencesChange(sharedPreferences: SharedPreferences, key: String) {
        if (key == "darkmode") {
            val darkmode = sharedPreferences.getBoolean(key, false)
            if (darkmode) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

}