package com.hyun.demo.constant

enum class Difficulty(val level: Int) {
    BEGINNER(1), ELEMENTARY(2), INTERMEDIATE(3), UPPER_INTERMEDIATE(4), ADVANCED(5)
}
fun String.toDifficulty(): Difficulty? {
    return try {
        Difficulty.valueOf(this.uppercase())
    } catch (e: Exception) {
        null
    }
}

enum class Mode {
    TEXT,
    VOICE
}

enum class Progress {
    NOT_STARTED,
    IN_PROGRESS,
    COMPLETED;

    fun isCompleted(): Boolean {
        return this == COMPLETED
    }
}