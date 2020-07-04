package com.dgdevelop.dynamiclinks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_post_movie.*

class PostMovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_movie)

        if (intent.extras != null){
            tvTitlePost.text = intent.extras!!.getString(KEY_EXTRA_POST)
        }
    }

    companion object{
        private const val KEY_EXTRA_POST = "key_post"
    }
}