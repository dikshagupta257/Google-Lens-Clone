package com.codingblocksmodules.googlelensclone.facedetect

import androidx.core.content.ContextCompat
import com.codingblocksmodules.googlelensclone.BaseLensActivity

class FaceDetectActivity:BaseLensActivity() {
    override val imageAnalyzer = FaceDetectAnalyzer()

    override fun startScanner() {
        startFaceDetect()
    }

    private fun startFaceDetect() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
    }
}