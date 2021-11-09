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
		const val PREFERENCES_EDIT_TEXT_CONTENT = "editTextContent"
	}

	private lateinit var etCustomLetters: EditText
	private lateinit var preferences: SharedPreferences
	private lateinit var radioGroup: RadioGroup

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_preferences)

		etCustomLetters = findViewById(R.id.et_custom_letters)
		radioGroup = findViewById(R.id.radio_group)

		preferences = PreferenceManager.getDefaultSharedPreferences(this)
		val checkedRbtn = findViewById<RadioButton>(preferences.getInt(PREFERENCES_CHECKED_RBTN, R.id.rb_alphabet))
		checkedRbtn.isChecked = true
		etCustomLetters.isVisible = preferences.getBoolean(PREFERENCES_EDIT_TEXT_VISIBLE, false)
		etCustomLetters.setText(preferences.getString(PREFERENCES_EDIT_TEXT_CONTENT, ""))
	}

	fun savePreferences(v: View) {
		val editTextContent = when (radioGroup.checkedRadioButtonId) {
			R.id.rb_alphabet -> Dice.FULL_ALPHABET
			R.id.rb_scattergories -> Dice.SCATTERGORIES_LETTERS
			R.id.rb_custom -> etCustomLetters.text.toString().lowercase()
			else -> ""
		}

		preferences.edit{
			putString(PREFERENCES_EDIT_TEXT_CONTENT, editTextContent)
			putInt(PREFERENCES_CHECKED_RBTN, radioGroup.checkedRadioButtonId)
			putBoolean(PREFERENCES_EDIT_TEXT_VISIBLE, etCustomLetters.isVisible)
		}

		startActivity(Intent(this, MainActivity::class.java))
	}

	fun changeCustomEditTextVisibility(v: View) {
		etCustomLetters.isVisible = v.id == R.id.rb_custom
	}
}