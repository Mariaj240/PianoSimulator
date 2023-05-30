package com.example.myapplication

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import java.io.InputStream
import kotlin.math.ln

class PianoSoundPlayer(val context: Context) {
    val sounds: Map<Int, String> =
        mapOf(1 to "do", 2 to "re", 3 to "mi", 4 to "fa", 5 to "so", 6 to "la", 7 to "si",
            8 to "doBlack", 9 to "reBlack", 10 to "faBlack", 11 to "soBlack", 12 to "laBlack")
    val threads: MutableMap<Int, SoundThread> = mutableMapOf()
    fun playSound(indexNote: Int) {
        if (!threads.containsKey(indexNote)) {
            val sound = SoundThread(indexNote)
            sound.start()
            threads[indexNote] = sound
        }
    }
    fun stopSound(indexNote: Int) {
        if (threads.containsKey(indexNote)) {
            threads.remove(indexNote)
        }
    }
    inner class SoundThread(val indexNotes: Int): Thread() {
        override fun run() {
            val path = sounds.get(indexNotes) + ".wav"
            val asset = context.assets
            val descriptor = asset.openFd(path)
            val fileSize: Long = descriptor.length
            val bufferSize = 4096
            val buffer = ByteArray(bufferSize)

            val audioTrack = AudioTrack(
                AudioManager.STREAM_MUSIC, 44100, AudioFormat.CHANNEL_CONFIGURATION_MONO,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize, AudioTrack.MODE_STREAM
            )

            val logVolume = 1 - ln(20.0f) / ln(100.0f)
            audioTrack.setStereoVolume(logVolume, logVolume)

            audioTrack.play()
            var audioStream: InputStream? = null
            val headerOffset = 0x2C
            var bytesWritten: Long = 0
            var bytesRead = 0

            audioStream = asset.open(path)
            audioStream.read(buffer, 0, headerOffset)

            while (bytesWritten < fileSize - headerOffset) {
                bytesRead = audioStream.read(buffer, 0, bufferSize)
                bytesWritten += audioTrack.write(buffer, 0, bytesRead)
            }
            audioTrack.stop()
            audioTrack.release()

        }
    }
}

