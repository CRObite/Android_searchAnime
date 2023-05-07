package com.example.finalanimenews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.finalanimenews.Fragments.FavoriteFragment
import com.example.finalanimenews.Fragments.InfoFragment
import com.example.finalanimenews.Fragments.NewsFragment

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.json.JSONArray


private lateinit var bottomNavBar: BottomNavigationView
private lateinit var animeFragment: NewsFragment
private lateinit var infoFragment: InfoFragment
private lateinit var favoriteFragment: FavoriteFragment
internal var currentJsonArray: JSONArray? = null
private lateinit var reff: DatabaseReference
private  lateinit var auth: FirebaseAuth
private  lateinit var user: FirebaseUser

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        bottomNavBar = findViewById(R.id.bottom_nav)


        animeFragment = NewsFragment()
        infoFragment = InfoFragment()
        favoriteFragment = FavoriteFragment()

        val favoriteFragment = FavoriteFragment()
        val favoriteList = favoriteFragment.getListOfFavorite()
        reff = FirebaseDatabase.getInstance().reference.child("Users")
        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null){
            val intent = Intent(this, Login::class.java)
            this.startActivity(intent)
        } else{
            user = auth.currentUser!!
            reff.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.hasChild(user.uid) && favoriteList.isNullOrEmpty()){
                        val path = snapshot.child(user.uid).child("Favorites")

                        println(user.uid)
                        path.children.forEach { e->
                            var anime = Anime(e.child("id").value.toString(),
                                e.child("title").value.toString(),
                                e.child("image").value.toString(),
                                e.child("hasSubOrDub").value.toString().toBoolean(),
                                e.child("url").value.toString(),
                                e.child("releaseDate").value.toString(),
                                true,
                                e.child("statusOfSerial").value.toString())


                            val fragmentFragment: FavoriteFragment = FavoriteFragment()
                            fragmentFragment.AddNewFavoriteAnime(anime,2)
                        }
                    } else{
                        reff.child(user.uid)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })



        }








        supportFragmentManager.beginTransaction().replace(R.id.main_fragment, animeFragment).commit()


        bottomNavBar.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.anime ->{ supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, animeFragment).commit()
                    bottomNavBar.menu.getItem(2).isChecked = false
                    bottomNavBar.menu.getItem(1).isChecked = false
                    bottomNavBar.menu.getItem(0).isChecked = true}
                R.id.favorite ->{ supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, favoriteFragment).commit()
                    bottomNavBar.menu.getItem(2).isChecked = false
                    bottomNavBar.menu.getItem(0).isChecked = false
                    bottomNavBar.menu.getItem(1).isChecked = true}
                R.id.info ->{ supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment, infoFragment).commit()
                    bottomNavBar.menu.getItem(0).isChecked = false
                    bottomNavBar.menu.getItem(1).isChecked = false
                    bottomNavBar.menu.getItem(2).isChecked = true}
            }

            false
        }



    }

    fun onModeChanged(){
        bottomNavBar = findViewById(R.id.bottom_nav)

        bottomNavBar.menu.getItem(2).isChecked = false
        bottomNavBar.menu.getItem(1).isChecked = false
        bottomNavBar.menu.getItem(0).isChecked = true
    }
}