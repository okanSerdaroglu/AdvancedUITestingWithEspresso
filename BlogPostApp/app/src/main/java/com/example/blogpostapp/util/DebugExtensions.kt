package com.example.blogpostapp.util

import android.util.Log
import com.example.blogpostapp.util.Constants.DEBUG
import com.example.blogpostapp.util.Constants.TAG

fun printLogD(className: String?, message: String ) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}