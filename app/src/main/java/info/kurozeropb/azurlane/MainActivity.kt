package info.kurozeropb.azurlane

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onEditorAction
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.fadein, R.anim.fadeout)
        setContentView(R.layout.activity_main)

        window.apply {
            statusBarColor = Color.WHITE
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        val names = Gson().fromJson<List<String>>(intent.getStringExtra("names"), object : TypeToken<List<String>?>() {}.type)
        ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names).also { adapter ->
            et_search_bar.setAdapter(adapter)
        }

        btn_search.onClick { searchShip() }
        et_search_bar.onEditorAction { _, actionId, _ ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> searchShip()
            }
        }
    }

    private fun searchShip() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mainActivity.windowToken, 0)

        val name = et_search_bar.text.toString()
        if (name.isBlank()) {
            Snackbar.make(mainActivity, "Name can't be empty", Snackbar.LENGTH_LONG)
            return
        }

        val inAnimation = AlphaAnimation(0f, 1f)
        inAnimation.duration = 200
        val outAnimation = AlphaAnimation(1f, 0f)
        outAnimation.duration = 200

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
                            Snackbar.make(mainActivity, exception.message ?: "Unkown Error", Snackbar.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}
