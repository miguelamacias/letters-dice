package com.macisdev.lettersdice

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.edit
import androidx.core.view.isVisible
import androidx.preference.PreferenceManager

class PreferencesActivity : AppCompatActivity() {
	companion object {
		const val PREFERENCES_CHECKED_RBTN = "checkedRBTN"
		const val PREFERENCES_EDIT_TEXT_VISIBLE = "editTextVisible"
		const val PREFERENCES_PLAYABLE_LETTERS_CONTENT = "playableLetterContent"
		const val PREFERENCES_PLAYABLE_LETTERS_NAME = "playableLetterName"
	}

	private lateinit var etCustomLetters: EditText
	private lateinit var preferences: SharedPreferences
	private lateinit var radioGroup: RadioGroup

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_preferences)

		etCustomLetters = findViewById(R.id.et_custom_letters)
		radioGroup = findViewById(R.id.radio_group)

		//Configure the GUI according to the saved preferences
		preferences = PreferenceManager.getDefaultSharedPreferences(this)
		val checkedRbtn = findViewById<RadioButton>(preferences.getInt(PREFERENCES_CHECKED_RBTN, R.id.rb_alphabet))
		checkedRbtn?.isChecked = true
		etCustomLetters.isVisible = preferences.getBoolean(PREFERENCES_EDIT_TEXT_VISIBLE, false)

		if (etCustomLetters.isVisible) {
			etCustomLetters.setText(preferences.getString(PREFERENCES_PLAYABLE_LETTERS_CONTENT, ""))
		}
	}

	//Called when the apply button is clicked
	@Suppress("UNUSED_PARAMETER")
	fun savePreferences(v: View) {
		val playableLettersContent = when (radioGroup.checkedRadioButtonId) {
			R.id.rb_alphabet -> Dice.FULL_ALPHABET
			R.id.rb_scattergories -> Dice.SCATTERGORIES_LETTERS
			R.id.rb_custom -> etCustomLetters.text.toString().lowercase()
			else -> ""
		}

		val playableLettersName = when (radioGroup.checkedRadioButtonId) {
			R.id.rb_alphabet -> getString(R.string.whole_alphabet_name)
			R.id.rb_scattergories -> getString(R.string.scattergories_name)
			R.id.rb_custom -> getString(R.string.customized_name)
			else -> ""
		}

		preferences.edit{
			putString(PREFERENCES_PLAYABLE_LETTERS_CONTENT, playableLettersContent)
			putString(PREFERENCES_PLAYABLE_LETTERS_NAME, playableLettersName)
			putInt(PREFERENCES_CHECKED_RBTN, radioGroup.checkedRadioButtonId)
			putBoolean(PREFERENCES_EDIT_TEXT_VISIBLE, etCustomLetters.isVisible)
		}

		startActivity(Intent(this, MainActivity::class.java))
	}

	//Called when the radio buttons are clicked
	fun changeCustomEditTextVisibility(v: View) {
		etCustomLetters.isVisible = v.id == R.id.rb_custom
	}
}