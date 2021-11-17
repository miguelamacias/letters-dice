package com.macisdev.lettersdice

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.macisdev.lettersdice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
	private lateinit var dice: Dice
	private lateinit var gui: ActivityMainBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		gui = ActivityMainBinding.inflate(layoutInflater)
		setContentView(gui.root)

		setSupportActionBar(gui.toolbar)

		//Configure the dice
		val playableLettersContent = PreferenceManager.getDefaultSharedPreferences(this)
			.getString(PreferencesActivity.PREFERENCES_PLAYABLE_LETTERS_CONTENT, Dice.FULL_ALPHABET)
		dice = Dice(playableLettersContent!!)

		//Configure the dice mode text view
		val playableLettersName = PreferenceManager.getDefaultSharedPreferences(this)
			.getString(PreferencesActivity.PREFERENCES_PLAYABLE_LETTERS_NAME, getString(R.string.whole_alphabet_name))
		gui.tvDiceMode.text = if (playableLettersName == getString(R.string.customized_name)) {
			"${getString(R.string.dice_mode)} $playableLettersName ${playableLettersContent.uppercase()}"
		} else {
			"${getString(R.string.dice_mode)} $playableLettersName"
		}

		//Ads
		MobileAds.initialize(this) {}
		val adRequest = AdRequest.Builder().build()
		gui.adView.loadAd(adRequest)
	}

	//Called from the "roll dice button"
	@Suppress("UNUSED_PARAMETER")
	fun rollDice(v: View) {

		if (dice.hasNextLetter()) {
			val letter = dice.nextLetter()
			val img = resources.getIdentifier(letter, "drawable", packageName)

			//Animation of the ImageView
			gui.rollBtn.isEnabled = false
			gui.letterImg.isEnabled = false
			gui.letterImg.animate().rotationXBy(720f).duration = 1200
			gui.letterImg.animate().rotationYBy(-720f).setDuration(1200)
				.withEndAction { gui.rollBtn.isEnabled = true; gui.letterImg.isEnabled = true}
			gui.rollBtn.animate().rotation(0f).setDuration(600).withEndAction { gui.letterImg.setImageResource(img)
				gui.tvPlayedLetters.append(letter.uppercase())}

		} else {
			Toast.makeText(this, R.string.all_letters_played, Toast.LENGTH_SHORT).show()
			gui.letterImg.setImageResource(R.drawable.empty_dice_img)
			gui.tvPlayedLetters.text = ""
			dice.reset()
		}
	}

	private fun openPreferences() {
		startActivity(Intent(this, PreferencesActivity::class.java))
	}

	override fun onCreateOptionsMenu(menu: Menu?): Boolean {
		menuInflater.inflate(R.menu.main, menu)
		return super.onCreateOptionsMenu(menu)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		when (item.itemId) {
			R.id.menu_preferences -> openPreferences()
		}
		return super.onOptionsItemSelected(item)
	}
}