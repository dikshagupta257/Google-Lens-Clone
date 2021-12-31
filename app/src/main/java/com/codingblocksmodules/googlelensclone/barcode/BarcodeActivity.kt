package com.codingblocksmodules.googlelensclone.barcode

import androidx.core.content.ContextCompat
import com.codingblocksmodules.googlelensclone.BaseLensActivity


class BarcodeActivity : BaseLensActivity() {

    override val imageAnalyzer = BarcodeAnalyzer()
    override fun startScanner() {
        scanBarcode()
    }
    private fun scanBarcode() {
       imageAnalysis.setAnalyzer(
           ContextCompat.getMainExecutor(this),
           imageAnalyzer
       )
    }

}