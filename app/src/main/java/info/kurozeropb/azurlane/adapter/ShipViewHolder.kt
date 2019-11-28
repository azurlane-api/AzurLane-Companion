package info.kurozeropb.azurlane.adapter

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
import info.kurozeropb.azurlane.MainActivity
import info.kurozeropb.azurlane.R
import info.kurozeropb.azurlane.helper.GlideApp
import info.kurozeropb.azurlane.responses.AllShip
import kotlinx.android.synthetic.main.card_ship.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.backgroundDrawable
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class ShipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    fun bindView(ship: AllShip, mainActivity: View) {
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
            MainActivity.searchShip(ship.name ?: "", itemView.context, mainActivity)
        }

        if (ship.rarity != null) {
            val rarity = ship.rarity.toLowerCase(Locale.getDefault())
            val color = rarities[rarity]
            if (color != null) {
                itemView.iv_ship_avatar.backgroundColor = color
            } else {
                when (rarity) {
                    "decisive" -> itemView.iv_ship_avatar.backgroundDrawable = rainbow
                    "ultra rare" -> itemView.iv_ship_avatar.backgroundDrawable = rainbow
                }
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