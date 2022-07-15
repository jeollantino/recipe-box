package com.jeollantino.recipebox.models

data class Step(
    var number: Int,
    var step: String,
    var ingredients: List<StepIngredient>,
    var equipment: List<StepEquipment>,
    var length: StepLength?
)
