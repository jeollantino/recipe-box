package com.jeollantino.recipebox.models

data class Ingredient(
    var id: Int,
    var aisle: String,
    var image: String,
    var consistency: String,
    var name: String,
    var nameClean: String,
    var original: String,
    var originalName: String,
    var amount: Double,
    var unit: String,
    var meta: List<String>
)
