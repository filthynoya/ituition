package com.example.i_tuition

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.content.Intent
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.core.view.isVisible

class Login : AppCompatActivity() {
    private lateinit var LogInEmail : EditText
    private lateinit var LogInPassword : EditText
    private lateinit var LogInBtn : Button
    private lateinit var SignUpBtn : Button
    private lateinit var ProgressBar: ProgressBar

    private lateinit var Auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init ()
    }

    override fun onStart() {
        super.onStart()

        checkIfTheUserLoggedIn ()
        checkForSignUp ()
        checkForLogIn ()
    }

    private fun init () {
        Auth = Firebase.auth
        ProgressBar = findViewById(R.id.progressbarID)
        LogInEmail = findViewById(R.id.login_email_text)
        LogInPassword = findViewById(R.id.login_password_text)
        LogInBtn = findViewById(R.id.log_in_btn)
        SignUpBtn = findViewById(R.id.sign_up_btn)
    }

    private fun checkIfTheUserLoggedIn () {
        val user = Auth.currentUser

        if (user != null) {
            val switchActivityIntent = Intent(this, MainActivity::class.java)
            startActivity(switchActivityIntent)
        }

    }

    private fun checkForSignUp () {
        SignUpBtn.setOnClickListener {
            val switchActivityIntent = Intent(this, SignUp::class.java)
            startActivity(switchActivityIntent)
        }
    }

    private fun checkForLogIn () {
        LogInBtn.setOnClickListener {

            ProgressBar.visibility= View.VISIBLE
            if (LogInEmail.text.toString() == "" || LogInPassword.text.toString() == "") {
                Toast.makeText(baseContext, "Please fill up necessary information.",
                    Toast.LENGTH_SHORT).show()
            } else {
                firebaseLogin(LogInEmail.text.toString(), LogInPassword.text.toString())
            }
        }

    }

    private fun firebaseLogin (email : String, password : String) {
        Auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->


                if (task.isSuccessful) {
                    ProgressBar.visibility =View.INVISIBLE
                    val switchActivityIntent = Intent(this, MainActivity::class.java)
                    startActivity(switchActivityIntent)
                } else {
                    ProgressBar.visibility =View.INVISIBLE
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}