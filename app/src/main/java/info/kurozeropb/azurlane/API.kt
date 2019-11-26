package info.kurozeropb.azurlane

import android.content.Context
import android.os.Build
import com.github.kittinunf.fuel.httpGet
import com.google.gson.Gson
import info.kurozeropb.azurlane.responses.Response
import info.kurozeropb.azurlane.responses.ShipResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception
import com.github.kittinunf.result.Result
import info.kurozeropb.azurlane.responses.AllShipsResponse

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
}