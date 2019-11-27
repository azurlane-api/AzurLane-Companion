package info.kurozeropb.azurlane.adapter

import android.graphics.drawable.Drawable
import android.view.View
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
import org.jetbrains.anko.sdk27.coroutines.onClick

class ShipViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val requestOptions = RequestOptions()
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    fun bindView(ship: AllShip, mainActivity: View) {
        itemView.tv_ship_name.text = ship.name ?: "-"
        itemView.cv_ship.onClick {
            MainActivity.searchShip(ship.name ?: "", itemView.context, mainActivity)
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