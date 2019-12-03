package info.kurozeropb.alcompanion.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.kurozeropb.alcompanion.App
import info.kurozeropb.alcompanion.R
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.sdk27.coroutines.onCheckedChange

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_settings)

        sharedPreferences.registerOnSharedPreferenceChangeListener(App::onSharedPreferencesChange)

        val darkmodeEnabled = sharedPreferences.getBoolean("darkmode", false)
        switch_darkmode.isChecked = darkmodeEnabled

        switch_darkmode.onCheckedChange { _, isChecked ->
            sharedPreferences.edit().putBoolean("darkmode", isChecked).apply()
        }
    }

}