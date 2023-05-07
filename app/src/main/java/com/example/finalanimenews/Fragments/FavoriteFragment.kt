package com.example.finalanimenews.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalanimenews.Anime
import com.example.finalanimenews.R
import com.example.finalanimenews.RecyclerFavoriteAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import org.json.JSONArray

private var currentJsonArray: JSONArray? = null
private var adapter: RecyclerView.Adapter<RecyclerFavoriteAdapter.ViewHolder>? = null
class FavoriteFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager?= null
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyText: TextView
    private lateinit var reff: DatabaseReference
    private  lateinit var auth: FirebaseAuth
    private  lateinit var user: FirebaseUser

    companion object{
        private var listOfFavorite: MutableList<Anime> = mutableListOf()
        private var count:Long = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        layoutManager = LinearLayoutManager(view.context)
        recyclerView = view.findViewById(R.id.recycler_view_favorite)
        recyclerView.layoutManager = layoutManager


        emptyText = view.findViewById(R.id.emptyText)
        if(listOfFavorite.isNullOrEmpty()){
            emptyText.visibility = View.VISIBLE
        } else{
            emptyText.visibility = View.INVISIBLE
        }

        adapter = RecyclerFavoriteAdapter(listOfFavorite)
        recyclerView.adapter = adapter


        return view
    }


    fun AddNewFavoriteAnime(anime: Anime, type: Int){

        listOfFavorite.add(anime)

        if(type!=2) {
            auth = FirebaseAuth.getInstance()
            user = auth.currentUser!!
            reff = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid)
                .child("Favorites")


            reff.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        count = snapshot.children.last().key.toString().toLong()
                    }


                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }


            })

            reff.child((count+1).toString()).setValue(anime)
            count++
        }




        if(type==1){

            adapter?.notifyDataSetChanged()
        }



    }

    fun RemoveFavoriteAnime(anime: Anime, type: Int){

        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
        if(!listOfFavorite.isNullOrEmpty()){

            for(i in listOfFavorite){

                if(i.getId() == anime.getId()){

                    listOfFavorite.remove(i)

                    reff = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid).child("Favorites")
                    reff.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            snapshot.children.forEach { e-> if(e.child("id").value.toString() == anime.getId()){
                                println(e.child("id").value.toString())
                                reff.child(e.key.toString()).removeValue()
                            } }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })


                    break

                }
            }
        }

        if(type==1){

            adapter?.notifyDataSetChanged()
        }
    }

    fun getListOfFavorite(): MutableList<Anime>{
        return listOfFavorite
    }

    fun setListOfFavorite(list: MutableList<Anime>){
        listOfFavorite = list
    }

}