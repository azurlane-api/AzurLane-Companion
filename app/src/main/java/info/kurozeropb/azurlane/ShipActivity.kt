package info.kurozeropb.azurlane

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import info.kurozeropb.azurlane.adapter.RecyclerAdapter
import info.kurozeropb.azurlane.helper.GlideApp
import info.kurozeropb.azurlane.helper.ItemDecoration
import info.kurozeropb.azurlane.responses.Ship
import kotlinx.android.synthetic.main.activity_ship.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

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

        val json = intent.getStringExtra("ship")
        if (json.isNullOrBlank()) {
            Snackbar.make(shipActivity, "Could not get ship data", Snackbar.LENGTH_LONG).show()
            return
        }
        val ship = Gson().fromJson(json, Ship::class.java)
        GlideApp.with(this@ShipActivity)
            .load(ship.skins[0].image)
            .into(main_image)

        tv_name.text = Html.fromHtml(getString(R.string.name, "<b>${ship.nationalityShort} ${ship.names.en}<br/>(cn: ${ship.names.cn}; jp: ${ship.names.jp}; kr: ${ship.names.kr})</b>"), Html.FROM_HTML_MODE_LEGACY)
        tv_construction_time.text = Html.fromHtml(getString(R.string.construction, "<b>${ship.buildTime}</b>"), Html.FROM_HTML_MODE_LEGACY)
        tv_rarity.text = Html.fromHtml(getString(R.string.rarity, "<b>${ship.rarity}</b>"), Html.FROM_HTML_MODE_LEGACY)
        tv_class.text = Html.fromHtml(getString(R.string.ship_class, "<b>${ship.`class`}</b>"), Html.FROM_HTML_MODE_LEGACY)
        tv_nationality.text = Html.fromHtml(getString(R.string.nationality, "<b>${ship.nationality}</b>"), Html.FROM_HTML_MODE_LEGACY)
        tv_classification.text = Html.fromHtml(getString(R.string.classification, "<b>${ship.hullType}</b>"), Html.FROM_HTML_MODE_LEGACY)

        tv_artist.text = Html.fromHtml(getString(R.string.artist, "<a href=\"${ship.miscellaneous.artist?.link}\"><b>${ship.miscellaneous.artist?.name}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        tv_artist.movementMethod = LinkMovementMethod.getInstance()
        tv_web.text = Html.fromHtml(getString(R.string.web, "<a href=\"${ship.miscellaneous.web?.link}\"><b>${ship.miscellaneous.web?.name}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        tv_web.movementMethod = LinkMovementMethod.getInstance()
        tv_pixiv.text = Html.fromHtml(getString(R.string.pixiv, "<a href=\"${ship.miscellaneous.pixiv?.link}\"><b>${ship.miscellaneous.pixiv?.name}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        tv_pixiv.movementMethod = LinkMovementMethod.getInstance()
        tv_twitter.text = Html.fromHtml(getString(R.string.twitter, "<a href=\"${ship.miscellaneous.twitter?.link}\"><b>${ship.miscellaneous.twitter?.name}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        tv_twitter.movementMethod = LinkMovementMethod.getInstance()
        tv_voice_actress.text = Html.fromHtml(getString(R.string.voice_actress, "<a href=\"${ship.miscellaneous.voiceActress?.link}\"><b>${ship.miscellaneous.voiceActress?.name}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        tv_voice_actress.movementMethod = LinkMovementMethod.getInstance()

        rv_row.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_row.addItemDecoration(ItemDecoration(80, ship.skins.size))

        val adapter = RecyclerAdapter()
        adapter.setImages(ship.skins.subList(1, ship.skins.size))
        rv_row.adapter = adapter
    }

    override fun onBackPressed() {
        API.getShipNames {
            val (response, exception) = this
            GlobalScope.launch(Dispatchers.Main) {
                when {
                    response != null -> {
                        val intent = Intent(this@ShipActivity, MainActivity::class.java)
                        intent.putExtra("names", Gson().toJson(response.ships))
                        startActivity(intent)
                        finish()
                    }
                    exception != null -> {
                        Timer().schedule(200) {
                            Snackbar.make(shipActivity, exception.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}