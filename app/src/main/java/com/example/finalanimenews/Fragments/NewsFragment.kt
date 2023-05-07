package com.example.finalanimenews.Fragments

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalanimenews.*
import com.example.finalanimenews.util.NetworkUtils
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.util.*

private lateinit var loading: ProgressBar
private lateinit var errorText: TextView
private lateinit var noResultText: TextView
private var currentJsonArray: JSONArray? = null
private lateinit var buttonSubmit: ImageView




class NewsFragment : Fragment() {

    private var layoutManager: RecyclerView.LayoutManager?= null
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchInput: EditText
    private lateinit var userButton: ImageView
    private var delay : Long = 1000
    private var timer = Timer()

    class QueryTask(private val recyclerView: RecyclerView):  AsyncTask<URL, Void, String>() {
        private var listOfAnime: MutableList<Anime> = mutableListOf()
        private var adapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>? = null

        override fun onPreExecute() {
            loading.visibility = View.VISIBLE
            noResultText.visibility = View.INVISIBLE
            errorText.visibility = View.INVISIBLE
        }

        override fun doInBackground(vararg p0: URL?): String? {

            var response : String? = NetworkUtils.getResponseFromURL(p0[0]!!)


            return response
        }

        override fun onPostExecute(result: String?) {
            var info: String? = null


            if(result != null && result != "" ) {
                try {
                    val jsonResponse: JSONObject = JSONObject(result)
                    val jsonArray: JSONArray = jsonResponse.getJSONArray("results")

                    com.example.finalanimenews.currentJsonArray = jsonArray

                    val favoriteFragment = FavoriteFragment()
                    val favoriteList = favoriteFragment.getListOfFavorite()

                    for(i in 0 until jsonArray.length()){

                        if(!favoriteList.isNullOrEmpty()){

                            if(favoriteList.any{it.getId() == jsonArray.getJSONObject(i).getString("id")}){
                                var anime = Anime(
                                    jsonArray.getJSONObject(i).getString("id"),
                                    jsonArray.getJSONObject(i).getString("title"),
                                    jsonArray.getJSONObject(i).getString("image"),
                                    jsonArray.getJSONObject(i).getString("subOrDub").toBoolean(),
                                    jsonArray.getJSONObject(i).getString("url"),
                                    jsonArray.getJSONObject(i).getString("releaseDate"),
                                    true
                                )

                                listOfAnime.add(anime)
                            }else if(favoriteList.all{it.getId() != jsonArray.getJSONObject(i).getString("id")}){
                                var anime = Anime(
                                    jsonArray.getJSONObject(i).getString("id"),
                                    jsonArray.getJSONObject(i).getString("title"),
                                    jsonArray.getJSONObject(i).getString("image"),
                                    jsonArray.getJSONObject(i).getString("subOrDub").toBoolean(),
                                    jsonArray.getJSONObject(i).getString("url"),
                                    jsonArray.getJSONObject(i).getString("releaseDate"),
                                    false
                                )

                                listOfAnime.add(anime)
                            }
                        }else {
                            var anime = Anime(
                                jsonArray.getJSONObject(i).getString("id"),
                                jsonArray.getJSONObject(i).getString("title"),
                                jsonArray.getJSONObject(i).getString("image"),
                                jsonArray.getJSONObject(i).getString("subOrDub").toBoolean(),
                                jsonArray.getJSONObject(i).getString("url"),
                                jsonArray.getJSONObject(i).getString("releaseDate"),
                                false
                            )

                            listOfAnime.add(anime)
                        }


                    }


                    if (!listOfAnime.isNullOrEmpty()){
                        adapter = RecyclerAdapter(listOfAnime)
                        recyclerView.adapter = adapter

                    }else {
                        noResultText.visibility = View.VISIBLE
                    }
                } catch (e: JSONException){
                    recyclerView.adapter = null
                    noResultText.visibility = View.VISIBLE
                }

            } else if(result == null || result == ""){
                errorText.visibility = View.VISIBLE
                recyclerView.adapter = null
            }

            loading.visibility = View.INVISIBLE

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news, container, false)
        loading = view.findViewById(R.id.loading)
        errorText = view.findViewById(R.id.errorText)
        noResultText = view.findViewById(R.id.noResoultText)

        layoutManager = LinearLayoutManager(view.context)
        recyclerView = view.findViewById(R.id.recycler_view_news)
        recyclerView.layoutManager = layoutManager
        userButton = view.findViewById(R.id.user_button)
        buttonSubmit = view.findViewById(R.id.button_submit)

        userButton.setOnClickListener {
            val intent = Intent(view.context, UserPageActivity::class.java)
            view.context.startActivity(intent)
        }


        var generatedURL: URL = NetworkUtils.generateURL("attack")
        QueryTask(recyclerView).execute(generatedURL)


        searchInput = view.findViewById(R.id.search_input)

        buttonSubmit.setOnClickListener {
            var generatedURL: URL = NetworkUtils.generateURL(searchInput.text.toString())
            QueryTask(recyclerView).execute(generatedURL)
        }

        return view
    }

    override fun onPause() {

        searchInput.text.clear()
        super.onPause()
    }

}