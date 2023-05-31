package com.example.myapplication

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.os.Handler
import android.os.Looper

class PianoView(context: Context, attribute: AttributeSet) :
    View(context, attribute) {
    val pianoControl = PianoControl(this, context)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var bSoundIndex = 8
        pianoControl.model.heightKey = h
        pianoControl.model.widthKey = w / pianoControl.model.countKeys
        for (i in 0 until pianoControl.model.countKeys) {
            var left = i * pianoControl.model.widthKey
            var right = left + pianoControl.model.widthKey
            if (i == pianoControl.model.countKeys - 1) {
                right = w
            }
            val rectF = RectF(left.toFloat(), 0f, right.toFloat(), h.toFloat())
            pianoControl.model.whiteKeys.add(PianoKey(rectF, i + 1))
            if (i == 1 || i == 2 || i == 4 || i == 5 || i == 6) {
                var leftB = (i - 1) * pianoControl.model.widthKey + pianoControl.model.widthKey * 0.75
                var rightB = i * pianoControl.model.widthKey + pianoControl.model.widthKey * 0.25
                val rectB = RectF(leftB.toFloat(), 0f, rightB.toFloat(), h * 0.6f)
                pianoControl.model.blackKeys.add(PianoKey(rectB, bSoundIndex))
                bSoundIndex += 1
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        for (wKey in pianoControl.model.whiteKeys) {
            canvas?.drawRect(wKey.rectF, if (wKey.isClicked) pianoControl.model.paints[2]
            else pianoControl.model.paints[1])
        }
        for (line in 1 until pianoControl.model.countKeys) {
            canvas?.drawLine(
                (line * pianoControl.model.widthKey).toFloat(), 0f,
                (line * pianoControl.model.widthKey).toFloat(),
                pianoControl.model.heightKey.toFloat(), pianoControl.model.paints[0]
            )
        }
        for (bKey in pianoControl.model.blackKeys) {
            canvas?.drawRect(bKey.rectF, if (bKey.isClicked) pianoControl.model.paints[2]
            else pianoControl.model.paints[0])
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        val isCorrect = (action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_MOVE)
        for (i in 0 until event!!.pointerCount) {
            val x = event.getX(i)
            val y = event.getY(i)
            var key: PianoKey? = findKey(x,y)

            if (key != null) {
                key.isClicked = isCorrect
            }
        }
        pianoControl.play()
        return true
    }
    fun findKey(x:Float, y:Float) : PianoKey?{

        for (bKey in pianoControl.model.blackKeys) {
            if (bKey.rectF.contains(x, y)) {
                return bKey

            }
        }
        for (wKey in pianoControl.model.whiteKeys) {
            if (wKey.rectF.contains(x, y)) {
                return wKey
            }
        }
        return null
    }
}

