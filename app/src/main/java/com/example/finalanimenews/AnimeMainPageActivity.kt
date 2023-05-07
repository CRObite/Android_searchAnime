package com.example.finalanimenews

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.finalanimenews.util.NetworkUtils
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL


private lateinit var loading: ProgressBar
private lateinit var errorText: TextView
private lateinit var noResultText: TextView
private lateinit var animeScore: TextView
private lateinit var animeDate: TextView
private lateinit var animeStatus: TextView
private lateinit var animeEpisodes: TextView
private lateinit var animeDescription: TextView

class AnimeMainPageActivity : AppCompatActivity() {



    private lateinit var backButton: LinearLayout
    private lateinit var animeImage: ImageView
    private lateinit var animeTitle: TextView
    private lateinit var animeType: TextView

    private lateinit var goWebButton: LinearLayout
    class QueryTask(private var animeScore: TextView,
                    private var animeDate: TextView,
                    private var animeStatus: TextView,
                    private var animeEpisodes: TextView,
                    private var animeDescription: TextView):  AsyncTask<URL, Void, String>() {
        private var listOfAnime: MutableList<Anime> = mutableListOf()


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


                    animeScore.text = "Type: ${jsonResponse.getString("type")}"
                    animeDate.text = "Genres: ${jsonResponse.getJSONArray("genres").get(0)},${jsonResponse.getJSONArray("genres").get(1)},${jsonResponse.getJSONArray("genres").get(2)}"
                    animeStatus.text = "Status:  ${jsonResponse.getString("status")}"
                    animeEpisodes.text = "Total Episodes:  ${jsonResponse.getString("totalEpisodes")}"
                    animeDescription.text = "Description: ${jsonResponse.getString("description")}"

                } catch (e: JSONException){

                    noResultText.visibility = View.VISIBLE
                }

            } else if(result == null || result == ""){
                errorText.visibility = View.VISIBLE

            }

            loading.visibility = View.INVISIBLE

        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.anime_main_page)



        backButton = findViewById(R.id.back_btn)

        animeImage = findViewById(R.id.anime_image)
        animeTitle = findViewById(R.id.title_anime)
        animeEpisodes = findViewById(R.id.anime_episodes)
        animeDate = findViewById(R.id.anime_date)
        animeType = findViewById(R.id.anime_type)
        animeScore = findViewById(R.id.anime_score)
        animeDescription = findViewById(R.id.anime_description)
        animeStatus = findViewById(R.id.anime_status)
        goWebButton = findViewById(R.id.go_web_btn)

        loading = findViewById(R.id.loading)
        errorText = findViewById(R.id.errorText)
        noResultText = findViewById(R.id.noResoultText)




        val arguments = intent.extras
        if (arguments!= null){
            Picasso.get().load(arguments.getString("image")).into(animeImage)

            animeTitle.text = arguments.getString("title")
            animeType.text = "Release Date: ${arguments.getString("releaseDate")}"
            var generatedURL: URL = NetworkUtils.generateURL("info/${arguments.getString("id")}")
            QueryTask(animeScore, animeDate, animeStatus, animeEpisodes, animeDescription).execute(generatedURL)
        }


        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            this.startActivity(intent)
        }
        goWebButton.setOnClickListener {
            if (arguments!= null){
                goToURL(arguments?.getString("url"))
            }else{
                Toast.makeText(this,"There were some problems. Try again later",Toast.LENGTH_LONG).show()
            }

        }

    }



    fun goToURL(url: String?) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}