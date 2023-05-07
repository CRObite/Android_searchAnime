package com.example.finalanimenews.Fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.finalanimenews.MainActivity
import com.example.finalanimenews.R


class InfoFragment : Fragment() {

    private lateinit var lightDarkModeBtn: ImageView
    private lateinit var linkButton: ImageView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    var nightMode: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_info, container, false)

        linkButton = view.findViewById(R.id.link_btn)
        lightDarkModeBtn = view.findViewById(R.id.light_dark_mode_btn)

        linkButton.setOnClickListener {
            goToURL("https://instagram.com/rahat_akhmetov?igshid=ZDdkNTZiNTM=")
        }

        sharedPreferences = view.context.getSharedPreferences("MODE",Context.MODE_PRIVATE)
        nightMode = sharedPreferences.getBoolean("night",false)



        if(nightMode){
            lightDarkModeBtn.setImageResource(R.drawable.moon_icon)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }


        lightDarkModeBtn.setOnClickListener {
            (activity as MainActivity?)?.onModeChanged()

            if(nightMode){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor = sharedPreferences.edit()
                editor.putBoolean("night",false)
            } else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor = sharedPreferences.edit()
                editor.putBoolean("night",true)
            }

            editor.apply()
        }




        return view
    }
    fun goToURL(url: String?) {
        val uri: Uri = Uri.parse(url)
        val intent = Intent(Intent.ACTION_VIEW, uri)
        startActivity(intent)
    }

}