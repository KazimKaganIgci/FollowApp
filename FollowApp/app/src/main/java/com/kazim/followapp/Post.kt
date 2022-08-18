package com.kazim.followapp

import java.io.Serializable

data class Post(val name:String,val email:String,val url:String,val konu:String,val baslik:String):Serializable {
}