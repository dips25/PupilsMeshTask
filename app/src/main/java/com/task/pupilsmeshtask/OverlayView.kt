/*
 * Copyright 2023 The TensorFlow Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *             http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.mediapipe.examples.facedetection

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import com.google.mediapipe.tasks.vision.facedetector.FaceDetectorResult
import com.task.pupilsmeshtask.R
import kotlin.math.min

class OverlayView(context: Context?, attrs: AttributeSet?) :
    View(context, attrs) {

    private var results: FaceDetectorResult? = null
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var mainRectPaint = Paint()

    private var scaleFactor: Float = 1f

    private var bounds = Rect()

     private var mainRect:RectF?=null

    private var MAIN_RECT_WIDTH = 0f;
    private var MAIN_RECT_HEIGHT = 0f;

    private var CENTERX = 0f;
    private var CENTERY = 0f;

    private var BOX_INSET = 0f

    init {
        initPaints()
    }

    fun clear() {
        results = null
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        textBackgroundPaint.color = Color.TRANSPARENT
        textBackgroundPaint.style = Paint.Style.FILL
        textBackgroundPaint.textSize = 50f

        textPaint.color = Color.WHITE
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f

        boxPaint.color = ContextCompat.getColor(context!!, android.R.color.transparent)
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE



        mainRectPaint.color = ContextCompat.getColor(context!!, android.R.color.holo_red_dark)
        mainRectPaint.strokeWidth = 8F
        mainRectPaint.style = Paint.Style.STROKE

        post {

            CENTERX = width/2f
            CENTERY = height/2f

            MAIN_RECT_WIDTH = width*0.35f
            MAIN_RECT_HEIGHT = width*0.35f

            BOX_INSET = width*0.069444f

            mainRect = RectF(CENTERX-MAIN_RECT_WIDTH
                ,CENTERY-MAIN_RECT_HEIGHT
                ,CENTERX+MAIN_RECT_WIDTH
                ,CENTERY+MAIN_RECT_HEIGHT)

            invalidate()




        }


        //invalidate()



    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        Log.d(OverlayView::class.java.simpleName, "draw: " + width)

        mainRect?.let {

            canvas.drawRect(mainRect!!,mainRectPaint)

        }



        results?.let {
            for (detection in it.detections()) {
                val boundingBox = detection.boundingBox()

                val top = (boundingBox.top * scaleFactor)+BOX_INSET
                val bottom = (boundingBox.bottom * scaleFactor)-BOX_INSET
                val left = (boundingBox.left * scaleFactor)+BOX_INSET
                val right = (boundingBox.right * scaleFactor)-BOX_INSET

                // Draw bounding box around detected faces
                val drawableRect = RectF(left, top, right, bottom)
                canvas.drawRect(drawableRect, boxPaint)

                // Create text to display alongside detected faces
                val drawableText =
                    detection.categories()[0].categoryName() +
                            " " +
                            String.format(
                                "%.2f",
                                detection.categories()[0].score()
                            )

                // Draw rect behind display text
                textBackgroundPaint.getTextBounds(
                    drawableText,
                    0,
                    drawableText.length,
                    bounds
                )
                val textWidth = bounds.width()
                val textHeight = bounds.height()
                canvas.drawRect(
                    left,
                    top,
                    left + textWidth + Companion.BOUNDING_RECT_TEXT_PADDING,
                    top + textHeight + Companion.BOUNDING_RECT_TEXT_PADDING,
                    textBackgroundPaint
                )



                    if (drawableRect.contains(mainRect!!)
                        || mainRect!!.contains(drawableRect)
                    ) {

                        mainRectPaint.color = Color.GREEN
                        canvas.drawRect(mainRect!!,mainRectPaint)

                    } else {

                        mainRectPaint.color = Color.RED
                        canvas.drawRect(mainRect!!,mainRectPaint)

                    }






//                if (drawableRect!!.intersect(mainRect!!)) {
//
//
//
//                } else {
//
//
//
//
//                }

                // Draw text for detected face
//                canvas.drawText(
//                    drawableText,
//                    left,
//                    top + bounds.height(),
//                    textPaint
//                )


            }
        }
    }

    fun setResults(
        detectionResults: FaceDetectorResult,
        imageHeight: Int,
        imageWidth: Int,
    ) {
        results = detectionResults

        // Images, videos and camera live streams are displayed in FIT_START mode. So we need to scale
        // up the bounding box to match with the size that the images/videos/live streams being
        // displayed.
        scaleFactor = min(width * 1f / imageWidth, height * 1f / imageHeight)

        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}