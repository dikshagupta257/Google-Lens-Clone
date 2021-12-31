package com.codingblocksmodules.googlelensclone

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.codingblocksmodules.googlelensclone.BaseLensActivity.Companion.CAMERA_PERM_CODE
import com.codingblocksmodules.googlelensclone.barcode.BarcodeActivity
import com.codingblocksmodules.googlelensclone.databinding.ActivityMainBinding
import com.codingblocksmodules.googlelensclone.facedetect.FaceDetectActivity
import com.codingblocksmodules.googlelensclone.imagelabeler.ImageLabelingActivity
import com.codingblocksmodules.googlelensclone.textrecog.TextRecognitionActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object{
        @JvmStatic
        val PHOTO_REQ_CODE = 234

        @JvmStatic
        val EXTRA_DATA = "data"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnTakeExtPhoto.setOnClickListener {
            askCameraPermission()
        }

        binding.btnCameraActivity.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        binding.btnBarcodeActivity.setOnClickListener {
            startActivity(Intent(this, BarcodeActivity::class.java))
        }

        binding.btnFaceDetectActivity.setOnClickListener {
            startActivity(Intent(this, FaceDetectActivity::class.java))
        }

        binding.btnLabelerActivity.setOnClickListener {
            startActivity(Intent(this, ImageLabelingActivity::class.java))
        }

        binding.btnTextRecogActivity.setOnClickListener {
            startActivity(Intent(this, TextRecognitionActivity::class.java))
        }
    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERM_CODE
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if(requestCode == CAMERA_PERM_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                startCamera()
            }else{
                AlertDialog.Builder(this)
                    .setTitle("Permission Error")
                    .setMessage("Camera permission not provided")
                    .setPositiveButton("OK"){_,_ ->
                        finish()
                    }
                    .setCancelable(false)
                    .show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startCamera() {
        val takePhotoIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(takePhotoIntent, PHOTO_REQ_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PHOTO_REQ_CODE){
            if(data!=null){
                val bitmap = data.extras?.get(EXTRA_DATA) as Bitmap
                binding.imgThumb.setImageBitmap(bitmap)
            }
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}