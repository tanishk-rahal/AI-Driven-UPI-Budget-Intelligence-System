package com.example.budgetmanager.ui.scan

import android.Manifest
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.budgetmanager.R
import com.example.budgetmanager.ui.payment.PaymentActivity
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class ScanActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var cameraPreview: PreviewView
    private var camera: Camera? = null
    
    // Scanner set to handle all formats to be more robust
    private val scanner = BarcodeScanning.getClient(
        BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
            .build()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        cameraPreview = findViewById(R.id.camera_preview)
        cameraExecutor = Executors.newSingleThreadExecutor()

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        findViewById<ImageView>(R.id.back_button).setOnClickListener { finish() }

        // Scanning line animation
        val scanLine = findViewById<View>(R.id.scan_line)
        ObjectAnimator.ofFloat(scanLine, "translationY", 0f, 600f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }
        
        // Tap to focus
        cameraPreview.setOnTouchListener { _, event ->
            val factory = cameraPreview.meteringPointFactory
            val point = factory.createPoint(event.x, event.y)
            val action = FocusMeteringAction.Builder(point, FocusMeteringAction.FLAG_AF)
                .setAutoCancelDuration(3, TimeUnit.SECONDS)
                .build()
            camera?.cameraControl?.startFocusAndMetering(action)
            true
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(cameraPreview.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(imageProxy)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                camera = cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )
                // Enable Auto Focus if available
                camera?.cameraControl?.enableTorch(false)
            } catch (exc: Exception) {
                Log.e("ScanActivity", "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processImageProxy(imageProxy: ImageProxy) {
        val mediaImage = imageProxy.image
        if (mediaImage != null) {
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    if (barcodes.isNotEmpty()) {
                        val rawValue = barcodes[0].rawValue
                        if (rawValue != null) {
                            runOnUiThread { handleScannedData(rawValue) }
                        }
                    }
                }
                .addOnFailureListener { Log.e("ScanActivity", "Scan failed", it) }
                .addOnCompleteListener { imageProxy.close() }
        } else {
            imageProxy.close()
        }
    }

    private fun handleScannedData(data: String) {
        Log.d("ScanActivity", "Data: $data")
        
        val intent = Intent(this, PaymentActivity::class.java)
        
        if (data.startsWith("upi://", ignoreCase = true)) {
            val uri = Uri.parse(data)
            intent.putExtra("UPI_ID", uri.getQueryParameter("pa"))
            intent.putExtra("PAYEE_NAME", uri.getQueryParameter("pn") ?: "Merchant")
        } else if (data.contains("@")) {
            // Case where QR is just the UPI ID string
            intent.putExtra("UPI_ID", data)
            intent.putExtra("PAYEE_NAME", "Recipient")
        } else {
            // General QR - maybe it's just a name or random text
            Toast.makeText(this, "Not a valid UPI QR: $data", Toast.LENGTH_SHORT).show()
            return 
        }

        // Only launch if we have a UPI ID
        if (intent.getStringExtra("UPI_ID") != null) {
            startActivity(intent)
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) startCamera() else finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
        scanner.close()
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}
