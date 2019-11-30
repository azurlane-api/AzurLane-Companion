package info.kurozeropb.alcompanion.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.responses.Skin

class SkinRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var images = listOf<Skin>()
    private lateinit var activity: Activity

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_skin, parent, false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ImageViewHolder
        holder.bindView(images[position], activity)
    }

    fun setImages(images: List<Skin>, activity: Activity) {
        this.images = images
        this.activity = activity
        notifyDataSetChanged()
    }

}