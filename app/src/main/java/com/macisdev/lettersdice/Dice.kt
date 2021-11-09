package com.macisdev.lettersdice

import kotlin.random.Random

class Dice (private val playableLetters: String) {
    companion object {
        const val FULL_ALPHABET = "abcdefghijklmnopqrstuvwxyz"
        const val SCATTERGORIES_LETTERS = "abcdefghijklmnoprstw"
    }

    private var playedLetters = ""
    var currentLetter = ""

    fun nextLetter(): String {
        val tempLetter = playableLetters[Random.nextInt(playableLetters.length)]
        return if (playedLetters.contains(tempLetter)) {
            nextLetter()
        } else {
            playedLetters += tempLetter
            tempLetter.toString()
        }
    }

    fun hasNextLetter() = playedLetters.length < playableLetters.length

    fun reset() {
        playedLetters = ""
    }
}