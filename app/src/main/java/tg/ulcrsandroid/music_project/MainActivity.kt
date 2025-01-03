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
import io.realm.RealmResults
import org.bson.types.ObjectId
import tg.ulcrsandroid.music_project.databinding.ActivityMainBinding
import tg.ulcrsandroid.music_project.model.Album
import tg.ulcrsandroid.music_project.model.Artiste
import tg.ulcrsandroid.music_project.model.Chanson
import tg.ulcrsandroid.music_project.model.SongData


class MainActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION_CODE = 1001
    private lateinit var realm : Realm
    private lateinit var songAdapter: SongAdapter
    private lateinit var ui : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ui = ActivityMainBinding.inflate(layoutInflater)
        setContentView(ui.root)
        //setSupportActionBar(ui.toolbar)
        setTitle("MYSIC")

        realm = Realm.getDefaultInstance()
        enableEdgeToEdge()

       // setContentView(R.layout.activity_main)
        Log.i("MUSIC", "COURS MMMMMMMMMMMMMM")
        checkAndRequestPermissions(this)

        //setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ui.recycler.id)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val songs = realm.where(Chanson::class.java).findAllAsync()
        val ite = songs.count()
        Log.i("DEBUG Main", "artiste: $ite")

        val songAdapter = SongAdapter(songs as RealmResults<Chanson>)
        ui.recycler.adapter = songAdapter
        ui.recycler.setHasFixedSize( true)
        realm.close()
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
        val realm = Realm.getDefaultInstance()
        cursor?.use {
            val songsData = getSongs(it)
            val count = songsData.size
            val countRealm = countRealmObject(realm).toInt()

            Log.i("MUSIC", "countCursor: $count countRealm: $countRealm")

            if (countRealm == 0) { // Cas ou aucun objet dans realm
                insertRealmObject(realm, songsData)
                Log.i("MUSIC", "Cas ou aucun objet dans realm")

            } else if(count > countRealm) { // Cas nouveau fichier ajouté
                initListe(realm)
                insertRealmObject(realm, songsData)
                Log.i("MUSIC", "Cas de mis a jour")
            } else if (countRealm == 0){
                Log.i("MUSIC", "Rien dans realm")
            } else {
                Log.i("MUSIC", "Tout est a jour")
            }
        }
        return songs
    }

fun getSongs(cursor: Cursor): List<SongData> {
    var songs = mutableListOf<SongData>()
    val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
    val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
    val artistColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
    val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)
    val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)
    val durationColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION)
    while (cursor.moveToNext()) {
        val id = cursor.getLong(idColumn)
        val titre = cursor.getString(titleColumn)
        val artisteNom = cursor.getString(artistColumn)
        val albumNom = cursor.getString(albumColumn)
        val url = cursor.getString(dataColumn)
        val duree = cursor.getLong(durationColumn)

        songs.add(SongData(id, titre, artisteNom, albumNom, url, duree))
        //Log.i("MUSIC", "artiste: ${artisteNom}")
    }
    return songs
}
    fun insertRealmObject(realm: Realm, songs : List<SongData>): Realm {
        realm.executeTransactionAsync ({ transac ->
            for (song in songs) {

                // Recuperer l'artiste
                val art = transac.where(Artiste::class.java)
                    .equalTo(Artiste.NOM, song.artiste)
                    .findFirst() ?: transac.createObject(Artiste::class.java, ObjectId()).apply {
                        this.nom = song.artiste
                }

                // Recuperer ou creer l'album
                var alb = transac.where(Album::class.java)
                    .equalTo(Album.NOM, song.album)
                    .findFirst() ?: transac.createObject(Album::class.java, ObjectId()).apply {
                        this.nom = song.album
                }

                val id = ObjectId()
                // Creer la chanson
                var chanson = transac.createObject(Chanson::class.java, id).apply {
                    this.titre = song.titre
                    this.duree = song.duree
                    this.artistePrincipal = art/* Artiste().apply {
                        this.nom = song.artiste
                    }*/
                    this.album = alb/*Album().apply {
                        this.nom = song.album
                    }*/
                    this.url = song.url
                }
                Log.i("MUSIC", "titre deb: ${chanson.titre}")

                art.chansons.add(chanson)
                alb.chansons.add(chanson)
            }
        }, {
            Log.i("MUSIC", "Transaction reussi")

        }, { error ->
            Log.e("Realm", "Transaction échouée : ${error.localizedMessage}")
        })
        return realm
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