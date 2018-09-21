package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import io.github.edgardobarriam.techkandroidchallenge.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk27.coroutines.onClick

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_gallery.onClick { startActivity<TagListActivity>() }
        button_upload.onClick { startActivity<UploadActivity>() }
        button_settings.onClick { startActivity<GalleryActivity>() }
        button_exit.onClick { finish() }
    }

}