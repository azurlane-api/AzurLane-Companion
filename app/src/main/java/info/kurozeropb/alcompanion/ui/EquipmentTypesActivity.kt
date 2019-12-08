package info.kurozeropb.alcompanion.ui

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import info.kurozeropb.alcompanion.Api
import info.kurozeropb.alcompanion.App
import info.kurozeropb.alcompanion.R
import kotlinx.android.synthetic.main.activity_equipment_types.*
import kotlinx.android.synthetic.main.app_bar_equipment.*
import kotlinx.android.synthetic.main.card_equipment_type.view.*
import kotlinx.android.synthetic.main.content_equipment_types.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.concurrent.schedule

class EquipmentTypesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_equipment_types)
        setSupportActionBar(toolbar_equipment)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        sharedPreferences.registerOnSharedPreferenceChangeListener(App::onSharedPreferencesChange)

        window.apply {
            statusBarColor = ContextCompat.getColor(this@EquipmentTypesActivity, R.color.colorPrimary)
            navigationBarColor = ContextCompat.getColor(this@EquipmentTypesActivity, R.color.colorPrimary)
        }

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar_equipment, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        val inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200
        val outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200

        progressBarHolder.animation = inAnimation
        progressBarHolder.visibility = View.VISIBLE

        Api.getEquipmentTypes {
            val (response, exception) = this
            GlobalScope.launch(Dispatchers.Main) {
                when {
                    response != null -> {
                        response.equipments.forEach { type ->
                            val layout = LayoutInflater.from(this@EquipmentTypesActivity).inflate(R.layout.card_equipment_type, ll_equipment_types, false)
                            layout.tv_equipment_type.text = type.name ?: Html.fromHtml("<font color='#DC143C'><b>Failed to load</b></font>", Html.FROM_HTML_MODE_LEGACY)

                            layout.cv_equipment_type.onClick {
                                if (type.name != null) {
                                    progressBarHolder.animation = inAnimation
                                    progressBarHolder.visibility = View.VISIBLE

                                    Api.getEquipments(type.name) {
                                        val (res, e) = this
                                        GlobalScope.launch(Dispatchers.Main) {
                                            when {
                                                res != null -> {
                                                    progressBarHolder.animation = outAnimation
                                                    progressBarHolder.visibility = View.GONE

                                                    val intent = Intent(this@EquipmentTypesActivity, EquipmentsActivity::class.java)
                                                    intent.putExtra("equipments", Gson().toJson(res.equipments))
                                                    startActivity(intent)
                                                    finish()
                                                }
                                                e != null -> {
                                                    progressBarHolder.animation = outAnimation
                                                    progressBarHolder.visibility = View.GONE

                                                    Timer().schedule(200) {
                                                        Snackbar.make(contentEquipmentTypes, e.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }

                            ll_equipment_types.addView(layout)
                        }.also {
                            progressBarHolder.animation = outAnimation
                            progressBarHolder.visibility = View.GONE
                        }
                    }
                    exception != null -> {
                        progressBarHolder.animation = outAnimation
                        progressBarHolder.visibility = View.GONE

                        Timer().schedule(200) {
                            Snackbar.make(contentEquipmentTypes, exception.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
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