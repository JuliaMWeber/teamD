package de.thm.mow2gamecollection.wordle.model.grid

enum class LetterStatus {
    BLANK,
    UNKNOWN,
    CORRECT,
    WRONG_POSITION,
    WRONG
}