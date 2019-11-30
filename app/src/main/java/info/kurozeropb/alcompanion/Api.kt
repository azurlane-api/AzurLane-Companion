@file:Suppress("DEPRECATION")

package info.kurozeropb.alcompanion

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import info.kurozeropb.alcompanion.responses.Response
import info.kurozeropb.alcompanion.responses.ShipResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import com.github.kittinunf.result.Result
import com.google.android.material.snackbar.Snackbar
import info.kurozeropb.alcompanion.responses.AllShipsResponse
import info.kurozeropb.alcompanion.ui.ShipActivity
import kotlinx.android.synthetic.main.content_main.view.*
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.concurrent.schedule

object Api {
    const val baseUrl = "https://azurlane-api.herokuapp.com/v2"

    init {
        System.loadLibrary("api")
    }

    external fun getSecretKey(): String?

    fun getVersionName(ctx: Context): String {
        val packageInfo = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        return packageInfo.versionName
    }

    fun getVersionCode(ctx: Context): String {
        val packageInfo = ctx.packageManager.getPackageInfo(ctx.packageName, 0)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String.format("%03d", packageInfo.longVersionCode)
        } else {
            @Suppress("DEPRECATION")
            String.format("%03d", packageInfo.versionCode)
        }
    }

    fun getShip(name: String, complete: Response<ShipResponse?, Exception?>.() -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            "/ship".httpGet(parameters = listOf("name" to name))
                .timeout(31000)
                .timeoutRead(60000)
                .responseString { result ->
                    val (data, exception) = result
                    val response = when (result) {
                        is Result.Success -> {
                            if (data != null) {
                                Response(Gson().fromJson(data, ShipResponse::class.java), null)
                            } else {
                                Response(null, Exception("No data returned"))
                            }
                        }
                        is Result.Failure -> {
                            if (exception != null) {
                                Response(null, exception)
                            } else {
                                Response(null, Exception("No data returned"))
                            }
                        }
                    }
                    complete(response)
                }
        }
    }

    fun getAllShips(complete: Response<AllShipsResponse?, Exception?>.() -> Unit) {
        GlobalScope.launch(Dispatchers.IO) {
            "/ships/all".httpGet()
                .timeout(31000)
                .timeoutRead(60000)
                .responseString { result ->
                    val (data, exception) = result
                    val response = when (result) {
                        is Result.Success -> {
                            if (data != null) {
                                Response(Gson().fromJson(data, AllShipsResponse::class.java), null)
                            } else {
                                Response(null, Exception("No data returned"))
                            }
                        }
                        is Result.Failure -> {
                            if (exception != null) {
                                Response(null, exception)
                            } else {
                                Response(null, Exception("No data returned"))
                            }
                        }
                    }
                    complete(response)
                }
        }
    }

    fun downloadAndSave(name: String, url: String, view: View) {
        val mediaStorageDir = File(Environment.getExternalStorageDirectory().toString() + "/AzurLane/")
        if (!mediaStorageDir.exists()) mediaStorageDir.mkdirs()
        val file = File(mediaStorageDir, "${name}.png")

        Fuel.download(url).fileDestination { response, _ ->
            response.toString()
            file
        }.response { _, _, result ->
            val (data, err) = result
            when {
                data != null -> {
                    val fileOutput = FileOutputStream(file)
                    fileOutput.write(data, 0, data.size)

                    view.context.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(view.context, "Saved as ${name}.png", Toast.LENGTH_LONG).show()
                    }
                    fileOutput.close()
                }
                err != null -> {
                    GlobalScope.launch(Dispatchers.Main) {
                        Toast.makeText(view.context, err.message ?: "ERROR", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    fun searchShip(name: String, view: View) {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        if (name.isBlank()) {
            Snackbar.make(view, "Name can't be empty", Snackbar.LENGTH_LONG)
            return
        }

        val inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200
        val outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200

        view.progressBarHolder.animation = inAnimation
        view.progressBarHolder.visibility = View.VISIBLE

        getShip(name) {
            val (response, exception) = this
            GlobalScope.launch(Dispatchers.Main) {
                when {
                    response != null -> {
                        view.progressBarHolder.animation = outAnimation
                        view.progressBarHolder.visibility = View.GONE

                        val intent = Intent(view.context, ShipActivity::class.java)
                        intent.putExtra("name", name)
                        intent.putExtra("ship", Gson().toJson(response.ship))
                        view.context.startActivity(intent)
                    }
                    exception != null -> {
                        view.progressBarHolder.animation = outAnimation
                        view.progressBarHolder.visibility = View.GONE

                        Timer().schedule(200) {
                            Snackbar.make(view, exception.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}