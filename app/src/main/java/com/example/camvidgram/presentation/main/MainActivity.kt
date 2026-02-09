package com.example.camvidgram.presentation.main

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.camvidgram.R
import com.example.camvidgram.presentation.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtName = findViewById<TextView>(R.id.tvWelcome)
        val txtPass = findViewById<TextView>(R.id.tvPassword)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        val email = intent.getStringExtra("email") ?: "Guest"
        val pass = intent.getStringExtra("pass") ?: "sdfsfd"

        txtName.text = "Welcome " + email
        txtPass.text = pass

        btnLogout.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

    }
}