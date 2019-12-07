package info.kurozeropb.alcompanion.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.responses.Nation
import info.kurozeropb.alcompanion.responses.Nations
import info.kurozeropb.alcompanion.ui.NationsActivity

class NationsRecyclerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private lateinit var activity: NationsActivity
    private var images = listOf<Nation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NationsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_nation, parent, false))
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as NationsViewHolder
        holder.bindView(images[position], activity)
    }

    fun setImages(images: Nations, activity: NationsActivity) {
        this.activity = activity
        this.images = images
        notifyDataSetChanged()
    }
}