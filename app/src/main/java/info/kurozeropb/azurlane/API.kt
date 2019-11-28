package info.kurozeropb.azurlane

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.widget.Toast
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import info.kurozeropb.azurlane.responses.Response
import info.kurozeropb.azurlane.responses.ShipResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import com.github.kittinunf.result.Result
import com.google.android.material.snackbar.Snackbar
import info.kurozeropb.azurlane.responses.AllShip
import info.kurozeropb.azurlane.responses.AllShipsResponse
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.util.*

object API {
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
}