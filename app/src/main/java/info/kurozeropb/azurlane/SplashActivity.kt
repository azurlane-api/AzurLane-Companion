package info.kurozeropb.azurlane

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.core.ImagePipelineConfig
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig
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

        window.apply {
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }

        FuelManager.instance.basePath = API.baseUrl
        FuelManager.instance.baseHeaders = mapOf("Authorization" to (API.getSecretKey() ?: ""), "User-Agent" to "AzurLaneCompanion/v${API.getVersionName(this)} (${API.getVersionCode(this)}) (https://github.com/azurlane-api/AzurLaneInfo)")

        // Initialize fresco for loading fullscreen images
        val config = ImagePipelineConfig.newBuilder(this)
            .setProgressiveJpegConfig(SimpleProgressiveJpegConfig())
            .setResizeAndRotateEnabledForNetwork(true)
            .setDownsampleEnabled(true)
            .build()
        Fresco.initialize(this, config)

        val pulse = AnimationUtils.loadAnimation(this, R.anim.pulse)
        logo.startAnimation(pulse)

        API.getAllShips {
            val (response, exception) = this
            GlobalScope.launch(Dispatchers.Main) {
                when {
                    response != null -> {
                        val intent = Intent(this@SplashActivity, MainActivity::class.java)
                        intent.putExtra("ships", Gson().toJson(response.ships))
                        startActivity(intent)
                        finish()
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