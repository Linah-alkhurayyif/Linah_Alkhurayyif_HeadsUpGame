package com.example.linah_alkhurayyif_headsupgame

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        game.setOnClickListener {
            intent = Intent(applicationContext, headsUp::class.java)
            startActivity(intent)
        }
        home.setOnClickListener {
            intent = Intent(applicationContext, ViewCelebritie::class.java)
            startActivity(intent)
        }
    }
}