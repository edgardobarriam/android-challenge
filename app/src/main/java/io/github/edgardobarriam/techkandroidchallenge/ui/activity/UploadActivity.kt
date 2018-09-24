package io.github.edgardobarriam.techkandroidchallenge.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
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
            permissionHandle()
        }

        button_upload_camera.setOnClickListener {
            pickImageFromCamera()
        }
    }

    @SuppressLint("NewApi")
    private fun permissionHandle() {
        if ( hasStoragePermission() ) {
            pickImageFromGallery()
        } else {
            val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
            requestPermissions(permission, PERMISSION_CODE_STORAGE)
        }
    }

    private fun hasStoragePermission() : Boolean {
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    private fun pickImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent, PICK_IMAGE_FROM_GALLERY)
    }

    private fun pickImageFromCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, TAKE_IMAGE_FROM_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode) {
            PICK_IMAGE_FROM_GALLERY -> {
                val imageuri = data!!.data
                val imageFile = File(imageuri.path)

                val requestFile = RequestBody.create(MediaType.parse("image/*"), imageFile)
                val fileBody = MultipartBody.Part.createFormData("image", imageFile.name, requestFile)

                val title = RequestBody.create(MediaType.parse("text/plain"), "TitleTest")

                val description = RequestBody.create(MediaType.parse("text/plain"), "TestDescription")

                //TODO: File upload not working
                disposable = imgurApiService.postImage(fileBody, title, description)
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
            TAKE_IMAGE_FROM_CAMERA -> {
                //TODO: Upload from camera
                val selectedImage = data!!.extras.get("data") as Bitmap
                imageView_uploaded_image.setImageBitmap(selectedImage)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode) { PERMISSION_CODE_STORAGE ->
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery()
            }
        }
    }

    companion object {
        const val PICK_IMAGE_FROM_GALLERY = 0
        const val TAKE_IMAGE_FROM_CAMERA = 1
        const val PERMISSION_CODE_STORAGE = 42
    }
}
