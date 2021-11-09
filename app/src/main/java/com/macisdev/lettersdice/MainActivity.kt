package com.macisdev.lettersdice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds

class MainActivity : AppCompatActivity() {
	private lateinit var dice: Dice

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		//Configure the dice
		val playableLettersContent = PreferenceManager.getDefaultSharedPreferences(this)
			.getString(PreferencesActivity.PREFERENCES_PLAYABLE_LETTERS_CONTENT, Dice.FULL_ALPHABET)
		dice = Dice(playableLettersContent!!)

		//Configure the dice mode text view
		val tvDiceMode = findViewById<TextView>(R.id.tv_dice_mode)
		val playableLettersName = PreferenceManager.getDefaultSharedPreferences(this)
			.getString(PreferencesActivity.PREFERENCES_PLAYABLE_LETTERS_NAME, getString(R.string.whole_alphabet_name))
		tvDiceMode.text = if (playableLettersName == getString(R.string.customized_name)) {
			"$playableLettersName ${playableLettersContent.uppercase()}"
		} else {
			playableLettersName
		}

		//Ads
		MobileAds.initialize(this) {}
		val adView = findViewById<AdView>(R.id.ad_view)
		val adRequest = AdRequest.Builder().build()
		adView.loadAd(adRequest)
	}

	//Called from the "roll dice button"
	@Suppress("UNUSED_PARAMETER")
	fun rollDice(v: View) {
		val letterImgV = findViewById<ImageView>(R.id.letter_img)
		val tvPlayedLetters = findViewById<TextView>(R.id.tv_played_letters)
		val rollBtn = findViewById<Button>(R.id.roll_btn)

		if (dice.hasNextLetter()) {
			val letter = dice.nextLetter()
			val img = resources.getIdentifier(letter, "drawable", packageName)

			//Animation of the ImageView
			rollBtn.isEnabled = false
			letterImgV.animate().rotationXBy(720f).duration = 1200
			letterImgV.animate().rotationYBy(-720f).setDuration(1200).withEndAction { rollBtn.isEnabled = true }
			rollBtn.animate().rotation(0f).setDuration(600).withEndAction { letterImgV.setImageResource(img) }

			tvPlayedLetters.append(letter.uppercase())
		} else {
			Toast.makeText(this, R.string.all_letters_played, Toast.LENGTH_SHORT).show()
			letterImgV.setImageResource(R.drawable.icon_small)
			tvPlayedLetters.text = ""
			dice.reset()
		}
	}

	//Called from the "open preferences button"
	@Suppress("UNUSED_PARAMETER")
	fun openPreferences(v: View) {
		startActivity(Intent(this, PreferencesActivity::class.java))
	}
}