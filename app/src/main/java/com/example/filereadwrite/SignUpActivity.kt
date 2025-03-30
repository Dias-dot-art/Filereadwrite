package com.example.filereadwrite

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import java.security.MessageDigest

class SignUpActivity : AppCompatActivity() {
    private lateinit var edUsername: EditText
    private lateinit var tilUsername: TextInputLayout
    private lateinit var edPassword: EditText
    private lateinit var tilPassword: TextInputLayout
    private lateinit var edConfirmPassword: EditText
    private lateinit var tilConfirmPassword: TextInputLayout
    private lateinit var btnCreateUser: Button

    private val CREDENTIAL_SHARED_PREF = "our_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        edUsername = findViewById(R.id.ed_username)
        tilUsername = findViewById(R.id.til_username)
        edPassword = findViewById(R.id.ed_password)
        tilPassword = findViewById(R.id.til_password)
        edConfirmPassword = findViewById(R.id.ed_confirm_pwd)
        tilConfirmPassword = findViewById(R.id.til_confirm_pwd)
        btnCreateUser = findViewById(R.id.btn_create_user)

        btnCreateUser.setOnClickListener {
            val username = edUsername.text.toString().trim()
            val password = edPassword.text.toString().trim()
            val confirmPassword = edConfirmPassword.text.toString().trim()

            if (!validateInput(username, password, confirmPassword)) {
                return@setOnClickListener // Exit if validation fails
            }

            val hashedPassword = hashPassword(password)

            val credentials: SharedPreferences = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)
            val editor = credentials.edit()
            editor.putString("Username", username)
            editor.putString("Password", hashedPassword)
            editor.apply()

            Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show() // Optional feedback

            finish()
        }
    }

    private fun validateInput(username: String, password: String, confirmPassword: String): Boolean {
        if (username.isEmpty()) {
            tilUsername.error = "Username is required"
            return false
        } else {
            tilUsername.error = null
        }

        if (password.isEmpty()) {
            tilPassword.error = "Password is required"
            return false
        } else {
            tilPassword.error = null
        }

        if (password.length < 6) {
            tilPassword.error = "Password must be at least 6 characters"
            return false
        } else {
            tilPassword.error = null
        }

        if (password != confirmPassword) {
            tilConfirmPassword.error = "Passwords do not match"
            return false
        } else {
            tilConfirmPassword.error = null
        }

        return true
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}