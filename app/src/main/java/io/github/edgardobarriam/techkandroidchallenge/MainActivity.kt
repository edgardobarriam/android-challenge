package io.github.edgardobarriam.techkandroidchallenge

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_gallery.onClick { startActivity<MainActivity>() }
        button_upload.onClick {startActivity<MainActivity>() }
        button_settings.onClick { startActivity<MainActivity>() }
        button_exit.onClick { finish() }
    }
}
