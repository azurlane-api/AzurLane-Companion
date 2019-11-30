@file:Suppress("DEPRECATION")

package info.kurozeropb.alcompanion.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.hendraanggrian.pikasso.into
import com.hendraanggrian.pikasso.picasso
import com.stfalcon.frescoimageviewer.ImageViewer
import info.kurozeropb.alcompanion.*
import info.kurozeropb.alcompanion.helpers.GlideApp
import info.kurozeropb.alcompanion.responses.Skin
import kotlinx.android.synthetic.main.overlay.view.*
import kotlinx.android.synthetic.main.card_skin.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

lateinit var file: File

class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    @SuppressLint("InflateParams")
    fun bindView(skin: Skin, activity: Activity) {
        itemView.title_skin.text = skin.title ?: ""
        GlideApp.with(itemView.context)
            .load(skin.image)
            .into(itemView.img_skin)

        itemView.img_skin.onClick {
            val overlay = View.inflate(itemView.context, R.layout.overlay, null)
            overlay.btn_share.onClick {
                picasso.load(skin.image).into {
                    onFailed { e, _ ->
                        Snackbar.make(itemView, e.message ?: "Something went wrong", Snackbar.LENGTH_LONG).show()
                    }
                    onLoaded { bitmap, _ ->
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "image/png"

                        val bytes = ByteArrayOutputStream()
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bytes)

                        val sdCard = Environment.getExternalStorageDirectory()
                        val dir = File(sdCard.absolutePath + "/AzurLane")
                        if (dir.exists().not())
                            dir.mkdirs()

                        file = File(dir, "share-${skin.title?.toLowerCase(Locale.getDefault())?.replace(" ", "-")}.png")

                        try {
                            file.createNewFile()
                            val fo = FileOutputStream(file)
                            fo.write(bytes.toByteArray())
                            fo.flush()
                            fo.close()
                        } catch (e: IOException) {
                            val message = e.message ?: "Unable to save/share image"
                            Snackbar.make(itemView, message, Snackbar.LENGTH_LONG).show()
                        }

                        val uri = FileProvider.getUriForFile(itemView.context, itemView.context.applicationContext.packageName + ".ImageFileProvider", file)
                        intent.putExtra(Intent.EXTRA_STREAM, uri)

                        startActivityForResult(activity, Intent.createChooser(intent,"Share Image"), App.SHARE_IMAGE, null)

                        file.deleteOnExit()
                    }
                }
            }

            overlay.btn_save.onClick {
                Api.downloadAndSave(
                    skin.title?.toLowerCase(Locale.getDefault())?.replace(" ", "-") ?: "unkown",
                    skin.image ?: "",
                    itemView
                )
            }

            ImageViewer.Builder(itemView.context, arrayOf(skin.image))
                .setOverlayView(overlay)
                .show()
        }
    }

}