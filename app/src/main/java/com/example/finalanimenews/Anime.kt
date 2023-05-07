package com.example.finalanimenews

class Anime(
    private var id: String,
    private var title: String,
    private var image: String,
    private var hasSubOrDub: Boolean,
    private var url: String,
    private var releaseDate: String,
    private var favorite: Boolean = false,
    private var statusOfSerial: String = "None"
){

    fun getId():String{
        return this.id
    }

    fun setFavorite(isfavorite: Boolean){
        this.favorite = isfavorite
    }

    fun getImage(): String{
        return this.image
    }

    fun getTitle(): String{
        return this.title
    }
    fun gethasSubOrDub(): Boolean{
        return this.hasSubOrDub
    }
    fun getUrl(): String{
        return this.url
    }

    fun getFavorite(): Boolean{
        return this.favorite
    }
    fun getReleaseDate(): String{
        return this.releaseDate
    }

    fun getStatusOfSerial(): String{
        return this.statusOfSerial
    }

    fun setStatusOfSerial(newStatus: String){
        this.statusOfSerial = newStatus
    }


}





