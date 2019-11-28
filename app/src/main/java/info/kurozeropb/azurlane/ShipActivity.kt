package info.kurozeropb.azurlane

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import info.kurozeropb.azurlane.adapter.ViewPagerAdapter
import info.kurozeropb.azurlane.adapter.file
import info.kurozeropb.azurlane.fragments.GeneralInfo
import info.kurozeropb.azurlane.fragments.StatsInfo
import kotlinx.android.synthetic.main.activity_ship.*

class ShipActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_ship)

        val name = intent.getStringExtra("name")
        if (name.isNullOrBlank()) {
            Snackbar.make(shipActivity, "Name can't be empty", Snackbar.LENGTH_LONG).show()
            return
        }

        setupViewPager().also {
            tl_info.setupWithViewPager(viewpager)
        }
    }

    // Whenever we get a result from an intent
    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        // If the user canceled the action just return on do nothing
        if (resultCode == RESULT_CANCELED) return
        when (requestCode) {
            App.SHARE_IMAGE -> { // Delete temp file after sharing an image
                try {
                    if (file.exists() && resultCode == RESULT_OK) {
                        Thread.sleep(1_000) // Stupid result doesn't actually wait for the intent to finish sending...
                        val isDeleted = file.delete()
                        if (isDeleted.not())
                            Snackbar.make(shipActivity, "Could not delete shared file", Snackbar.LENGTH_LONG).show()
                    }
                } catch (e: Exception) {
                    Snackbar.make(shipActivity, e.message ?: "Something went wrong", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    private fun setupViewPager() {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.apply {
            addFragment(GeneralInfo(), "General")
            addFragment(StatsInfo(), "Stats")
        }
        viewpager.adapter = adapter
    }
}