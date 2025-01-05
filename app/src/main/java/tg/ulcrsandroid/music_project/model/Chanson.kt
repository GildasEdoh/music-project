package tg.ulcrsandroid.music_project.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Chanson () : RealmObject() {
    @PrimaryKey var id : ObjectId? = null
    var titre: String? = null
    var url: String? = null
    var duree: Long? = null
    var artistePrincipal : Artiste? = null
    var arstistes: RealmList<Artiste> = RealmList()
    var genre: Genre? = null
    var album: Album? =  null
    var isPlaying: Boolean = false
    var nbrOfplay: Int = 0
    var recentlyPlayed = false

    companion object {
        val ID = "id"
        val TITRE = "titre"
        val DUREE = "duree"
        val ARTISTEPRINCIPAL = "artistePrincipal"
        val ALBUM = "album"
    }
}