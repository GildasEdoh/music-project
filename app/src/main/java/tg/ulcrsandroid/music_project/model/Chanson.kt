package tg.ulcrsandroid.music_project.model

import io.realm.RealmList
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Chanson () : RealmObject() {
    @PrimaryKey var id : ObjectId? = null
    var titre: String? = null
    var url: String? = null
    var duree: String? = null
    var arstistes: RealmList<Artiste> = RealmList()
    var genre: Genre? = null
}