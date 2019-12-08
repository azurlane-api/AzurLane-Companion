package info.kurozeropb.alcompanion.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import info.kurozeropb.alcompanion.App
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.helpers.GlideApp
import info.kurozeropb.alcompanion.responses.Equipments
import kotlinx.android.synthetic.main.activity_equipments.*
import kotlinx.android.synthetic.main.app_bar_equipment.*
import kotlinx.android.synthetic.main.card_equipment.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import java.util.*

class EquipmentsActivity : AppCompatActivity() {

    val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_equipments)
        setSupportActionBar(toolbar_equipment)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val rainbow = GradientDrawable(GradientDrawable.Orientation.TL_BR, intArrayOf(
                ContextCompat.getColor(this, R.color.rainbow_yellow),
                ContextCompat.getColor(this, R.color.rainbow_green),
                ContextCompat.getColor(this, R.color.rainbow_blue),
                ContextCompat.getColor(this, R.color.rainbow_purple)
        ))

        val rarities = mapOf(
                "normal" to ContextCompat.getColor(this, R.color.normal),
                "rare" to ContextCompat.getColor(this, R.color.rare),
                "epic" to ContextCompat.getColor(this, R.color.elite),
                "super rare" to ContextCompat.getColor(this, R.color.super_rare)
        )

        sharedPreferences.registerOnSharedPreferenceChangeListener(App::onSharedPreferencesChange)

        val equipments = Gson().fromJson<Equipments>(intent.getStringExtra("equipments"), object : TypeToken<Equipments?>() {}.type)
        equipments.forEach { equipment ->
            val layout = LayoutInflater.from(this).inflate(R.layout.card_equipment, ll_equipments, false)
            layout.tv_equipment_name.text = equipment.name ?: Html.fromHtml("<font color='#DC143C'><b>Failed to load</b></font>", Html.FROM_HTML_MODE_LEGACY)

            if (equipment.rarity != null) {
                when (val rarity = equipment.rarity.toLowerCase(Locale.getDefault())) {
                    "ultra rare" -> layout.iv_equipment_icon.backgroundDrawable = rainbow
                    else -> layout.iv_equipment_icon.backgroundColor = rarities[rarity] ?: ContextCompat.getColor(this, R.color.background)
                }
            }

            GlideApp.with(this)
                    .load(equipment.icon)
                    .apply(requestOptions)
                    .error(R.drawable.placeholder)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                            layout.progress.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                            layout.progress.visibility = View.GONE
                            return false
                        }
                    })
                    .into(layout.iv_equipment_icon)

            ll_equipments.addView(layout)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, EquipmentTypesActivity::class.java)
        startActivity(intent)
        finish()
    }

}