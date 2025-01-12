package tg.ulcrsandroid.music_project

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class SongApplication : Application() {
    lateinit var realm: Realm
    private val REQUEST_PERMISSION_CODE = 1001
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
        //checkAndRequestPermissions(this)
    }

    private fun initSong() {
        realm.executeTransactionAsync {
        }
    }

}