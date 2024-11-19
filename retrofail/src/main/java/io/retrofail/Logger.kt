package io.retrofail

import android.util.Log

/**
 * Interface to which logging can be delegated.
 */
public fun interface Logger {
    /**
     * Logs a message.
     *
     * @param priority The log priority.
     * @param tag The tag to log with.
     * @param message The message to be logged.
     * @param e An optional [Throwable].
     */
    public fun log(priority: Int, tag: String, message: String, e: Throwable?)

    public companion object {
        public const val DEBUG: Int = Log.DEBUG
        public const val INFO: Int = Log.INFO
        public const val WARN: Int = Log.WARN
        public const val ERROR: Int = Log.ERROR
    }
}

private const val TAG = "Retrofail"
internal fun Logger.d(message: String, e: Throwable? = null) = log(Logger.DEBUG, TAG, message, e)
internal fun Logger.i(message: String, e: Throwable? = null) = log(Logger.INFO, TAG, message, e)
internal fun Logger.w(message: String, e: Throwable? = null) = log(Logger.WARN, TAG, message, e)
internal fun Logger.e(message: String, e: Throwable? = null) = log(Logger.ERROR, TAG, message, e)
