package com.codingblocksmodules.googlelensclone.textrecog

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class TextAnalyzer: ImageAnalysis.Analyzer {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        Log.d("TEXT", "Image analyzed")

        imageProxy.image?.let {
            val inputImage = InputImage.fromMediaImage(
                it, imageProxy.imageInfo.rotationDegrees
            )

            recognizer.process(inputImage)
                .addOnSuccessListener { text ->
                    text.textBlocks.forEach { block ->
                        Log.d(
                            "TEXT", """
                            Lines = ${block.lines.joinToString("\n"){ it1 -> it1.text}}
                        """.trimIndent()
                        )
                    }
                }.addOnFailureListener { ex ->
                    Log.e("TEXT", "Detection failed", ex)
                }.addOnCompleteListener {
                    imageProxy.close()
                }
        } ?: imageProxy.close()  // close if image not found either
    }
}