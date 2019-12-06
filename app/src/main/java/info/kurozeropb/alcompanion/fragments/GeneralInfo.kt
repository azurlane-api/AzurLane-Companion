@file:Suppress("DEPRECATION")

package info.kurozeropb.alcompanion.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hendraanggrian.pikasso.into
import com.hendraanggrian.pikasso.picasso
import com.stfalcon.frescoimageviewer.ImageViewer
import info.kurozeropb.alcompanion.Api
import info.kurozeropb.alcompanion.App
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.adapters.SkinRecyclerAdapter
import info.kurozeropb.alcompanion.adapters.file
import info.kurozeropb.alcompanion.helpers.GlideApp
import info.kurozeropb.alcompanion.helpers.ItemDecoration
import info.kurozeropb.alcompanion.responses.Ship
import kotlinx.android.synthetic.main.overlay.view.*
import kotlinx.android.synthetic.main.fragment_tab_general.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.act
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

class GeneralInfo(val name: String, val ship: Ship) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_general, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val customTabs = CustomTabsIntent.Builder()
                .addDefaultShareMenuItem()
                .setToolbarColor(ContextCompat.getColor(view.context, R.color.colorPrimary))
                .setShowTitle(true)
                .build()

        val rainbow = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(
                ContextCompat.getColor(view.context, R.color.rainbow_yellow),
                ContextCompat.getColor(view.context, R.color.rainbow_green),
                ContextCompat.getColor(view.context, R.color.rainbow_blue),
                ContextCompat.getColor(view.context, R.color.rainbow_purple)
            )
        )

        val rarities = mapOf(
            "normal" to ContextCompat.getColor(view.context, R.color.normal),
            "rare" to ContextCompat.getColor(view.context, R.color.rare),
            "elite" to ContextCompat.getColor(view.context, R.color.elite),
            "super rare" to ContextCompat.getColor(view.context, R.color.super_rare),
            "priority" to ContextCompat.getColor(view.context, R.color.priority),
            "unreleased" to ContextCompat.getColor(view.context, R.color.unreleased)
        )

        if (ship.rarity != null) {
            when (val rarity = ship.rarity.toLowerCase(Locale.getDefault())) {
                "decisive", "ultra rare" -> view.main_image.backgroundDrawable = rainbow
                else -> view.main_image.backgroundColor = rarities[rarity] ?: R.color.background
            }
        }

        GlideApp.with(this)
            .load(ship.skins[0].image)
            .into(view.main_image)

        view.main_image.onClick {
            val overlay = View.inflate(view.context, R.layout.overlay, null)
            overlay.btn_share.onClick {
                picasso.load(ship.skins[0].image).into {
                    onFailed { e, _ ->
                        Snackbar.make(view, e.message ?: "Something went wrong", Snackbar.LENGTH_LONG).show()
                    }
                    onLoaded { bitmap, _ ->
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/png"

                        val bytes = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

                        val sdCard = Environment.getExternalStorageDirectory()
                        val dir = File(sdCard.absolutePath + "/AzurLane")
                        if (dir.exists().not())
                            dir.mkdirs()

                        file = File(dir, "share-${ship.skins[0].title?.toLowerCase(Locale.getDefault())?.replace(" ", "-")}.png")

                        try {
                            file.createNewFile()
                            val fo = FileOutputStream(file)
                            fo.write(bytes.toByteArray())
                            fo.flush()
                            fo.close()
                        } catch (e: IOException) {
                            val message = e.message ?: "Unable to save/share image"
                            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show()
                        }

                        val uri = FileProvider.getUriForFile(view.context, view.context.applicationContext.packageName + ".ImageFileProvider", file)
                        intent.putExtra(Intent.EXTRA_STREAM, uri)

                        startActivityForResult(Intent.createChooser(intent,"Share Image"), App.SHARE_IMAGE, null)

                        file.deleteOnExit()
                    }
                }
            }

            overlay.btn_save.onClick {
                Api.downloadAndSave(
                    ship.skins[0].title?.toLowerCase(Locale.getDefault())?.replace(" ", "-") ?: "unkown",
                    ship.skins[0].image ?: "",
                    view
                )
            }

            ImageViewer.Builder(context, arrayOf(ship.skins[0].image))
                .setOverlayView(overlay)
                .show()
        }

        view.tv_name.text = Html.fromHtml(getString(R.string.name, "<b>${ship.nationalityShort ?: ""} ${ship.names.en ?: ""}<br/>(cn: ${ship.names.cn ?: ""}; jp: ${ship.names.jp ?: ""}; kr: ${ship.names.kr ?: ""})</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_construction_time.text = Html.fromHtml(getString(R.string.construction, "<b>${ship.buildTime ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_rarity.text = Html.fromHtml(getString(R.string.rarity, "<b>${ship.rarity ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_class.text = Html.fromHtml(getString(R.string.ship_class, "<b>${ship.`class` ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_nationality.text = Html.fromHtml(getString(R.string.nationality, "<b>${ship.nationality ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_classification.text = Html.fromHtml(getString(R.string.classification, "<b>${ship.hullType ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)

        view.tv_artist.text = Html.fromHtml(getString(R.string.artist, "<font color=\"#B4D5E8\"><b>${ship.miscellaneous.artist?.name ?: ""}</b></font>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_artist.onClick {
            if (ship.miscellaneous.artist?.link != null) {
                customTabs.launchUrl(view.context, Uri.parse(ship.miscellaneous.artist?.link))
            }
        }

        view.tv_web.text = Html.fromHtml(getString(R.string.web, "<font color=\"#B4D5E8\"><b>${ship.miscellaneous.web?.name ?: ""}</b></font>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_web.onClick {
            if (ship.miscellaneous.web?.link != null) {
                customTabs.launchUrl(view.context, Uri.parse(ship.miscellaneous.web?.link))
            }
        }

        view.tv_pixiv.text = Html.fromHtml(getString(R.string.pixiv, "<font color=\"#B4D5E8\"><b>${ship.miscellaneous.pixiv?.name ?: ""}</b></font>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_pixiv.onClick {
            if (ship.miscellaneous.pixiv?.link != null) {
                customTabs.launchUrl(view.context, Uri.parse(ship.miscellaneous.pixiv?.link))
            }
        }

        view.tv_twitter.text = Html.fromHtml(getString(R.string.twitter, "<font color=\"#B4D5E8\"><b>${ship.miscellaneous.twitter?.name ?: ""}</b></font>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_twitter.onClick {
            if (ship.miscellaneous.twitter?.link != null) {
                customTabs.launchUrl(view.context, Uri.parse(ship.miscellaneous.twitter?.link))
            }
        }

        view.tv_voice_actress.text = Html.fromHtml(getString(R.string.voice_actress, "<font color=\"#B4D5E8\"><b>${ship.miscellaneous.voiceActress?.name ?: ""}</b></font>"), Html.FROM_HTML_MODE_LEGACY)
        view.tv_voice_actress.onClick {
            if (ship.miscellaneous.voiceActress?.link != null) {
                customTabs.launchUrl(view.context, Uri.parse(ship.miscellaneous.voiceActress?.link))
            }
        }

        view.rv_row.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.rv_row.addItemDecoration(ItemDecoration(80, ship.skins.size))

        val adapter = SkinRecyclerAdapter()
        val skins = ship.skins.subList(1, ship.skins.size)
        if (skins.isNullOrEmpty()) view.rv_row.visibility = View.GONE
        adapter.setImages(skins, act)
        view.rv_row.adapter = adapter
    }

}