package info.kurozeropb.azurlane.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.stfalcon.frescoimageviewer.ImageViewer
import info.kurozeropb.azurlane.helper.GlideApp
import info.kurozeropb.azurlane.responses.Skin
import kotlinx.android.synthetic.main.skin.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bindView(skin: Skin) {
        itemView.title_skin.text = skin.title ?: ""
        GlideApp.with(itemView.context)
            .load(skin.image)
            .into(itemView.img_skin)

        itemView.img_skin.onClick {
            ImageViewer.Builder(itemView.context, arrayOf(skin.image)).show()
        }
    }
}