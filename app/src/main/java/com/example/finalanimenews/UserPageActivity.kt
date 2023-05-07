package com.example.finalanimenews

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.finalanimenews.Fragments.FavoriteFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class UserPageActivity  : AppCompatActivity() {


    private lateinit var quitCounter: TextView
    private lateinit var watchingCounter: TextView
    private lateinit var inPlanCounter: TextView
    private lateinit var viewedCounter: TextView
    private lateinit var postponedCounter: TextView
    private lateinit var userImage: ImageView
    private lateinit var backButton: LinearLayout
    private lateinit var backLogOut: LinearLayout
    private lateinit var userNickname: TextView
    private  lateinit var auth: FirebaseAuth
    private  lateinit var user: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_page)

        backLogOut = findViewById(R.id.back_logout)
        backButton = findViewById(R.id.back_btn)
        quitCounter = findViewById(R.id.quit)
        watchingCounter = findViewById(R.id.watching)
        inPlanCounter = findViewById(R.id.in_plan)
        viewedCounter = findViewById(R.id.viewed)
        postponedCounter = findViewById(R.id.postponed)
        userImage = findViewById(R.id.user_image)
        userNickname = findViewById(R.id.user_nic)
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            val intent = Intent(this, Login::class.java)
            this.startActivity(intent)
        } else{
            user = auth.currentUser!!
            userNickname.text = user.email
        }

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }

        val favoriteFragment = FavoriteFragment()
        val favoriteList = favoriteFragment.getListOfFavorite()
        backLogOut.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            favoriteFragment.setListOfFavorite(mutableListOf())

            val intent = Intent(this, Login::class.java)
            this.startActivity(intent)
        }


        favoriteList.forEach{e -> when(e.getStatusOfSerial()){
            "Watching" -> watchingCounter.text = (watchingCounter.text.toString().toInt() + 1).toString()
            "In the plans" -> inPlanCounter.text = (inPlanCounter.text.toString().toInt() + 1).toString()
            "Viewed" -> viewedCounter.text = (viewedCounter.text.toString().toInt() + 1).toString()
            "Postponed" -> postponedCounter.text = (postponedCounter.text.toString().toInt() + 1).toString()
            "Quit watching" -> quitCounter.text = (quitCounter.text.toString().toInt() + 1).toString()
        } }






    }
}