package com.example.camvidgram.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.camvidgram.R
import com.example.camvidgram.domain.models.LoginState
import com.example.camvidgram.presentation.login.LoginViewModel
import com.example.camvidgram.presentation.main.MainActivity
import com.example.camvidgram.presentation.registeruser.RegisterUserActivity
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Get views
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val tvSignUp = findViewById<TextView>(R.id.tvSignUp)
        val tvForgotPassword = findViewById<TextView>(R.id.tvForgotPassword)

        // Set click listeners
        setupClickListeners(etEmail, etPassword, tilEmail, tilPassword, btnLogin, tvSignUp, tvForgotPassword)

        observeLoginState(etEmail, etPassword)

    }

    private fun setupClickListeners(
        etEmail: EditText,
        etPassword: EditText,
        tilEmail: TextInputLayout,
        tilPassword: TextInputLayout,
        btnLogin: Button,
        tvSignUp: TextView,
        tvForgotPassword: TextView
    ) {
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString()

            if (validateInput(email, password, tilEmail, tilPassword)) {
                viewModel.login(email, password)
            }
        }

        tvSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterUserActivity::class.java)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            Toast.makeText(this, "Forgot password clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateInput(
        email: String,
        password: String,
        tilEmail: TextInputLayout,
        tilPassword: TextInputLayout
    ): Boolean {
        var isValid = true

        if (email.isEmpty()) {
            tilEmail.error = "Email is required"
            isValid = false
        } else {
            tilEmail.error = null
        }

        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            isValid = false
        } else {
            tilPassword.error = null
        }

        return isValid
    }

    private fun observeLoginState(etEmail: EditText?, etPassword: EditText?) {
        lifecycleScope.launch {
            viewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Idle -> {
                        // Do nothing
                    }

                    is LoginState.Loading -> {
                        // Show loading indicator
                        findViewById<Button>(R.id.btnLogin).text = "Logging in..."
                        findViewById<Button>(R.id.btnLogin).isEnabled = false
                    }

                    is LoginState.Success -> {
                        // Reset button
                        findViewById<Button>(R.id.btnLogin).text = "Log In"
                        findViewById<Button>(R.id.btnLogin).isEnabled = true

                        // Show success message
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                            putExtra("email",etEmail?.text.toString())
                            putExtra("pass", etPassword?.text.toString())
                        }
                        // Navigate to Main
                        startActivity(intent)
                        finish()
                    }

                    is LoginState.Error -> {
                        // Reset button
                        findViewById<Button>(R.id.btnLogin).text = "Log In"
                        findViewById<Button>(R.id.btnLogin).isEnabled = true

                        // Show error
                        Toast.makeText(this@LoginActivity, state.message, Toast.LENGTH_SHORT).show()

                        // Reset ViewModel state after showing error
                        viewModel.resetState()
                    }
                }
            }
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            currentFocus!!.clearFocus()
        }
        return super.dispatchTouchEvent(ev)
    }
}