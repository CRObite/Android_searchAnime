package com.example.finalanimenews

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var login: TextView
    private lateinit var backRegister: TextView
    private  lateinit var auth: FirebaseAuth
    private lateinit var progres: ProgressBar


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        auth = FirebaseAuth.getInstance()

        progres = findViewById(R.id.progress_bar)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        login = findViewById(R.id.login_btn)
        backRegister = findViewById(R.id.to_register)

        login.setOnClickListener {

            progres.visibility = View.VISIBLE

            if (TextUtils.isEmpty(email.text)) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password.text)) {
                Toast.makeText(this, "Enter password", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    progres.visibility = View.GONE

                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information

                        Toast.makeText(this, "Login Successful",
                            Toast.LENGTH_SHORT).show()


                        val intent = Intent(this, MainActivity::class.java)
                        this.startActivity(intent)

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }

        }

        backRegister.setOnClickListener {
            val intent = Intent(this, Register::class.java)
            this.startActivity(intent)
        }
    }
}
