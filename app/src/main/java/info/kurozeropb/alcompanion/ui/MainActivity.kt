package info.kurozeropb.alcompanion.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import info.kurozeropb.alcompanion.Api
import info.kurozeropb.alcompanion.App
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.adapters.ShipRecyclerAdapter
import info.kurozeropb.alcompanion.responses.Ships
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onEditorAction

lateinit var ships: Ships

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        sharedPreferences.registerOnSharedPreferenceChangeListener(App::onSharedPreferencesChange)

        window.apply {
            statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
            navigationBarColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val stringShips = intent.getStringExtra("ships")
        if (!stringShips.isNullOrBlank()) {
            ships = Gson().fromJson<Ships>(stringShips, object : TypeToken<Ships?>() {}.type)
            ships = ships.distinctBy { it.name }
        }

        rv_ships.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val rvAdapter = ShipRecyclerAdapter()
        rvAdapter.setImages(ships, contentMain)
        rv_ships.adapter = rvAdapter

        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, ships.map { it.name })
        et_search_bar.setAdapter(adapter)

        et_search_bar.onEditorAction { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val name = et_search_bar.text.toString()
                    Api.searchShip(name, contentMain)
                }
            }
        }

        et_search_bar.addTextChangedListener {
            if ((it?.length ?: 0) >= 1) {
                btn_clear_search_bar.visibility = View.VISIBLE
            } else {
                btn_clear_search_bar.visibility = View.GONE
            }
        }

        btn_clear_search_bar.onClick {
            et_search_bar.text.clear()
        }

        fab_to_top.onClick {
            (rv_ships.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(0, 0)
        }

        if (!App.hasPermissions(this)) {
            ActivityCompat.requestPermissions(this, App.permissions, App.REQUEST_PERMISSION)
        }
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawers()
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_ships -> {
                drawer_layout.closeDrawers()
            }
            R.id.nav_equipment -> {
                val intent = Intent(this, EquipmentListActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return true
    }

}
