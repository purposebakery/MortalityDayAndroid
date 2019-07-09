package com.techlung.android.mortalityday

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView

import androidx.appcompat.app.AppCompatActivity

import com.techlung.android.mortalityday.enums.Theme
import com.techlung.android.mortalityday.settings.Preferences
import com.techlung.android.mortalityday.settings.PreferencesActivity
import com.techlung.android.mortalityday.util.MortalityDayUtil
import kotlinx.android.synthetic.main.message_activity.*

class MessageActivity : AppCompatActivity() {

    // TODO
    // start on boot error
    // not only random message but complete pool
    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.initPreferences(this)
        if (Preferences.theme === Theme.DARK) {
            setTheme(R.style.AppTheme_Dark)
        }
        super.onCreate(savedInstanceState)

        setContentView(R.layout.message_activity)

        var message: String? = intent.getStringExtra(MESSAGE_EXTRA)
        var author: String? = intent.getStringExtra(AUTHOR_EXTRA)

        if ((message == null || message == "") && (author == null || author == "")) {
            val quote = MortalityDayUtil.getQuote(this)
            message = quote.message
            author = quote.author
        }

        val messageView = findViewById<View>(R.id.message) as TextView
        if (author != null && author.trim { it <= ' ' } != "") {
            message = "\"" + message + "\""
        }
        messageView.text = message

        val authorView = findViewById<View>(R.id.author) as TextView
        if (author != null && author.trim { it <= ' ' } != "") {
            authorView.text = " - $author"
        } else {
            authorView.visibility = View.GONE
        }

        message_settings.setOnClickListener {
            val intent = Intent(this@MessageActivity, PreferencesActivity::class.java)
            startActivity(intent)
        }

        val shareMessage = message!! + if (author != null && author.trim { it <= ' ' } != "") "\n - $author" else ""
        message_share.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, shareMessage)

            val chooserMessage = getString(R.string.alert_share)
            startActivity(Intent.createChooser(share, chooserMessage))
        }
    }

    companion object {
        const val MESSAGE_EXTRA = "MESSAGE_EXTRA"
        const val AUTHOR_EXTRA = "AUTHOR_EXTRA"
    }

}
