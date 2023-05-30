package com.example.myapplication

import android.graphics.RectF

class PianoKey(val rectF: RectF, val soundIndex: Int) {
    var isClicked: Boolean = false
}