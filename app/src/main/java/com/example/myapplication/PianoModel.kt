package com.example.myapplication

import android.graphics.Paint

class PianoModel {
    val countKeys = 7
    val whiteKeys: MutableList<PianoKey>
    val blackKeys: MutableList<PianoKey>
    val paints: List<Paint>
    var widthKey: Int
    var heightKey: Int
    init {
        whiteKeys = mutableListOf<PianoKey>()
        blackKeys = mutableListOf<PianoKey>()
        val paint1 = Paint()
        paint1.setColor(android.graphics.Color.BLACK)
        val paint2 = Paint()
        paint2.setColor(android.graphics.Color.WHITE)
        paints = listOf<Paint>(paint1, paint2)
        widthKey = 0
        heightKey = 0
    }
}