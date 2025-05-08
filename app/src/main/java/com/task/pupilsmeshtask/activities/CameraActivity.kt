package com.task.pupilsmeshtask.activities

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.camera.core.AspectRatio
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageAnalysis.OUTPUT_IMAGE_FORMAT_RGBA_8888
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.mediapipe.examples.facedetection.FaceDetectorHelper
import com.google.mediapipe.examples.facedetection.OverlayView
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.task.pupilsmeshtask.R
import com.task.pupilsmeshtask.Type
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class CameraActivity:Fragment() {

    private val TAG = CameraActivity::class.java.name

    lateinit var cameraTexture:TextureView

    lateinit var backgroundExecutor:ExecutorService
    lateinit var faceDetectorHelper: FaceDetectorHelper
    lateinit var imageAnalyzer: ImageAnalysis
    lateinit var cameraProvider: ProcessCameraProvider
    lateinit var preview:Preview
    lateinit var camera:Camera
    lateinit var previewView:PreviewView
    lateinit var overlayView:OverlayView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_camera2,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        previewView = view.findViewById(R.id.view_finder)
        overlayView = view.findViewById(R.id.overlay)

        var toolbar:Toolbar = requireActivity().findViewById(R.id.toolbar)

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.title = getString(R.string.camera)


        backgroundExecutor = Executors.newSingleThreadExecutor()

        // Create the FaceDetectionHelper that will handle the inference
        backgroundExecutor.execute {
            faceDetectorHelper =
                FaceDetectorHelper(
                    context = requireActivity().applicationContext,
                    threshold = 0.5f,
                    currentDelegate = FaceDetectorHelper.DELEGATE_CPU,
                    faceDetectorListener = object : FaceDetectorHelper.DetectorListener {
                        override fun onError(error: String, errorCode: Int) {

                            Log.d(TAG, "onError: " + error)



                        }

                        override fun onResults(resultBundle: FaceDetectorHelper.ResultBundle) {

//                            Log.d(CameraUtils::class.java.name
//                                , "onResults: "+String.format("%d ms", resultBundle.inferenceTime))

                           requireActivity().runOnUiThread {

                                val detectionResult = resultBundle.results[0]

                                overlayView.setResults(
                                    detectionResult,
                                    resultBundle.inputImageHeight,
                                    resultBundle.inputImageWidth
                                )


                            }

                        }

                    },
                    runningMode = RunningMode.LIVE_STREAM
                )

            // Set up the camera and its use cases

            try {

                setUpCamera()

            } catch (ex:Exception) {


            }

        }
    }

    private fun setUpCamera() {
        val cameraProviderFuture =
            ProcessCameraProvider.getInstance(requireActivity())
        cameraProviderFuture.addListener(
            {
                // CameraProvider
                cameraProvider = cameraProviderFuture.get()

                // Build and bind the camera use cases
                bindCameraUseCases()
            },
            ContextCompat.getMainExecutor(requireActivity())
        )
    }

    private fun bindCameraUseCases() {

        // CameraProvider
        val cameraProvider =
            cameraProvider
                ?: throw IllegalStateException("Camera initialization failed.")

        // CameraSelector - makes assumption that we're only using the back camera
        val cameraSelector =
            CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_FRONT).build()

        // Preview. Only using the 4:3 ratio because this is the closest to our models
        preview =
            Preview.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setTargetRotation(previewView.display.rotation)
                .build()

        // ImageAnalysis. Using RGBA 8888 to match how our models work
        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(AspectRatio.RATIO_16_9)
                .setTargetRotation(previewView.display.rotation)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .setOutputImageFormat(OUTPUT_IMAGE_FORMAT_RGBA_8888)
                .build()
                // The analyzer can then be assigned to the instance
                .also {
                    it.setAnalyzer(
                        backgroundExecutor,
                        faceDetectorHelper::detectLivestreamFrame
                    )
                }

        // Must unbind the use-cases before rebinding them
        cameraProvider.unbindAll()

        try {
            // A variable number of use-cases can be passed here -
            // camera provides access to CameraControl & CameraInfo
            camera = cameraProvider.bindToLifecycle(
                this,
                cameraSelector,
                preview,
                imageAnalyzer
            )

            // Attach the viewfinder's surface provider to preview use case
            preview?.setSurfaceProvider(previewView.surfaceProvider)
        } catch (exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).selectIcon(Type.CAMERA)
    }


}