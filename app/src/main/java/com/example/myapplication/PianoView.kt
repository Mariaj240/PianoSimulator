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

class PianoView(context: Context, attribute: AttributeSet, val model: PianoModel) :
    View(context, attribute) {
    val pianoSound: PianoSoundPlayer = PianoSoundPlayer(context)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        var bSoundIndex = 8
        model.heightKey = h
        model.widthKey = w / model.countKeys
        for (i in 0 until model.countKeys) {
            var left = i * model.heightKey
            var right = left + model.heightKey
            if (i == model.countKeys - 1) {
                right = w
            }
            val rectF = RectF(left.toFloat(), 0f, right.toFloat(), h.toFloat())
            model.whiteKeys.add(PianoKey(rectF, i + 1))
            if (i == 1 || i == 2 || i == 4 || i == 5 || i == 6) {
                var leftB = (i - 1) * model.widthKey + model.widthKey * 0.75
                var rightB = i * model.widthKey + model.widthKey * 0.25
                val rectB = RectF(leftB.toFloat(), 0f, rightB.toFloat(), h * 0.6f)
                model.blackKeys.add(PianoKey(rectB, bSoundIndex))
                bSoundIndex += 1
            }
        }
    }

    override fun onDraw(canvas: Canvas?) {
        for (wKey in model.whiteKeys) {
            canvas?.drawRect(wKey.rectF, model.paints[1])
        }
        for (line in 1 until model.countKeys) {
            canvas?.drawLine(
                (line * model.widthKey).toFloat(), 0f, (line * model.widthKey).toFloat(),
                model.heightKey.toFloat(), model.paints[0]
            )
        }
        for (bKey in model.blackKeys) {
            canvas?.drawRect(bKey.rectF, model.paints[0])
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        val isCorrect = (action == MotionEvent.ACTION_DOWN) || (action == MotionEvent.ACTION_MOVE)
        for (i in 0 until event!!.pointerCount) {
            val x = event.getX(i)
            val y = event.getY(i)
            var key: PianoKey? = null
            for (wKey in model.whiteKeys) {
                if (wKey.rectF.contains(x, y)) {
                    key = wKey
                    break
                }
            }
            for (bKey in model.blackKeys) {
                if (bKey.rectF.contains(x, y)) {
                    key = bKey
                    break
                }
            }
            if (key != null) {
                key.isClicked = isCorrect
            }
        }
        for (wKey in model.whiteKeys) {
            if (wKey.isClicked) {
                if (!pianoSound.threads.containsKey(wKey.soundIndex)) {
                    pianoSound.playSound(wKey.soundIndex)
                    invalidate()
                } else {
                    releaseKey(wKey)
                }
            } else {
                pianoSound.stopSound(wKey.soundIndex)
                releaseKey(wKey)
            }
        }
        for (bKey in model.blackKeys) {
            if (bKey.isClicked) {
                if (!pianoSound.threads.containsKey(bKey.soundIndex)) {
                    pianoSound.playSound(bKey.soundIndex)
                    invalidate()
                } else {
                    releaseKey(bKey)
                }
            } else {
                pianoSound.stopSound(bKey.soundIndex)
                releaseKey(bKey)
            }
        }
        return true
    }

    private val handler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            invalidate()
        }
    }

    private fun releaseKey(pianoKey: PianoKey) {
        handler.postDelayed(Runnable {
            pianoKey.isClicked = false
            handler.sendEmptyMessage(0)
        }, 100)
    }
}

