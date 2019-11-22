package info.kurozeropb.azurlane.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import info.kurozeropb.azurlane.helper.GlideApp
import info.kurozeropb.azurlane.responses.Skin
import kotlinx.android.synthetic.main.skin.view.*

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(skin: Skin) {
        GlideApp.with(itemView.context)
            .load(skin.image)
            .into(itemView.img_skin)
    }
}