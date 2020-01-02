package com.cuhacking.app.admin.data

import android.annotation.SuppressLint
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import com.google.android.gms.tasks.Task
import com.google.firebase.ml.vision.FirebaseVision
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions
import com.google.firebase.ml.vision.common.FirebaseVisionImage
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata

class QrCodeAnalyzer(
    private val onQrCodesDetected: (qrCodes: List<FirebaseVisionBarcode>) -> Unit
) : ImageAnalysis.Analyzer {

    private val options = FirebaseVisionBarcodeDetectorOptions.Builder()
        .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
        .build()

    private val detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options)

    @SuppressLint("UnsafeExperimentalUsageError")
    override fun analyze(image: ImageProxy) {
        val rotation = rotationDegreesToFirebaseRotation(image.imageInfo.rotationDegrees)
        val visionImage = FirebaseVisionImage.fromMediaImage(image.image!!, rotation)

        detector.detectInImage(visionImage)
            .addOnSuccessListener { barcodes ->
                onQrCodesDetected(barcodes)
            }
            .addOnFailureListener{
            }

        image.close()
    }

    private fun rotationDegreesToFirebaseRotation(rotationDegrees: Int) : Int {
        return when(rotationDegrees) {
            0 -> FirebaseVisionImageMetadata.ROTATION_0
            90 -> FirebaseVisionImageMetadata.ROTATION_90
            180 -> FirebaseVisionImageMetadata.ROTATION_180
            270 -> FirebaseVisionImageMetadata.ROTATION_270
            else -> throw IllegalArgumentException("Not supported")
        }
    }
}