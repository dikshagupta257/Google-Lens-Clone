package com.codingblocksmodules.googlelensclone.facedetect

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectAnalyzer:ImageAnalysis.Analyzer {
    private val detector = FaceDetection.getClient(
        FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
            .build()
    )

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("FACEDETECT","Image analyzed")

        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(
                it, imageProxy.imageInfo.rotationDegrees
            )

            detector.process(inputImage)
                .addOnSuccessListener { faces ->
                    Log.d("FACEDETECT", "Faces = ${faces.size}")
                    faces.forEach { face ->
                        Log.d("FACEDETECT", """
                            lefteye = ${face.leftEyeOpenProbability}
                            righteye = ${face.rightEyeOpenProbability}
                            smile = ${face.smilingProbability}
                        """.trimIndent())
                    }
                }.addOnFailureListener{ex ->
                    Log.e("FACEDETECT","Detection failed",ex)
                }.addOnCompleteListener{
                    imageProxy.close()
                }
        }?: imageProxy.close()  // close if image not found either
    }
}