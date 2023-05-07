package com.example.finalanimenews

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth


class Register : AppCompatActivity() {

    private lateinit var email: TextInputEditText
    private lateinit var password: TextInputEditText
    private lateinit var register: TextView
    private lateinit var backLogin: TextView
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
        setContentView(R.layout.register_activity)

        auth = FirebaseAuth.getInstance()

        progres = findViewById(R.id.progress_bar)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        register = findViewById(R.id.register_btn)
        backLogin = findViewById(R.id.to_login)

        register.setOnClickListener {

            progres.visibility = View.VISIBLE

            if(TextUtils.isEmpty(email.text)){
                Toast.makeText(this,"Enter email",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            if(TextUtils.isEmpty(password.text)){
                Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener{ task ->
                    progres.visibility = View.GONE

                    if (task.isSuccessful) {
                        Toast.makeText(this, "Account Created.",
                            Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, Login::class.java)
                        this.startActivity(intent)

                    } else {

                        Toast.makeText(this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()

                    }
                }
        }
        backLogin.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            this.startActivity(intent)
        }
    }
}
