package com.hyun.demo.word.entity

import jakarta.persistence.Access
import jakarta.persistence.AccessType

@Access(AccessType.FIELD)
data class Word (
    val word:String,
    val meaning:String
)