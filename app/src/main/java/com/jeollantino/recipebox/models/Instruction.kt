package com.jeollantino.recipebox.models

data class Instruction(
    var name: String,
    var steps: List<Step>
)
