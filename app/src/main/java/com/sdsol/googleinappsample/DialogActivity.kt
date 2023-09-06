package com.sdsol.googleinappsample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.sdsol.googleinappsample.databinding.LayoutDialogItemBinding

class DialogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)
        showReasonDialog()
    }
}

fun AppCompatActivity.showReasonDialog() {

    val layoutDialogItemBinding: LayoutDialogItemBinding =
        LayoutDialogItemBinding.inflate(layoutInflater, null, false)
    val alertDialog = AlertDialog.Builder(this)
    alertDialog.setCancelable(true)
    alertDialog.setView(layoutDialogItemBinding.root)


    val pickImage = this.registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri: Uri = result.data?.data as Uri
            layoutDialogItemBinding.image.apply {
                setImageURI(imageUri)
                visibility = View.VISIBLE
            }
        }
    }

    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                val galleryIntent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                pickImage.launch(galleryIntent)
            }
        }


    layoutDialogItemBinding.btnPickImage.setOnClickListener {
        if (this.checkSelfPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            pickImage.launch(galleryIntent)
        } else {
            requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }



    alertDialog.show()
}