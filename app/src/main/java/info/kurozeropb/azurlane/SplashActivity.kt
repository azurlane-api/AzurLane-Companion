package info.kurozeropb.azurlane

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.core.FuelManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_splash)

        FuelManager.instance.basePath = API.baseUrl
        FuelManager.instance.baseHeaders = mapOf("Authorization" to (API.getSecretKey() ?: ""), "User-Agent" to "AzurLaneInfo/v${API.version} (https://github.com/azurlane-api/AzurLaneInfo)")

        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
        logo.startAnimation(pulse)

        API.getShipNames {
            val (response, exception) = this
            GlobalScope.launch(Dispatchers.Main) {
                when {
                    response != null -> {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        intent.putExtra("names", Gson().toJson(response.ships))
                        Timer().schedule(10000) {
                            startActivity(intent)
                            finish()
                        }
                    }
                    exception != null -> {
                        Timer().schedule(200) {
                            Snackbar.make(splashActivity, exception.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }

}