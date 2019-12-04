package info.kurozeropb.alcompanion.fragments

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.responses.Ship
import info.kurozeropb.alcompanion.responses.Stat
import kotlinx.android.synthetic.main.fragment_tab_stats.*
import kotlinx.android.synthetic.main.fragment_tab_stats.view.*
import kotlin.math.roundToInt


class StatsInfo(val ship: Ship) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_tab_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val items = view.findViewById<LinearLayout>(R.id.stat_items)
        val statNames = mutableListOf<String>()
        if (ship.stats.base != null) statNames.add("Base")
        if (ship.stats.level100 != null) statNames.add("Level 100")
        if (ship.stats.level120 != null) statNames.add("Level 120")
        if (ship.stats.retrofit100 != null) statNames.add("Retrofit Level 100")
        if (ship.stats.retrofit120 != null) statNames.add("Retrofit Level 120")

        val adapter = ArrayAdapter<String>(view.context, R.layout.list_item, statNames)
        dropdown_stats.adapter = adapter
        dropdown_stats.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, pos: Int, id: Long) {
                when (pos) {
                    0 -> ship.stats.base!!.forEachIndexed { index, stat -> updateTextView(items, index, stat) }
                    1 -> ship.stats.level100!!.forEachIndexed { index, stat -> updateTextView(items, index, stat) }
                    2 -> ship.stats.level120!!.forEachIndexed { index, stat -> updateTextView(items, index, stat) }
                    3 -> ship.stats.retrofit100!!.forEachIndexed { index, stat -> updateTextView(items, index, stat) }
                    4 -> ship.stats.retrofit120!!.forEachIndexed { index, stat -> updateTextView(items, index, stat) }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Another interface callback
            }
        }
    }

    fun updateTextView(view: View, index: Int, stat: Stat) {
        when (index) {
            0 -> {
                view.tv_health.text = Html.fromHtml(getString(R.string.stat_health, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.health)
                val pixelDrawableSize = (view.tv_health.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_health.setCompoundDrawables(drawable, null, null, null)
            }
            1 -> {
                view.tv_armor.text = Html.fromHtml(getString(R.string.stat_armor, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.armor)
                val pixelDrawableSize = (view.tv_armor.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_armor.setCompoundDrawables(drawable, null, null, null)
            }
            2 -> {
                view.tv_reload.text = Html.fromHtml(getString(R.string.stat_reload, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.reload)
                val pixelDrawableSize = (view.tv_reload.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_reload.setCompoundDrawables(drawable, null, null, null)
            }
            3 -> {
                view.tv_luck.text = Html.fromHtml(getString(R.string.stat_luck, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.luck)
                val pixelDrawableSize = (view.tv_luck.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_luck.setCompoundDrawables(drawable, null, null, null)
            }
            4 -> {
                view.tv_firepower.text = Html.fromHtml(getString(R.string.stat_firepower, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.firepower_small)
                val pixelDrawableSize = (view.tv_firepower.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_firepower.setCompoundDrawables(drawable, null, null, null)
            }
            5 -> {
                view.tv_torpedo.text = Html.fromHtml(getString(R.string.stat_torpedo, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.torpedo)
                val pixelDrawableSize = (view.tv_torpedo.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_torpedo.setCompoundDrawables(drawable, null, null, null)
            }
            6 -> {
                view.tv_evasion.text = Html.fromHtml(getString(R.string.stat_evasion, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.evasion)
                val pixelDrawableSize = (view.tv_evasion.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_evasion.setCompoundDrawables(drawable, null, null, null)
            }
            7 -> {
                view.tv_speed.text = Html.fromHtml(getString(R.string.stat_speed, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
            }
            8 -> {
                view.tv_anti_air.text = Html.fromHtml(getString(R.string.stat_anti_air, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.anti_air)
                val pixelDrawableSize = (view.tv_anti_air.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_anti_air.setCompoundDrawables(drawable, null, null, null)
            }
            9 -> {
                view.tv_aviation.text = Html.fromHtml(getString(R.string.stat_aviation, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.aviation)
                val pixelDrawableSize = (view.tv_aviation.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_aviation.setCompoundDrawables(drawable, null, null, null)
            }
            10 -> {
                view.tv_oil.text = Html.fromHtml(getString(R.string.stat_oil, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.oil)
                val pixelDrawableSize = (view.tv_oil.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_oil.setCompoundDrawables(drawable, null, null, null)
            }
            11 -> {
                view.tv_accuracy.text = Html.fromHtml(getString(R.string.stat_accuracy, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
            }
            12 -> {
                view.tv_anti_sub_warfare.text = Html.fromHtml(getString(R.string.stat_anti_sub_warfare, "<b>${stat.value ?: ""}</b>"), Html.FROM_HTML_MODE_LEGACY)
                val drawable = ContextCompat.getDrawable(view.context, R.drawable.anti_sub)
                val pixelDrawableSize = (view.tv_anti_sub_warfare.lineHeight * 0.9).roundToInt()
                drawable?.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize)
                view.tv_anti_sub_warfare.setCompoundDrawables(drawable, null, null, null)
            }
        }
    }

}