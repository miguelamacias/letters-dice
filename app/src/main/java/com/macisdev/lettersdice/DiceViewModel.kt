package com.macisdev.lettersdice

import androidx.lifecycle.ViewModel
import kotlin.random.Random

class DiceViewModel : ViewModel() {
    companion object {
        const val FULL_ALPHABET = "abcdefghijklmnopqrstuvwxyz"
        const val SCATTERGORIES_LETTERS = "abcdefghijklmnoprstw"
    }
    private lateinit var playableLetters: String
    var playedLetters = ""
    var currentLetter = ""

    fun initialize(playableLetters: String) {
        this.playableLetters = playableLetters
    }

    fun nextLetter(): String {
        val tempLetter = playableLetters[Random.nextInt(playableLetters.length)]
        return if (playedLetters.contains(tempLetter)) {
            nextLetter()
        } else {
            playedLetters += tempLetter
            currentLetter = tempLetter.toString()
            tempLetter.toString()
        }
    }

    fun hasNextLetter() = playedLetters.length < playableLetters.length

    fun reset() {
        playedLetters = ""
    }
}