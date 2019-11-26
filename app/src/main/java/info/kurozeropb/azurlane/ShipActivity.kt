package info.kurozeropb.azurlane

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import info.kurozeropb.azurlane.adapter.ViewPagerAdapter
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

    override fun onBackPressed() {
        val intent = Intent(this@ShipActivity, MainActivity::class.java)
        startActivity(intent)
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