package tg.ulcrsandroid.music_project.model

import io.realm.RealmObject

open class Genre (): RealmObject() {
    var nom: String = ""
    var chansons: String = ""
}