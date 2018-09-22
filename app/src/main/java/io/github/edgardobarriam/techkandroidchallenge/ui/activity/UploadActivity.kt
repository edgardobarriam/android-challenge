package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.github.edgardobarriam.techkandroidchallenge.R
import io.github.edgardobarriam.techkandroidchallenge.server.ImgurApiService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_upload.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import java.io.File


class UploadActivity : AppCompatActivity() {

    private val imgurApiService by lazy {
        ImgurApiService.getInstance()
    }
    var disposable: Disposable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload)


        button_upload_gallery.setOnClickListener {
            permissionhandle()
        }
        button_upload_camera.onClick {
            pickImageFromCamera()

        }
    }

    fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 0)

    }

    fun pickImageFromCamera() {

    }

    fun permissionhandle() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, 42)
            } else {
                pickImageFromGallery()
            }
        } else {
            pickImageFromGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            0 -> {
                // Pick from gallery
                val imageuri = data!!.data
                val imageFile = File(imageuri.path)

                // Create image Part
                val requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
                val fileBody = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

                // Create title Part
                val title = RequestBody.create(MediaType.parse("text/plain"), "TitleTest")

                //TODO: Missing file upload
                disposable = imgurApiService.postImage(fileBody, title)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {result ->
                                toast("Success")
                            },
                            {error -> toast(error.message!!)}
                    )

                imageView_uploaded_image.setImageURI(imageuri)
            }
            1 -> {
                //TODO: Take from camera
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) { 42 ->
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            }
        }
    }
}
