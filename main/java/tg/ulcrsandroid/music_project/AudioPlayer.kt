package tg.ulcrsandroid.music_project

import android.content.Context
import android.media.MediaPlayer

class AudioPlayer(private val context : Context) {
    private var mediaPlayer: MediaPlayer? = null

    fun play(url: String?) {
        stop()
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener{start()}
            setOnErrorListener { _, _, _ ->
                    false
            }
            prepareAsync()
        }
    }
    fun pause() {
        mediaPlayer?.takeIf { it.isPlaying }?.pause()
    }

    fun resume() {
        mediaPlayer?.start()
    }
    fun stop() {
        mediaPlayer?.release()
        mediaPlayer = null
    }
}