package com.example.clubsentiersnature.models

data class Randonnee(
    val nom: String,
    val lieu: String,
    val date: String,
    val note: Int,
    val description: String,
    val photo: String = ""
)