package tg.ulcrsandroid.music_project.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import org.bson.types.ObjectId

open class Genre (): RealmObject() {
    @PrimaryKey var id: ObjectId? = null
    var nom: String = ""
    var chansons: String = ""
}