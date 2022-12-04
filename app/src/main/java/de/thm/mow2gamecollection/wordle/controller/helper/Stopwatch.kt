package de.thm.mow2gamecollection.wordle.controller.helper

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

// Debugging
private const val DEBUG = true
private const val TAG = "Stopwatch"

/**
 * @param activity The Activity that uses and displays the time
 * @param textView: the TextView that is used to show the time
 */
class Stopwatch(val activity: Activity, val textView: TextView) {
    private lateinit var dataHelper: DataHelper
    private val timer = Timer()

    init {
        dataHelper = DataHelper(activity)
    }

    fun onResume() {
        if (DEBUG) Log.d(TAG, "isStopwatchRunning: ${dataHelper.isStopwatchRunning}")
        if(dataHelper.isStopwatchRunning) {
            dataHelper.setStopwatchRunning(true)
        } else {
            dataHelper.setStopwatchRunning(false)
            if (dataHelper.startTime != null && dataHelper.stopTime != null) {
                if (DEBUG) Log.d(TAG, "startTime and stop not null")
                val time = Date().time - calculateRestartTime().time
                textView.text = timeStringFromLong(time)
            }
            start()
        }

        timer.scheduleAtFixedRate(UpdateUiTimerTask(), 0, 500)
    }

    fun resetTimer() {
        dataHelper.apply {
            setStartTime(null)
            setStopTime(null)
            dataHelper.setStopwatchRunning(false)
        }
        textView.text = timeStringFromLong(0)
    }

    fun stop() {
        if (dataHelper.isStopwatchRunning) {
            dataHelper.setStopTime(Date())
            dataHelper.setStopwatchRunning(false)
        } else {
            Log.e(TAG, "startStopAction")
        }
    }

    fun start() {
        if (dataHelper.stopTime != null) {
            dataHelper.setStartTime(calculateRestartTime())
            dataHelper.setStopTime(null)
        } else {
            dataHelper.setStartTime(Date())
        }
        dataHelper.setStopwatchRunning(true)
    }

    fun getTotalTimeString() : String? {
        dataHelper.getTotalMilliseconds()?.let {
            return timeStringFromLong(it)
        }
        return null
    }

    private fun calculateRestartTime(): Date {
        var restartTimeInMilliseconds = System.currentTimeMillis()
        dataHelper.getTotalMilliseconds()?.let { timeElapsed ->
            restartTimeInMilliseconds -= timeElapsed
        }
        return Date(restartTimeInMilliseconds)
    }

    private inner class UpdateUiTimerTask: TimerTask() {
        override fun run() {
            if (dataHelper.isStopwatchRunning) {
                val time = Date().time - dataHelper.startTime!!.time

                activity.runOnUiThread {
                    textView.text = timeStringFromLong(time)
                }
            }
        }
    }

    companion object {
        const val PREFERENCES_KEY = "preferences"
        const val START_TIME_KEY = "startTime"
        const val STOP_TIME_KEY = "stopTime"
        const val IS_STOPWATCH_RUNNING_KEY = "isStopwatchRunning"

        fun timeStringFromLong(timeMilliseconds: Long): String {
            val seconds = (timeMilliseconds / 1000) % 60
            val minutes = (timeMilliseconds / (1000 * 60) % 60)
            val hours = (timeMilliseconds / (1000 * 60 * 60) % 24)
            return makeTimeString(hours, minutes, seconds)
        }

        fun makeTimeString(hours: Long, minutes: Long, seconds: Long): String {
            return when (hours) {
                0L -> String.format("%02d:%02d", minutes, seconds)
                else -> String.format("%02d:%02d:%02d", hours, minutes, seconds)
            }
        }
    }


    private inner class DataHelper(context: Context) {
        private val sharedPreferences = context.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE)
        private val dateFormat = SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.getDefault())

        var isStopwatchRunning = false
            private set
        var startTime: Date? = null
            private set
        var stopTime: Date? = null
            private set

        init {
            Log.d(TAG, "init")
            isStopwatchRunning = sharedPreferences.getBoolean(IS_STOPWATCH_RUNNING_KEY, false)

            val startTimeString = sharedPreferences.getString(START_TIME_KEY, null)
            startTimeString?.let {
                startTime = dateFormat.parse(it)
            }

            val stopTimeString = sharedPreferences.getString(STOP_TIME_KEY, null)
            stopTimeString?.let {
                stopTime = dateFormat.parse(it)
            }
            Log.d(TAG, "init: isStopWatchRunning: $isStopwatchRunning startTime: $startTime \t stopTime: $stopTime")
        }

        fun setStartTime(date: Date?) {
            Log.d(TAG, "setStartTime $date")
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

            Log.d(TAG, "setStopTime ${
                sharedPreferences.getString(STOP_TIME_KEY, "nothing there")
            }")
        }

        fun setStopwatchRunning(boolean: Boolean) {
            Log.d(TAG, "setStopWatchRunning $boolean")
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
