package com.example.foodorderingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.ActionBar

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val actionBar: ActionBar?=supportActionBar
        actionBar?.hide()

        Handler().postDelayed({
            startActivity(Intent(this@splashActivity,MainActivity::class.java))
            finish()

        },2000)




    }
}