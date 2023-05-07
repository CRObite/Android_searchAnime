package com.example.finalanimenews.util


import android.net.Uri
import java.io.IOException
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL
import java.net.UnknownHostException
import java.util.*

class NetworkUtils {
    companion object{
        private const val API_BASE_URL:String = "https://api.consumet.org"
        private const val TYPE:String = "/anime"
        private const val RESOURCE:String = "/gogoanime"

        public fun generateURL(text: String): URL{
            val ANIME_TITLE:String = "/${text}"
            var builtUri: Uri? = Uri.parse(API_BASE_URL + TYPE+ RESOURCE+ANIME_TITLE)
                .buildUpon().build()

            var url: URL? = null
            url = URL(builtUri.toString())



            return url
        }

        @Throws(IOException::class)
        fun getResponseFromURL(url: URL): String? {
            val urlConnection = url.openConnection() as HttpURLConnection
            return try {
                val input : InputStream = urlConnection.inputStream
                val scanner = Scanner(input)
                scanner.useDelimiter("\\A")
                val hasInput = scanner.hasNext()
                if (hasInput) {
                    scanner.next()
                } else {
                    null
                }
            } catch (e: UnknownHostException){
                null
            } finally {
                urlConnection.disconnect()
            }
        }
    }

}