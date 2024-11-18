package com.example.ch9

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.widget.Button
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    // 建立兩個數值，用於計算兔子與烏龜的進度
    private var progressRabbit = 0
    private var progressTurtle = 0

    private lateinit var btnStart: Button
    private lateinit var sbRabbit: SeekBar
    private lateinit var sbTurtle: SeekBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnStart = findViewById(R.id.btn_start)
        sbRabbit = findViewById(R.id.sb_rabbit)
        sbTurtle = findViewById(R.id.sb_turtle)

        btnStart.setOnClickListener {
            btnStart.isEnabled = false // 比賽期間，不可重複操作
            progressRabbit = 0 // 初始化進度
            progressTurtle = 0
            sbRabbit.progress = 0
            sbTurtle.progress = 0
            // 執行方法
            runRabbit()
            runTurtle()
        }
    }

    private val handler = Handler(Looper.getMainLooper()) { msg ->
        when (msg.what) {
            1 -> sbRabbit.progress = progressRabbit
            2 -> sbTurtle.progress = progressTurtle
        }

        // 判斷誰快
        when {
            progressRabbit >= 100 && progressTurtle < 100 -> {
                Toast.makeText(this, "兔子勝", Toast.LENGTH_SHORT).show()
                btnStart.isEnabled = true
            }
            progressTurtle >= 100 && progressRabbit < 100 -> {
                Toast.makeText(this, "烏龜勝", Toast.LENGTH_SHORT).show()
                btnStart.isEnabled = true
            }
        }

        false
    }

    private fun runRabbit() {
        Thread {
            val sleepProbability = arrayOf(true, true, false) // 兔子有三分之二的機率會偷懶

            while (progressRabbit <= 100 && progressTurtle < 100) {
                try {
                    Thread.sleep(100) // 延遲0.1秒
                    if (sleepProbability[(Math.random() * 3).toInt()]) {
                        Thread.sleep(300) // 兔子休息0.3秒
                    }
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressRabbit += 3 // 每次三步

                val msg = Message.obtain() // 建立Message物件
                msg.what = 1 // 加入編號
                handler.sendMessage(msg)
            }
        }.start()
    }

    private fun runTurtle() {
        Thread {
            while (progressTurtle <= 100 && progressRabbit < 100) {
                try {
                    Thread.sleep(100) // 延遲0.1秒
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
                progressTurtle += 1 // 每次一步

                val msg = Message.obtain() // 建立Message物件
                msg.what = 2 // 加入編號
                handler.sendMessage(msg)
            }
        }.start()
    }
}
