package com.example.finalanimenews


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalanimenews.Fragments.FavoriteFragment
import com.squareup.picasso.Picasso

class RecyclerAdapter(private val listOfAnime: MutableList<Anime>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycle_card,parent,false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return  listOfAnime.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favoriteFragment: FavoriteFragment = FavoriteFragment()
        var favoriteList = favoriteFragment.getListOfFavorite()


        if(!favoriteList.isNullOrEmpty()){


            if(favoriteList.contains(listOfAnime[position])){

                listOfAnime[position].setFavorite(true)
            }
        }


        Picasso.get()
            .load(listOfAnime[position].getImage())
            .into(holder.itemImage)
        
        holder.itemType.text = listOfAnime[position].getReleaseDate()

        holder.itemTitle.text = listOfAnime[position].getTitle()

        if(!listOfAnime[position].getFavorite()){
            holder.itemButton.setImageResource(R.drawable.favorite_icon_for_butn )
        }else if(listOfAnime[position].getFavorite()){
            holder.itemButton.setImageResource(R.drawable.favorite_colored_icon)
        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemImage: ImageView
        var itemTitle: TextView
        var itemType: TextView
        var itemContainer: RelativeLayout
        var itemButton: ImageView

        init {
            itemImage = itemView.findViewById(R.id.anime_image)
            itemTitle = itemView.findViewById(R.id.anime_title)
            itemType = itemView.findViewById(R.id.anime_type)
            itemButton = itemView.findViewById(R.id.favorite_add_btn)
            itemContainer = itemView.findViewById(R.id.container)


            itemContainer.setOnClickListener {

                val intent = Intent(itemView.context, AnimeMainPageActivity::class.java)
                intent.putExtra("id",listOfAnime[position].getId())
                intent.putExtra("title",listOfAnime[position].getTitle())
                intent.putExtra("image",listOfAnime[position].getImage())
                intent.putExtra("url",listOfAnime[position].getUrl())
                intent.putExtra("releaseDate",listOfAnime[position].getReleaseDate())
                itemView.context.startActivity(intent)
            }

            itemButton.setOnClickListener {
                if(!listOfAnime[position].getFavorite()){
                    itemButton.setImageResource(R.drawable.favorite_colored_icon)
                    listOfAnime[position].setFavorite(true)

                    val fragmentFragment: FavoriteFragment = FavoriteFragment()
                    fragmentFragment.AddNewFavoriteAnime(listOfAnime[position],0)

                }else{
                    itemButton.setImageResource(R.drawable.favorite_icon_for_butn )
                    listOfAnime[position].setFavorite(false)
                    val fragmentFragment: FavoriteFragment = FavoriteFragment()
                    fragmentFragment.RemoveFavoriteAnime(listOfAnime[position],0)
                }
            }




        }

    }



}