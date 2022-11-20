package de.thm.mow2gamecollection.wordle.helper

const val WORD_LENGTH = 5
const val MAX_TRIES = 6
// number of recently played target words to save. When a new random target word is picked,
// the app makes sure that it is not one of the last [NUMBER_OF_RECENT_TARGET_WORDS] target words
const val NUMBER_OF_RECENT_TARGET_WORDS = 5