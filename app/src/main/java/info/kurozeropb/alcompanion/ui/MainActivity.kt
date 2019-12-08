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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import info.kurozeropb.alcompanion.Api
import info.kurozeropb.alcompanion.App
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.adapters.ShipRecyclerAdapter
import info.kurozeropb.alcompanion.helpers.EndlessRecyclerViewScrollListener
import info.kurozeropb.alcompanion.helpers.GlideApp
import info.kurozeropb.alcompanion.responses.Ships
import it.sephiroth.android.library.xtooltip.ClosePolicy
import it.sephiroth.android.library.xtooltip.Tooltip
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onEditorAction

lateinit var allShips: Ships
lateinit var filteredShips: Ships
lateinit var ships: Ships

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    val pageSize = 10
    var oldPage = 0
    var newPage = pageSize

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

        val icon = intent.getStringExtra("icon")
        val nation = intent.getStringExtra("nation")
        val tooltipText = if (icon != null) {
            GlideApp.with(this)
                    .load(icon)
                    .apply(requestOptions)
                    .error(R.drawable.placeholder)
                    .into(iv_active_filter)

            "Only $nation ships are shown"
        } else {
            iv_active_filter.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.filter_all))

            "All ships are shown"
        }

        iv_active_filter.onClick {
            Tooltip.Builder(this@MainActivity)
                    .anchor(iv_active_filter, 0, 0, false)
                    .text(tooltipText)
                    .styleId(R.style.Tooltip)
                    .arrow(true)
                    .floatingAnimation(Tooltip.Animation.SLOW)
                    .closePolicy(ClosePolicy.TOUCH_INSIDE_NO_CONSUME)
                    .showDuration(5000)
                    .create()
                    .show(iv_active_filter, Tooltip.Gravity.BOTTOM, true)
        }

        val jsonships = intent.getStringExtra("ships")
        if (!jsonships.isNullOrBlank()) {
            allShips = Gson().fromJson<Ships>(jsonships, object : TypeToken<Ships?>() {}.type)
            allShips = allShips.distinctBy { it.name } // TODO : Should be done by the api
        }

        if (nation != null) {
            filteredShips = allShips.filter { it.nationality == nation }
            ships = filteredShips
        } else {
            ships = allShips
        }

        val maxShips = ships.size
        val showShips = ships.toMutableList().subList(oldPage, if (newPage > ships.size) ships.size else newPage)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rv_ships.layoutManager = layoutManager

        val rvAdapter = ShipRecyclerAdapter()
        rvAdapter.setImages(showShips, contentMain)
        rv_ships.adapter = rvAdapter

        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                oldPage = newPage
                newPage += pageSize
                if (newPage > maxShips) {
                    return
                }

                val curSize = rvAdapter.itemCount
                val moreShips = ships.subList(oldPage, newPage)
                showShips.addAll(moreShips)
                view.post { rvAdapter.notifyItemRangeInserted(curSize, showShips.size - 1) }
            }
        }
        // Adds the scroll listener to RecyclerView
        rv_ships.addOnScrollListener(scrollListener)

        val adapter = ArrayAdapter<String>(this, R.layout.list_item, ships.map { it.name })
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
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_nations -> {
                val intent = Intent(this, NationsActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_equipment -> {
                val intent = Intent(this, EquipmentTypesActivity::class.java)
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
