package info.kurozeropb.alcompanion.adapters

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import info.kurozeropb.alcompanion.Api
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.helpers.GlideApp
import info.kurozeropb.alcompanion.responses.AllShip
import kotlinx.android.synthetic.main.card_ship.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class ShipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    fun bindView(ship: AllShip, mainView: View) {
        val rainbow = GradientDrawable(
            GradientDrawable.Orientation.TL_BR, intArrayOf(
                ContextCompat.getColor(itemView.context, R.color.rainbow_yellow),
                ContextCompat.getColor(itemView.context, R.color.rainbow_green),
                ContextCompat.getColor(itemView.context, R.color.rainbow_blue),
                ContextCompat.getColor(itemView.context, R.color.rainbow_purple)
            )
        )

        val rarities = mapOf(
            "normal" to ContextCompat.getColor(itemView.context, R.color.normal),
            "rare" to ContextCompat.getColor(itemView.context, R.color.rare),
            "elite" to ContextCompat.getColor(itemView.context, R.color.elite),
            "super rare" to ContextCompat.getColor(itemView.context, R.color.super_rare),
            "priority" to ContextCompat.getColor(itemView.context, R.color.priority),
            "unreleased" to ContextCompat.getColor(itemView.context, R.color.unreleased)
        )

        itemView.tv_ship_name.text = ship.name ?: "-"
        itemView.cv_ship.onClick {
            Api.searchShip(ship.name ?: "", mainView)
        }

        if (ship.rarity != null) {
            when (val rarity = ship.rarity.toLowerCase(Locale.getDefault())) {
                "decisive", "ultra rare" -> itemView.iv_ship_avatar.backgroundDrawable = rainbow
                else -> itemView.iv_ship_avatar.backgroundColor = rarities[rarity] ?: ContextCompat.getColor(itemView.context, R.color.background)
            }
        }

        GlideApp.with(itemView.context)
            .load(ship.avatar)
            .apply(requestOptions)
            .error(R.drawable.placeholder)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any, target: Target<Drawable>, isFirstResource: Boolean): Boolean {
                    itemView.progress.visibility = View.GONE
                    return false
                }

                override fun onResourceReady(resource: Drawable, model: Any, target: Target<Drawable>, dataSource: DataSource, isFirstResource: Boolean): Boolean {
                    itemView.progress.visibility = View.GONE
                    return false
                }
            })
            .into(itemView.iv_ship_avatar)
    }

}