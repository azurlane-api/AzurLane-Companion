package info.kurozeropb.azurlane.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.kurozeropb.azurlane.R
import info.kurozeropb.azurlane.responses.AllShip
import info.kurozeropb.azurlane.responses.Ships

class ShipRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var mainView: View
    private var images = listOf<AllShip>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ShipViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_ship, parent, false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ShipViewHolder
        holder.bindView(images[position], mainView)
    }

    fun setImages(images: Ships, mainView: View) {
        this.mainView = mainView
        this.images = images
        notifyDataSetChanged()
    }
}