package com.codingblocksmodules.googlelensclone.textrecog

import androidx.core.content.ContextCompat
import com.codingblocksmodules.googlelensclone.BaseLensActivity

class TextRecognitionActivity: BaseLensActivity() {
    override val imageAnalyzer = TextAnalyzer()

    override fun startScanner() {
        startTextRecognition()
    }

    private fun startTextRecognition() {
        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            imageAnalyzer
        )
    }
}