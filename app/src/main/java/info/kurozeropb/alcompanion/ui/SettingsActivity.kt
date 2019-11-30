package info.kurozeropb.alcompanion.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.kurozeropb.alcompanion.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_settings)
    }

}