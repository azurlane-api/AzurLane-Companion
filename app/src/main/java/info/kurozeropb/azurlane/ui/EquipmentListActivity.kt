package info.kurozeropb.azurlane.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.navigation.NavigationView
import info.kurozeropb.azurlane.R
import kotlinx.android.synthetic.main.activity_equipment_list.*
import kotlinx.android.synthetic.main.app_bar_equipment.*

class EquipmentListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_equipment_list)
        setSupportActionBar(toolbar_equipment)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        window.apply {
            statusBarColor = ContextCompat.getColor(this@EquipmentListActivity, R.color.colorPrimary)
            navigationBarColor = ContextCompat.getColor(this@EquipmentListActivity, R.color.colorPrimary)
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar_equipment, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_ships -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_equipment -> {
                drawer_layout.closeDrawers()
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}