package com.example.finalanimenews


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.finalanimenews.Fragments.FavoriteFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class RecyclerFavoriteAdapter(private val listOfFavorite: MutableList<Anime>): RecyclerView.Adapter<RecyclerFavoriteAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycle_card2,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  listOfFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        Picasso.get()
            .load(listOfFavorite[position].getImage())
            .into(holder.itemImage)

        holder.itemType.text = listOfFavorite[position].getReleaseDate()

        holder.itemTitle.text = listOfFavorite[position].getTitle()
        if(listOfFavorite[position].getStatusOfSerial() != "None"){
            val spinnerPosition: Int = holder.spinnerAdapter.getPosition(listOfFavorite[position].getStatusOfSerial())
            holder.itemSpinner.setSelection(spinnerPosition)

        }
        if(!listOfFavorite[position].getFavorite()){
            holder.itemButton.setImageResource(R.drawable.favorite_icon_for_butn )
        }else if(listOfFavorite[position].getFavorite()){
            holder.itemButton.setImageResource(R.drawable.favorite_colored_icon)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        AdapterView.OnItemSelectedListener {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemType: TextView
        var itemContainer: RelativeLayout
        var itemButton: ImageView
        var itemSpinner: Spinner
        var spinnerAdapter: ArrayAdapter<CharSequence>
        var reff: DatabaseReference

        init {
            itemImage = itemView.findViewById(R.id.anime_image)
            itemTitle = itemView.findViewById(R.id.anime_title)
            itemType = itemView.findViewById(R.id.anime_type)
            itemButton = itemView.findViewById(R.id.favorite_add_btn)
            itemContainer = itemView.findViewById(R.id.container)
            itemSpinner = itemView.findViewById(R.id.spinner)

            spinnerAdapter = ArrayAdapter.createFromResource(itemView.context,R.array.serial_status
                ,android.R.layout.simple_spinner_item)
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            itemSpinner.adapter = spinnerAdapter

            itemSpinner.onItemSelectedListener = this

            reff = FirebaseDatabase.getInstance().reference.child("Favorites")

            itemContainer.setOnClickListener {
                val intent = Intent(itemView.context, AnimeMainPageActivity::class.java)
                intent.putExtra("id",listOfFavorite[position].getId())
                intent.putExtra("title",listOfFavorite[position].getTitle())
                intent.putExtra("image",listOfFavorite[position].getImage())
                intent.putExtra("url",listOfFavorite[position].getUrl())
                intent.putExtra("releaseDate",listOfFavorite[position].getReleaseDate())
                itemView.context.startActivity(intent)
            }

            itemButton.setOnClickListener {
                if(!listOfFavorite[position].getFavorite()){
                    itemButton.setImageResource(R.drawable.favorite_colored_icon)
                    listOfFavorite[position].setFavorite(true)

                    val fragmentFragment: FavoriteFragment = FavoriteFragment()
                    fragmentFragment.AddNewFavoriteAnime(listOfFavorite[position],1)



                }else{
                    itemButton.setImageResource(R.drawable.favorite_icon_for_butn )
                    listOfFavorite[position].setFavorite(false)
                    val fragmentFragment: FavoriteFragment = FavoriteFragment()
                    fragmentFragment.RemoveFavoriteAnime(listOfFavorite[position],1)
                }
            }






        }

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            var choice = p0?.getItemAtPosition(p2).toString()
            listOfFavorite[position].setStatusOfSerial(choice)


            var auth= FirebaseAuth.getInstance()
            var user = auth.currentUser!!
            reff = FirebaseDatabase.getInstance().reference.child("Users").child(user.uid).child("Favorites")
            reff.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { e-> if(e.child("id").value.toString() == listOfFavorite[position].getId()){

                        reff.child(e.key.toString()).child("statusOfSerial").setValue(choice)
                    } }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {
            TODO("Not yet implemented")
        }

    }



}