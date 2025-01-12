package tg.ulcrsandroid.music_project.model

data class SongData(
    val id: Long,
    val titre: String,
    val artiste: String,
    val album: String,
    val url: String,
    val duree: Long
)
