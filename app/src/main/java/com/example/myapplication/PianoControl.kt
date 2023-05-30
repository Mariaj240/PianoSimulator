package com.example.myapplication

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message

class PianoControl(val view: PianoView, val context: Context) {
    val model: PianoModel = PianoModel()
    val pianoSound: PianoSoundPlayer = PianoSoundPlayer(context)
    fun play() {
        for (wKey in model.whiteKeys) {
            if (wKey.isClicked) {
                if (!pianoSound.threads.containsKey(wKey.soundIndex)) {
                    pianoSound.playSound(wKey.soundIndex)
                    view.invalidate()
                } else {
                    releaseKey(wKey)
                }
            } else {
                pianoSound.stopSound(wKey.soundIndex)
                releaseKey(wKey)
            }
        }
    for (bKey in model.blackKeys)
    {
        if (bKey.isClicked) {
            if (!pianoSound.threads.containsKey(bKey.soundIndex)) {
                pianoSound.playSound(bKey.soundIndex)
                view.invalidate()
            } else {
                releaseKey(bKey)
            }
        } else {
            pianoSound.stopSound(bKey.soundIndex)
            releaseKey(bKey)
        }
    }
}

private val handler: Handler = object : Handler(Looper.getMainLooper()) {
    override fun handleMessage(msg: Message) {
        view.invalidate()
    }
}

private fun releaseKey(pianoKey: PianoKey) {
    handler.postDelayed(Runnable {
        pianoKey.isClicked = false
        handler.sendEmptyMessage(0)
    }, 100)
}
}
