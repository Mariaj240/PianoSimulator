package com.example.myapplication

import android.graphics.RectF

class PianoKey(val rectF: RectF, val soundIndex: Int) {
    //    ссылка на звук
    var isClicked: Boolean = false
}