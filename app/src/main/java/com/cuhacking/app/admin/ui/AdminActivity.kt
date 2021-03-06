/*
 *    Copyright 2019 cuHacking
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.cuhacking.app.admin.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.cuhacking.app.R
import com.cuhacking.app.admin.data.QrCodeAnalyzer
import com.cuhacking.app.di.injector
import com.cuhacking.app.util.formatTimeDuration
import com.google.android.material.snackbar.Snackbar
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.android.synthetic.main.activity_admin.*

private const val REQUEST_PERMISSIONS_CODE = 10

private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

class AdminActivity : AppCompatActivity(R.layout.activity_admin) {

    private val viewFinder: PreviewView by lazy { findViewById<PreviewView>(R.id.view_finder) }
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>
    private val viewModel: AdminViewModel by viewModels { injector.adminViewModelFactory() }
    private val resultOverlay by lazy { findViewById<ResultOverlay>(R.id.result_overlay) }
    private val vibrator by lazy { getSystemService(Context.VIBRATOR_SERVICE) as Vibrator }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (allPermissionsGranted()) {
            viewFinder.post { startCamera() }
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_PERMISSIONS_CODE)
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "No event selected"

        viewModel.scanStatus.observe(this, Observer {
            it ?: return@Observer
            Snackbar.make(viewFinder, it.messageRes, Snackbar.LENGTH_SHORT).show()

            resultOverlay.updateState(if (it.success) ResultOverlay.State.SUCCESS else ResultOverlay.State.FAILURE)

            if (it.success) {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            50,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(50)
                }
            } else {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createWaveform(
                            longArrayOf(50L, 100L, 50L),
                            intArrayOf(VibrationEffect.DEFAULT_AMPLITUDE, 0, VibrationEffect.DEFAULT_AMPLITUDE),
                            -1
                        )
                    )
                } else {
                    vibrator.vibrate(longArrayOf(0L, 50L, 100L, 50L), -1)
                }
            }
        })

        viewModel.selectedEvent?.observe(this, Observer {
            it ?: return@Observer

            supportActionBar?.title = it.title
            supportActionBar?.subtitle =
                "${it.id} / ${formatTimeDuration(it.startTime, it.endTime)}"
        })
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))

    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview: Preview = Preview.Builder()
            .setTargetName("Preview")
            .build()

        preview.previewSurfaceProvider = viewFinder.previewSurfaceProvider

        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetName("Image Analysis")
            .setTargetResolution(Size(1280, 720))
            .build()

        imageAnalysis.setAnalyzer(
            ContextCompat.getMainExecutor(this),
            QrCodeAnalyzer(viewModel::scan)
        )

        cameraProvider.bindToLifecycle(
            this,
            CameraSelector.DEFAULT_BACK_CAMERA,
            preview,
            imageAnalysis
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (allPermissionsGranted()) {
                viewFinder.post { startCamera() }
            } else {
                Toast.makeText(this, "Permissions not granted by user.", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }

    /**
     * Check if all our required permissions have been granted.
     */
    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
            R.id.select_event -> openSelectionDialog()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.admin, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun openSelectionDialog() {
        val items =
            viewModel.scanEvents.value?.map { "${it.id}//${it.title}" }?.toTypedArray() ?: return
        AlertDialog.Builder(this)
            .setItems(items) { _, which ->
                viewModel.setSelectedEvent(viewModel.scanEvents.value!![which])
            }
            .show()
    }
}
