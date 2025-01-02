package tg.ulcrsandroid.music_project.model

import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Playlist {
    @PrimaryKey var id: ObjectId? = null
    var nom:String = ""
    var chansons :String = ""
    var dureeSecondes: Int = 0
    var dureeMinutes: Int = 0
}