package com.example.stopwatch

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.widget.Button
import android.widget.TextView


class MainActivity : AppCompatActivity() {
    var flagStart = false
    var updateTime: Long = 0
    var timeBuff: Long = 0
    var timeInMilliseconds: Long = 0
    var startTime: Long = 0

    var minutes: Int = 0
    var seconds: Int = 0
    var milliSeconds: Int = 0

    lateinit var handler: Handler
    lateinit var startButton: Button
    lateinit var resetButton: Button
    lateinit var timeText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        startButton = findViewById(R.id.startTime)
        resetButton = findViewById(R.id.resetTime)
        timeText = findViewById(R.id.timeText)
        stopwatchRun()
    }

    /**
     * Уставнавливает обработчики для кнопок "Старт" и "Сброс",
     * при нажатии на "Старт" запускается секундомер в новом потоке,
     * при нажатии "Сброс" секундомер обнуляется и поток останавливается
     */
    private fun stopwatchRun() {
        handler = Handler()

        startButton.setOnClickListener {
            if(!flagStart) {
                startButton.text = "Пауза"
                startButton.setBackgroundResource(R.drawable.pause_button)
                startTime = SystemClock.uptimeMillis()
                handler.postDelayed(runnable, 0)
                resetButton.isEnabled = false
                flagStart = true
            } else {
                startButton.text = "Старт"
                startButton.setBackgroundResource(R.drawable.start_button)
                timeBuff += timeInMilliseconds
                handler.removeCallbacks(runnable)
                flagStart = false
                resetButton.isEnabled = true
            }
        }

        resetButton.setOnClickListener {
            timeInMilliseconds = 0
            timeBuff = 0
            updateTime = 0
            seconds = 0
            minutes = 0
            seconds = 0
            milliSeconds = 0
            timeText.setText(R.string.clear_time)
            handler.removeCallbacks(runnable)
            resetButton.isEnabled = false
        }
    }

    // поток, который запускается при старте секундомера
    private var runnable: Runnable = object : Runnable {
        override fun run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime
            updateTime = timeBuff + timeInMilliseconds
            seconds = (updateTime / 1000).toInt()
            minutes = seconds / 60
            seconds %= 60
            milliSeconds = (updateTime % 1000).toInt()
            timeText.text = ("" + minutes + ":"
                    + String.format("%02d", seconds) + ":"
                    + String.format("%03d", milliSeconds))
            handler.postDelayed(this, 0)
        }
    }
}
