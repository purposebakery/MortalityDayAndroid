package com.techlung.android.mortalityday.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.techlung.android.mortalityday.MessageActivity
import com.techlung.android.mortalityday.R
import com.techlung.android.mortalityday.enums.Theme
import com.techlung.android.mortalityday.notification.MortalityDayNotificationManager
import com.techlung.android.mortalityday.util.MortalityDayUtil
import kotlinx.android.synthetic.main.preferences_activity.*

class PreferencesActivity : AppCompatActivity() {
    private var skipped = false

    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.initPreferences(this)
        if (Preferences.theme === Theme.DARK) {
            setTheme(R.style.AppTheme_Dark)
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.preferences_activity)

        showQuote.setOnClickListener {
            val quote = MortalityDayUtil.getQuote(this@PreferencesActivity)
            val resultIntent = Intent(this@PreferencesActivity, MessageActivity::class.java)
            resultIntent.putExtra(MessageActivity.MESSAGE_EXTRA, quote.message)
            resultIntent.putExtra(MessageActivity.AUTHOR_EXTRA, quote.author)
            startActivity(resultIntent)
        }

        info.setOnClickListener {
            AlertDialog.Builder(this@PreferencesActivity)
                    .setTitle(R.string.alert_info)
                    .setMessage(R.string.info_message)
                    .setPositiveButton(R.string.alert_ok, null)
                    .show()
        }

        skipped = false

        checkFirstStart()
    }

    override fun onStop() {
        super.onStop()

        if (!skipped) {
            MortalityDayNotificationManager.setNextNotification(this, true)
        }
    }

    fun updateTheme() {
        finish()
        val intent = intent
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun checkFirstStart() {
        if (Preferences.firstStart) {
            Preferences.firstStart = false
            showFirstStartMessage()
        } else if (MortalityDayUtil.isMortalityDay && !intent.getBooleanExtra(CALLED_INTERNAL, false)) {
            skipped = true
            val messageStart = Intent(this, MessageActivity::class.java)
            startActivity(messageStart)
        }
    }

    private fun showFirstStartMessage() {
        AlertDialog.Builder(this)
                .setTitle(R.string.first_start_title)
                .setMessage(R.string.first_start_message)
                .setPositiveButton(R.string.alert_thanks) { dialog, which -> dialog.dismiss() }
                .show()
    }

    companion object {
        const val CALLED_INTERNAL = "CALLED_INTERNAL"
    }
}
