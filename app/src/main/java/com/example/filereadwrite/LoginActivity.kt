package com.example.filereadwrite

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputLayout
import java.security.MessageDigest

class LoginActivity : AppCompatActivity() {
    private lateinit var edUsername: EditText
    private lateinit var tilUsername: TextInputLayout
    private lateinit var edPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button

    private val CREDENTIAL_SHARED_PREF = "our_shared_pref"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edUsername = findViewById(R.id.ed_username)
        tilUsername = findViewById(R.id.til_username)
        edPassword = findViewById(R.id.ed_password)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)

        btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            val credentials: SharedPreferences = getSharedPreferences(CREDENTIAL_SHARED_PREF, Context.MODE_PRIVATE)
            val strUsername = credentials.getString("Username", null)
            val strPassword = credentials.getString("Password", null)

            val usernameFromEd = edUsername.text.toString()
            val passwordFromEd = edPassword.text.toString()
            val hashedPasswordFromEd = hashPassword(passwordFromEd) // Hash the entered password

            if (strUsername != null && usernameFromEd != null && strUsername.equals(usernameFromEd, ignoreCase = true)) {
                if (strPassword != null && hashedPasswordFromEd != null && strPassword.equals(hashedPasswordFromEd, ignoreCase = true)) {
                    Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()

                    // Add this code to start MainActivity
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()  // Close LoginActivity so the user doesn't go back to it
                } else {
                    tilUsername.error = "Invalid username or password"
                    Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                tilUsername.error = "Invalid username or password"
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}