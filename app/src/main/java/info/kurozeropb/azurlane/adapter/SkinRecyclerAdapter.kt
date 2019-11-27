package info.kurozeropb.azurlane.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.kurozeropb.azurlane.R
import info.kurozeropb.azurlane.responses.Skin

class SkinRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var images = listOf<Skin>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.skin, parent, false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ImageViewHolder
        holder.bindView(images[position])
    }

    fun setImages(imgs: List<Skin>) {
        this.images = imgs
        notifyDataSetChanged()
    }
}