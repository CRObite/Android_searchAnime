package com.example.finalanimenews


import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import pl.droidsonroids.gif.GifImageView


class LoadScreen : AppCompatActivity() {
    private lateinit var gif : GifImageView
    private val SPLASH_DISPLAY = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.load_screen)

        gif = findViewById(R.id.gif_image)
        Glide.with(this)
            .asGif()
            .load(R.drawable.cat)
            .transform(CircleCrop())
            .into(gif)

        Handler().postDelayed({
            val mainIntent = Intent(this, Login::class.java)
            this.startActivity(mainIntent)
            this.finish()
        }, SPLASH_DISPLAY.toLong())

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }
}