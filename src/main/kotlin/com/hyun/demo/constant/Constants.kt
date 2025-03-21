package com.hyun.demo.constant

enum class Difficulty(level: Int) {
    BEGINNER(1), ELEMENTARY(2), INTERMEDIATE(3), UPPER_INTERMEDIATE(4), ADVANCED(5)
}

enum class Progress {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED;

    fun isCompleted(): Boolean {
        return this == COMPLETED
    }
}