package com.example.camvidgram.presentation.registeruser

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.camvidgram.R
import com.example.camvidgram.domain.models.RegisterState
import com.example.camvidgram.presentation.login.LoginActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch
import kotlin.getValue

class RegisterUserActivity : AppCompatActivity() {

    private val viewModel: RegisterUserViewModel by viewModels()
    private lateinit var loadingOverlay : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register_user)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        handleBackPress()

        loadingOverlay = findViewById<LinearLayout>(R.id.loadingOverlay)
        val etUserName = findViewById<TextInputEditText>(R.id.etUsername)
        val tilUserName = findViewById<TextInputLayout>(R.id.tilUsername)
        val etEmail = findViewById<TextInputEditText>(R.id.etEmail)
        val tilEmail = findViewById<TextInputLayout>(R.id.tilEmail)
        val etPassword = findViewById<TextInputEditText>(R.id.etPassword)
        val tilPassword = findViewById<TextInputLayout>(R.id.tilPassword)
        val etConfirmPassword = findViewById<TextInputEditText>(R.id.etConfirmPassword)
        val tilConfirmPassword = findViewById<TextInputLayout>(R.id.tilConfirmPassword)
        val btnSignUp = findViewById<Button>(R.id.btnSignUp)
        val tvLogin = findViewById<TextView>(R.id.tvLogin)

        setupClickListeners(etUserName,tilUserName,etEmail,tilEmail,etPassword,tilPassword,etConfirmPassword,tilConfirmPassword,btnSignUp,tvLogin)

        observeLoginState()
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            viewModel.registerUserState.collect { state ->
                when(state){
                    is RegisterState.Idle -> {
                        // Do nothing
                    }
                    is RegisterState.Loading -> {
                        findViewById<Button>(R.id.btnSignUp).text = "Signing up..."
                        findViewById<Button>(R.id.btnSignUp).isEnabled = false
                        loadingOverlay.visibility = LinearLayout.VISIBLE
                    }
                    is RegisterState.Success -> {
                        findViewById<Button>(R.id.btnSignUp).text = "Sign Up"
                        findViewById<Button>(R.id.btnSignUp).isEnabled = true
                        loadingOverlay.visibility = LinearLayout.GONE

                        // Show success message
                        Toast.makeText(this@RegisterUserActivity, state.message, Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@RegisterUserActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    is RegisterState.Error -> {
                        findViewById<Button>(R.id.btnSignUp).text = "Sign Up"
                        findViewById<Button>(R.id.btnSignUp).isEnabled = true

                        // Show success message
                        Toast.makeText(this@RegisterUserActivity, state.message, Toast.LENGTH_SHORT).show()

                        // Reset ViewModel state after showing error
                        viewModel.resetState()
                    }
                }

            }
        }
    }

    private fun handleBackPress() {
        onBackPressedDispatcher.addCallback(this) {
            val intent = Intent(this@RegisterUserActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setupClickListeners(
        etUserName: TextInputEditText,
        tilUserName: TextInputLayout,
        etEmail: TextInputEditText,
        tilEmail: TextInputLayout,
        etPassword: TextInputEditText,
        tilPassword: TextInputLayout,
        etConfirmPassword: TextInputEditText,
        tilConfirmPassword: TextInputLayout,
        btnSignUp: Button,
        tvLogin: TextView
    ) {
        btnSignUp.setOnClickListener {
            // Handle sign up button click
            val username = etUserName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (validateInput(username, email, password, confirmPassword, tilUserName, tilEmail, tilPassword, tilConfirmPassword)) {
                viewModel.registerUser(username, email, password, confirmPassword)
            }
        }

        tvLogin.setOnClickListener {
            // Handle login text click
            val intent = Intent(this@RegisterUserActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun validateInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String,
        tilUserName: TextInputLayout,
        tilEmail: TextInputLayout,
        tilPassword: TextInputLayout,
        tilConfirmPassword: TextInputLayout
    ): Boolean {

        var isValid = true

        tilUserName.error = null
        tilEmail.error = null
        tilPassword.error = null
        tilConfirmPassword.error = null

        if (username.isBlank()) {
            tilUserName.error = "Username is required"
            isValid = false
        }

        if (email.isBlank()) {
            tilEmail.error = "Email is required"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            tilEmail.error = "Enter a valid email"
            isValid = false
        }

        if (password.isBlank()) {
            tilPassword.error = "Password is required"
            isValid = false
        } else if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            isValid = false
        }

        if (confirmPassword.isBlank()) {
            tilConfirmPassword.error = "Confirm password is required"
            isValid = false
        } else if (password != confirmPassword) {
            tilPassword.error = "Passwords do not match"
            tilConfirmPassword.error = "Passwords do not match"
            isValid = false
        }

        return isValid
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
