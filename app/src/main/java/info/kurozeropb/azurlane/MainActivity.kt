package info.kurozeropb.azurlane

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import com.github.kittinunf.fuel.core.FuelManager
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_ship.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_main)

        FuelManager.instance.basePath = API.baseUrl
        FuelManager.instance.baseHeaders = mapOf("Authorization" to (API.getSecretKey() ?: ""), "User-Agent" to "AzurLaneInfo/v${API.version} (https://github.com/azurlane-api/AzurLaneInfo)")

        val inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200
        val outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200

        btn_search.onClick {
            val name = et_search_bar.text.toString()
            if (name.isBlank()) {
                Snackbar.make(mainActivity, "Name can't be empty", Snackbar.LENGTH_LONG)
                return@onClick
            }

            progressBarHolder.animation = inAnimation
            progressBarHolder.visibility = View.VISIBLE
            btn_search.isEnabled = false

            API.getShip(name) {
                val (response, exception) = this
                GlobalScope.launch(Dispatchers.Main) {
                    when {
                        response != null -> {
                            progressBarHolder.animation = outAnimation
                            progressBarHolder.visibility = View.GONE
                            btn_search.isEnabled = true

                            val intent = Intent(this@MainActivity, ShipActivity::class.java)
                            intent.putExtra("name", name)
                            intent.putExtra("ship", Gson().toJson(response.ship))
                            startActivity(intent)
                            finish()
                        }
                        exception != null -> {
                            progressBarHolder.animation = outAnimation
                            progressBarHolder.visibility = View.GONE
                            btn_search.isEnabled = true

                            Timer().schedule(200) {
                                Snackbar.make(shipActivity, exception.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
        }
    }
}
