package com.codingblocksmodules.googlelensclone

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.codingblocksmodules.googlelensclone.databinding.ActivityLensBinding

abstract class BaseLensActivity:AppCompatActivity() {
    companion object{
        @JvmStatic
        val CAMERA_PERM_CODE = 422
    }

    abstract val imageAnalyzer : ImageAnalysis.Analyzer
    protected lateinit var imageAnalysis: ImageAnalysis
    private lateinit var binding :ActivityLensBinding

    abstract fun startScanner()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLensBinding.inflate(layoutInflater)
        setContentView(binding.root)

        askCameraPermission()

        binding.btnStartScanner.setOnClickListener {
            Toast.makeText(this, "Scanning started!", Toast.LENGTH_SHORT).show()
            startScanner()
        }
    }

    private fun askCameraPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
            CAMERA_PERM_CODE)
    }

    private fun startCamera() {
        val cameraFutureProvider = ProcessCameraProvider.getInstance(this)
        cameraFutureProvider.addListener(
            {
                val cameraProvider = cameraFutureProvider.get()
                val preview  = Preview.Builder().build().also {
                    it.setSurfaceProvider(binding.previewBarcode.createSurfaceProvider())
                }

                imageAnalysis = ImageAnalysis.Builder().build()
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try{
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalysis)
                }catch(e : Exception){
                    Log.e("CAM", "Error binding camera ",e)
                }
            },
            ContextCompat.getMainExecutor(this)
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

}