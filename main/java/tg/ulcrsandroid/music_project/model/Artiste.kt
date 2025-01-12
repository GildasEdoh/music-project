package tg.ulcrsandroid.music_project.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Artiste (): RealmObject() {
    @PrimaryKey var id : ObjectId? = null
    var nom: String = ""
    var albums: RealmList<Album> = RealmList()
    var chansons: RealmList<Chanson> = RealmList()

    companion object {
        val ID = "id"
        val NOM = "nom"
        val ALBUMS = "albums"
        val CHANSONS = "chansons"
    }
    fun addChansons(chanson :Chanson) {
        this.chansons.add(chanson)
    }
    fun addAlbualbumms(album :Album) {
        this.albums.add(album)
    }
}