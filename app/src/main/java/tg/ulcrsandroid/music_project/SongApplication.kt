package tg.ulcrsandroid.music_project

import android.app.Application
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import io.realm.Realm
import io.realm.RealmConfiguration
import tg.ulcrsandroid.music_project.model.Chanson

class SongApplication : Application() {
    lateinit var realm: Realm

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("my-realm")
            .deleteRealmIfMigrationNeeded()
            .compactOnLaunch()
            .allowWritesOnUiThread(true)
            .build()
        Realm.setDefaultConfiguration(config)
        realm = Realm.getDefaultInstance()
        scanMusicFiles(this)
    }

    private fun initSong() {
        realm.executeTransactionAsync {

        }
    }
    private fun scanMusicFiles(context: Context): List<Chanson>{
        var songs = mutableListOf<Chanson>()
        val uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION
        )
        val cursor = context.contentResolver.query(uri, projection, null, null, null)
        cursor?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
            val titleColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
            val artistColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
            val albumColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
            val durationColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

            while (it.moveToNext()) {
                val id = it.getLong(idColumn)
                val title = it.getString(titleColumn)
                val artist = it.getString(artistColumn)
                val album = it.getString(albumColumn)
                val data = it.getString(dataColumn)
                val duration = it.getLong(durationColumn)
                Log.i("MUSIC", "title: $title")
            }
            Log.i("MUSIC", "EXECUTION LECTURE")
        }

        return songs
    }
}