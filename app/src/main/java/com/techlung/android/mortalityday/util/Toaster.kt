package com.techlung.android.mortalityday.util

import android.content.Context
import android.widget.Toast

object Toaster {
    fun show(message: String, context: Context?) {
        if (context != null) {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        }
    }
}
