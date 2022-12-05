package de.thm.mow2gamecollection.wordle.controller.helper

import android.app.Activity
import android.content.Context
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

/**
 * @param activity The Activity that uses and displays the time
 * @param textView: the TextView that is used to show the time
 */
class Stopwatch(val activity: Activity, val textView: TextView) {
    private var sharedPreferencesHelper: SharedPreferencesHelper
    private val timer = Timer()

    init {
        sharedPreferencesHelper = SharedPreferencesHelper(activity)
    }

    fun onResume() {
        if(sharedPreferencesHelper.isStopwatchRunning) {
            sharedPreferencesHelper.setStopwatchRunning(true)
        } else {
            sharedPreferencesHelper.setStopwatchRunning(false)
            if (sharedPreferencesHelper.startTime != null && sharedPreferencesHelper.stopTime != null) {
                val time = Date().time - calculateRestartTime().time
                textView.text = timeStringFromLong(time)
            }
            start()
        }

        // use a custom TimerTask to update TextView
        timer.scheduleAtFixedRate(UpdateUiTimerTask(), 0, 500)
    }

    fun resetTimer() {
        sharedPreferencesHelper.apply {
            setStartTime(null)
            setStopTime(null)
            sharedPreferencesHelper.setStopwatchRunning(false)
        }
        textView.text = timeStringFromLong(0)
    }

    fun stop() {
        if (sharedPreferencesHelper.isStopwatchRunning) {
            sharedPreferencesHelper.setStopTime(Date())
            sharedPreferencesHelper.setStopwatchRunning(false)
        }
    }

    fun start() {
        if (sharedPreferencesHelper.stopTime != null) {
            sharedPreferencesHelper.setStartTime(calculateRestartTime())
            sharedPreferencesHelper.setStopTime(null)
        } else {
            sharedPreferencesHelper.setStartTime(Date())
        }
        sharedPreferencesHelper.setStopwatchRunning(true)
    }

    fun getTotalTimeString() : String? {
        sharedPreferencesHelper.getTotalMilliseconds()?.let {
            return timeStringFromLong(it)
        }
        return null
    }

    private fun calculateRestartTime(): Date {
        var restartTimeInMilliseconds = System.currentTimeMillis()
        sharedPreferencesHelper.getTotalMilliseconds()?.let { timeElapsed ->
            restartTimeInMilliseconds -= timeElapsed
        }
        return Date(restartTimeInMilliseconds)
    }

    /**
     * custom TimerTask to update the TextView displaying the time
     */
    private inner class UpdateUiTimerTask: TimerTask() {
        override fun run() {
            if (sharedPreferencesHelper.isStopwatchRunning) {
                val time = Date().time - sharedPreferencesHelper.startTime!!.time

                activity.runOnUiThread {
                    textView.text = timeStringFromLong(time)
                }
            }
        }
    }

    private fun timeStringFromLong(timeMilliseconds: Long): String {
        val seconds = (timeMilliseconds / 1000) % 60
        val minutes = (timeMilliseconds / (1000 * 60) % 60)
        val hours = (timeMilliseconds / (1000 * 60 * 60) % 24)
        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
        return when (hours) {
            0L -> String.format("%02d:%02d", minutes, seconds)
            else -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }
    }

    companion object {

        // keys for Shared Preferences
        const val PREFERENCES_KEY = "preferences"
        const val START_TIME_KEY = "startTime"
        const val STOP_TIME_KEY = "stopTime"
        const val IS_STOPWATCH_RUNNING_KEY = "isStopwatchRunning"
    }


    private inner class SharedPreferencesHelper(context: Context) {
        private val sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        private val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())

        var isStopwatchRunning = false
            private set
        var startTime: Date? = null
            private set
        var stopTime: Date? = null
            private set

        init {
            isStopwatchRunning = sharedPreferences.getBoolean(IS_STOPWATCH_RUNNING_KEY, false)

            val startTimeString = sharedPreferences.getString(START_TIME_KEY, null)
            startTimeString?.let {
                startTime = dateFormat.parse(it)
            }

            val stopTimeString = sharedPreferences.getString(STOP_TIME_KEY, null)
            stopTimeString?.let {
                stopTime = dateFormat.parse(it)
            }
        }

        fun setStartTime(date: Date?) {
            startTime = date
            with (sharedPreferences.edit()) {
                val stringDate = if (date == null) null else dateFormat.format(date)
                putString(START_TIME_KEY, stringDate)
                apply()
            }
        }

        fun setStopTime(date: Date?) {
            stopTime = date
            with(sharedPreferences.edit()) {
                val stringDate = if (date == null) null else dateFormat.format(date)
                putString(STOP_TIME_KEY, stringDate)
                apply()
            }
        }

        fun setStopwatchRunning(boolean: Boolean) {
            isStopwatchRunning = boolean
            with(sharedPreferences.edit()) {
                putBoolean(IS_STOPWATCH_RUNNING_KEY, boolean)
                apply()
            }
        }

        /**
         * returns the total elapsed time in milliseconds
         */
        fun getTotalMilliseconds() : Long? {
            var total: Long? = null
            startTime?.let { start ->
                stopTime?.let { end ->
                    total = end.time - start.time
                }
            }
            return total
        }
    }
}
