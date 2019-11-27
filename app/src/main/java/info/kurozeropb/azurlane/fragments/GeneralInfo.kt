package info.kurozeropb.azurlane.fragments

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.stfalcon.frescoimageviewer.ImageViewer
import info.kurozeropb.azurlane.R
import info.kurozeropb.azurlane.adapter.SkinRecyclerAdapter
import info.kurozeropb.azurlane.helper.GlideApp
import info.kurozeropb.azurlane.helper.ItemDecoration
import info.kurozeropb.azurlane.responses.Ship
import kotlinx.android.synthetic.main.activity_ship.*
import kotlinx.android.synthetic.main.tab_layout_general.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class GeneralInfo : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.tab_layout_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val json = activity?.intent?.getStringExtra("ship")
        if (json.isNullOrBlank()) {
            Snackbar.make(shipActivity, "Could not get ship data", Snackbar.LENGTH_LONG).show()
            return
        }

        val ship = Gson().fromJson(json, Ship::class.java)
        GlideApp.with(this)
            .load(ship.skins[0].image)
            .into(view.main_image)

        view.main_image.onClick {
            ImageViewer.Builder(context, arrayOf(ship.skins[0].image)).show()
        }

        view.tv_name.text = Html.fromHtml(getString(R.string.name, "<b>${ship.nationalityShort ?: ""} ${ship.names.en ?: ""}<br/>(cn: ${ship.names.cn ?: ""}; jp: ${ship.names.jp ?: ""}; kr: ${ship.names.kr ?: ""})</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_construction_time.text = Html.fromHtml(getString(R.string.construction, "<b>${ship.buildTime ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_rarity.text = Html.fromHtml(getString(R.string.rarity, "<b>${ship.rarity ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_class.text = Html.fromHtml(getString(R.string.ship_class, "<b>${ship.`class` ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_nationality.text = Html.fromHtml(getString(R.string.nationality, "<b>${ship.nationality ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_classification.text = Html.fromHtml(getString(R.string.classification, "<b>${ship.hullType ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)

        view.tv_artist.text = Html.fromHtml(getString(R.string.artist, "<a href=\"${ship.miscellaneous.artist?.link ?: ""}\"><b>${ship.miscellaneous.artist?.name ?: ""}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_artist.movementMethod = LinkMovementMethod.getInstance()
        view.tv_web.text = Html.fromHtml(getString(R.string.web, "<a href=\"${ship.miscellaneous.web?.link ?: ""}\"><b>${ship.miscellaneous.web?.name ?: ""}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_web.movementMethod = LinkMovementMethod.getInstance()
        view.tv_pixiv.text = Html.fromHtml(getString(R.string.pixiv, "<a href=\"${ship.miscellaneous.pixiv?.link ?: ""}\"><b>${ship.miscellaneous.pixiv?.name ?: ""}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_pixiv.movementMethod = LinkMovementMethod.getInstance()
        view.tv_twitter.text = Html.fromHtml(getString(R.string.twitter, "<a href=\"${ship.miscellaneous.twitter?.link ?: ""}\"><b>${ship.miscellaneous.twitter?.name ?: ""}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_twitter.movementMethod = LinkMovementMethod.getInstance()
        view.tv_voice_actress.text = Html.fromHtml(getString(R.string.voice_actress, "<a href=\"${ship.miscellaneous.voiceActress?.link ?: ""}\"><b>${ship.miscellaneous.voiceActress?.name ?: ""}</b></a>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_voice_actress.movementMethod = LinkMovementMethod.getInstance()

        view.rv_row.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.rv_row.addItemDecoration(ItemDecoration(80, ship.skins.size))

        val adapter = SkinRecyclerAdapter()
        val skins = ship.skins.subList(1, ship.skins.size)
        if (skins.isNullOrEmpty()) view.rv_row.visibility = View.GONE
        adapter.setImages(skins)
        view.rv_row.adapter = adapter
    }

}