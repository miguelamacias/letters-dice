package com.macisdev.lettersdice

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager
import com.macisdev.lettersdice.databinding.ActivityPreferencesBinding

class PreferencesActivity : AppCompatActivity() {
	companion object {
		const val PREFERENCES_CHECKED_RBTN = "checkedRBTN"
		const val PREFERENCES_EDIT_TEXT_VISIBLE = "editTextVisible"
		const val PREFERENCES_PLAYABLE_LETTERS_CONTENT = "playableLetterContent"
		const val PREFERENCES_PLAYABLE_LETTERS_NAME = "playableLetterName"
	}

	private lateinit var gui: ActivityPreferencesBinding
	private lateinit var preferences: SharedPreferences

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		gui = ActivityPreferencesBinding.inflate(layoutInflater)
		setContentView(gui.root)

		//Configure the GUI according to the saved preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this)
		val checkedRbtn = findViewById<RadioButton>(preferences.getInt(PREFERENCES_CHECKED_RBTN, R.id.rbAlphabet))
		checkedRbtn?.isChecked = true
		gui.etCustomLetters.isVisible = preferences.getBoolean(PREFERENCES_EDIT_TEXT_VISIBLE, false)

		if (gui.etCustomLetters.isVisible) {
			gui.etCustomLetters.setText(preferences.getString(PREFERENCES_PLAYABLE_LETTERS_CONTENT, ""))
		}
	}

	//Called when the apply button is clicked
	@Suppress("UNUSED_PARAMETER")
	fun savePreferences(v: View) {
		val playableLettersContent = when (gui.radioGroup.checkedRadioButtonId) {
			R.id.rbAlphabet -> Dice.FULL_ALPHABET
			R.id.rbScattergories -> Dice.SCATTERGORIES_LETTERS
			R.id.rbCustom -> gui.etCustomLetters.text.toString().lowercase()
			else -> ""
		}

		val playableLettersName = when (gui.radioGroup.checkedRadioButtonId) {
			R.id.rbAlphabet -> getString(R.string.whole_alphabet_name)
			R.id.rbScattergories -> getString(R.string.scattergories_name)
			R.id.rbCustom -> getString(R.string.customized_name)
			else -> ""
		}

		if (playableLettersContent.isNotBlank() && playableLettersContent.all { it.isLetter() }) {
			preferences.edit{
				putString(PREFERENCES_PLAYABLE_LETTERS_CONTENT, playableLettersContent)
				putString(PREFERENCES_PLAYABLE_LETTERS_NAME, playableLettersName)
				putInt(PREFERENCES_CHECKED_RBTN, gui.radioGroup.checkedRadioButtonId)
				putBoolean(PREFERENCES_EDIT_TEXT_VISIBLE, gui.etCustomLetters.isVisible)
			}

			startActivity(Intent(this, MainActivity::class.java))

		} else {
			gui.etCustomLetters.error = getString(R.string.only_letters_error)
		}
	}

	//Called when the radio buttons are clicked
	fun changeCustomEditTextVisibility(v: View) {
		gui.etCustomLetters.isVisible = v.id == R.id.rbCustom
	}
}