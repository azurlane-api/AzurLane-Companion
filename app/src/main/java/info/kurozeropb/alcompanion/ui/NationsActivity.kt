package info.kurozeropb.alcompanion.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import info.kurozeropb.alcompanion.Api
import info.kurozeropb.alcompanion.App
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.adapters.NationsRecyclerAdapter
import info.kurozeropb.alcompanion.helpers.EndlessRecyclerViewScrollListener
import info.kurozeropb.alcompanion.responses.Nations
import kotlinx.android.synthetic.main.activity_nations.*
import kotlinx.android.synthetic.main.app_bar_nations.*
import kotlinx.android.synthetic.main.content_nations.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

lateinit var nations: Nations

class NationsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    val pageSize = 10
    var oldPage = 0
    var newPage = pageSize

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_nations)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        sharedPreferences.registerOnSharedPreferenceChangeListener(App::onSharedPreferencesChange)

        window.apply {
            statusBarColor = ContextCompat.getColor(this@NationsActivity, R.color.colorPrimary)
            navigationBarColor = ContextCompat.getColor(this@NationsActivity, R.color.colorPrimary)
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view_nations.setNavigationItemSelectedListener(this)

        val inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200
        val outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200

        progressBarHolder.animation = inAnimation
        progressBarHolder.visibility = View.VISIBLE

        Api.getNations {
            val (response, exception) = this
            GlobalScope.launch(Dispatchers.Main) {
                when {
                    response != null -> {
                        progressBarHolder.animation = outAnimation
                        progressBarHolder.visibility = View.GONE
                        nations = response.nations

                        val maxNations = nations.size
                        val showNations = nations.toMutableList().subList(oldPage, newPage)

                        val layoutManager = LinearLayoutManager(this@NationsActivity, LinearLayoutManager.VERTICAL, false)
                        rv_nations.layoutManager = layoutManager

                        val rvAdapter = NationsRecyclerAdapter()
                        rvAdapter.setImages(showNations, this@NationsActivity)
                        rv_nations.adapter = rvAdapter

                        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
                            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                                oldPage = newPage
                                newPage += pageSize
                                if (newPage > maxNations) {
                                    newPage = maxNations
                                }

                                val curSize = rvAdapter.itemCount
                                val moreNations = nations.subList(oldPage, newPage)
                                showNations.addAll(moreNations)
                                view.post { rvAdapter.notifyItemRangeInserted(curSize, showNations.size - 1) }
                            }
                        }
                        // Adds the scroll listener to RecyclerView
                        rv_nations.addOnScrollListener(scrollListener)
                    }
                    exception != null -> {
                        progressBarHolder.animation = outAnimation
                        progressBarHolder.visibility = View.GONE

                        Timer().schedule(200) {
                            Snackbar.make(contentNations, exception.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
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