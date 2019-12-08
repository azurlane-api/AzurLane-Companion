package info.kurozeropb.alcompanion.adapters

import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import info.kurozeropb.alcompanion.R
import info.kurozeropb.alcompanion.helpers.GlideApp
import info.kurozeropb.alcompanion.responses.Nation
import info.kurozeropb.alcompanion.ui.MainActivity
import info.kurozeropb.alcompanion.ui.NationsActivity
import kotlinx.android.synthetic.main.card_nation.view.*
import kotlinx.android.synthetic.main.card_ship.view.progress
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*

class NationsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)

    fun bindView(nation: Nation, activity: NationsActivity) {

        val nations = mapOf(
                "eagle union" to ContextCompat.getColor(itemView.context, R.color.eagle_union),
                "royal navy" to ContextCompat.getColor(itemView.context, R.color.royal_navy),
                "sakura empire" to ContextCompat.getColor(itemView.context, R.color.sakura_empire),
                "ironblood" to ContextCompat.getColor(itemView.context, R.color.ironblood),
                "eastern radiance" to ContextCompat.getColor(itemView.context, R.color.eastern_radiance),
                "north union" to ContextCompat.getColor(itemView.context, R.color.north_union),
                "iris libre" to ContextCompat.getColor(itemView.context, R.color.iris_libre),
                "vichya dominion" to ContextCompat.getColor(itemView.context, R.color.vichya_dominion),
                "sardegna empire" to ContextCompat.getColor(itemView.context, R.color.sardegna_empire),
                "neptunia" to ContextCompat.getColor(itemView.context, R.color.neptunia),
                "bilibili" to ContextCompat.getColor(itemView.context, R.color.bilibili),
                "utawarerumono" to ContextCompat.getColor(itemView.context, R.color.utawarerumono),
                "kizunaai" to ContextCompat.getColor(itemView.context, R.color.kizunaai)
        )

        if (nation.name != null) {
            val name = nation.name.toLowerCase(Locale.getDefault())
            itemView.iv_nation_avatar.backgroundColor = nations[name] ?: ContextCompat.getColor(itemView.context, R.color.background)
        }

        itemView.tv_nation_name.text = nation.name ?: "-"
        itemView.cv_nation.onClick {
            val intent = Intent(activity, MainActivity::class.java)
            intent.putExtra("nation", nation.name)
            intent.putExtra("icon", nation.icon)
            activity.startActivity(intent)
            activity.finish()
        }

        GlideApp.with(itemView.context)
                .load(nation.icon)
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
                .into(itemView.iv_nation_avatar)
    }

}