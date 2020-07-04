package com.dgdevelop.dynamiclinks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private var deepLink: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.i(TAG, "Before FirebaseDynamicLinks")
        FirebaseDynamicLinks.getInstance().getDynamicLink(intent).addOnSuccessListener(this) {pendingDynamicLinkData ->
            Log.i(TAG, "We have a dynamic link!")

            //Get deep link from result (may be null if no link is found)
            if (pendingDynamicLinkData != null) {
                Log.i(TAG, "Inside Here")
                deepLink = pendingDynamicLinkData.link //https://platzi.com/starwars
            }

            if (deepLink != null) {
                Log.i(TAG, "Here's the deep link URL: ${deepLink.toString()}")
                val strArr =  deepLink.toString().split(DEEP_LINK)
                val idPost = strArr[1]
                Log.i(TAG, "Id_post: $idPost")
                val intent = Intent(this, PostMovieActivity::class.java)

                if (idPost == STAR_WARS_KEY){
                    intent.putExtra(KEY_EXTRA_POST, STAR_WARS_KEY)
                }

                if (idPost == AMELIE_KEY){
                    intent.putExtra(KEY_EXTRA_POST, AMELIE_KEY)
                }
                startActivity(intent)

            }
        }
            .addOnFailureListener {e ->
                Log.e(TAG, "Oops, we couldn't retrieve dynamic link data.")
            }

        Log.i(TAG, "After FirebaseDynamicLinks")

        btnShareStarWars.setOnClickListener {
            shareDeepLink(createDynamicLink(STAR_WARS_SHORT_KEY))

        }
        btnShareAmelie.setOnClickListener {
            shareDeepLink(createDynamicLink(AMELIE_SHORT_KEY))
        }

    }

    private fun createDynamicLink(key: String): String{
        val dynamicLink  = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://platzi.com/")
            domainUriPrefix = "https://dgdevelop.page.link/$key"

            androidParameters("com.dgdevelop.dynamiclinks") {
                minimumVersion = 125
            }

        }
        val dynamicLinksUri = dynamicLink .uri
        Log.i(TAG, "Uri: $dynamicLinksUri")
        return dynamicLinksUri.toString()
    }

    private fun shareDeepLink(deepLink: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase DeepLink Share")
        intent.putExtra(Intent.EXTRA_TEXT, deepLink)

        startActivity(Intent.createChooser(intent, "Enviar a"))
    }


    companion object {
        private const val TAG = "MainInfo"
        private const val STAR_WARS_SHORT_KEY = "star"
        private const val AMELIE_SHORT_KEY = "ame"
        private const val STAR_WARS_KEY = "starwars"
        private const val AMELIE_KEY = "amelie"
        private const val DEEP_LINK = "https://platzi.com/"
        private const val KEY_EXTRA_POST = "key_post"
    }
}