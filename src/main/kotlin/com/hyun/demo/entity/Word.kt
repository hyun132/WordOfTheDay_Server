package com.hyun.demo.entity

import jakarta.persistence.Access
import jakarta.persistence.AccessType

@Access(AccessType.FIELD)
data class Word (
    val word:String
)