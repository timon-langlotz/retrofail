package io.retrofail.example

import android.util.Log
import io.retrofail.Logger

object LogcatLogger : Logger {
    override fun log(priority: Int, tag: String, message: String, e: Throwable?) {
        val stackTrace = Log.getStackTraceString(e)
        if (stackTrace.isEmpty()) {
            Log.println(priority, tag, message)
        } else {
            Log.println(priority, tag, "$message\n$stackTrace")
        }
    }
}