package tg.ulcrsandroid.music_project
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import io.realm.Realm
import tg.ulcrsandroid.music_project.model.Album
import tg.ulcrsandroid.music_project.model.Artiste
import tg.ulcrsandroid.music_project.model.Chanson


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION_CODE = 1001
    private lateinit var realm : Realm
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        Log.i("MUSIC", "COURS MMMMMMMMMMMMMM")

        realm = Realm.getDefaultInstance()
        checkAndRequestPermissions(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    //
    private fun checkAndRequestPermissions(context: Context) {
        val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
           // val songs = scanMusicFiles(context)
            Log.i("PERMISSION", "Permissions accordées")
            scanMusicFiles(context)
        } else {
            // Demander la permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission),
                REQUEST_PERMISSION_CODE
            )
            Log.i("MUSIC", "PERMISION NON ACCORDEE ")
        }
    }
    //
    private fun scanMusicFiles(context: Context): List<Chanson> {
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
            val count = it.count
            val countRealm = countRealmObject(realm).toInt()
            if (count == 0 && countRealm == 0) { // Cas ou aucun objet dans realm
                insertSongsIntoRealm(it, realm)
            } else if(count != 0 && countRealm != 0 && count > countRealm){ // Cas ou nouvelle  musique dans

            }
            Log.i("MusicApp", "Nombre total de chansons : $count")
            Log.i("MUSIC", "EXECUTION LECTURE")
        }
        return songs
    }
fun insertSongsIntoRealm(cursor: Cursor, realm: Realm) {
    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
    val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
    val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
    val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)

    realm.executeTransactionAsync ({ transac ->
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val titre = cursor.getString(titleColumn)
            val artisteNom = cursor.getString(artistColumn)
            val albumNom = cursor.getString(albumColumn)
            val url = cursor.getString(dataColumn)
            val duree = cursor.getLong(durationColumn)

            // Recuperer l'artiste
            val art = transac.where(Artiste::class.java)
                .equalTo(Artiste.NOM, artisteNom)
                .findFirst() ?: transac.createObject(Artiste::class.java, artisteNom)

            // Recuperer ou creer l'album
            var alb = transac.where(Album::class.java)
                .equalTo(Album.NOM, albumNom)
                .findFirst() ?: transac.createObject(Album::class.java, albumNom)

            // Creer la chanson
            var chanson = transac.createObject(Chanson::class.java, url).apply {
                this.titre = titre
                this.duree = duree
                this.artistePrincipal = art
                this.album = alb
                this.url = url
            }
            Log.i("MUSIC", "artiste: $artisteNom")
        }
    }, {
        Log.i("MUSIC", "Transaction reussi")

    }, { error ->
        Log.e("Realm", "Transaction échouée : ${error.localizedMessage}")
    })
}
    fun countRealmObject(realm: Realm) : Long {
        return realm.where(Chanson::class.java).count()
    }
    //
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("PERMISSION", "Permissions accordées")
                // Recommencer la lecture des fichiers audio
                scanMusicFiles(this)
            } else {
                Log.i("MUSIC", "PERMISSION REFUSÉE")
            }
        }
    }
    fun initListe(realm: Realm) {
        realm.executeTransactionAsync { r ->
            // Supprime tous les X-Men existants dans Realm
            Log.i("Mise a jour .....", "maj")
            r.where(Chanson::class.java).findAll().deleteAllFromRealm()
        }
        this.realm.close()
    }
}