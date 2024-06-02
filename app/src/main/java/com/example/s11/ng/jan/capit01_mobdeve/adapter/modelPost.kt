package com.example.s11.ng.jan.capit01_mobdeve.adapter

class modelPost {
    var username: String? = null
    var imageID: Int = 0
    var captionID: String? = null

    internal constructor()

    internal constructor(
        username: String?,
        imageID: Int,
        captionID: String?
    ) {
        this.username = username
        this.imageID = imageID
        this.captionID = captionID
    }

}