package com.example.photopicker_v1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        photo_im.isEnabled = false

        /*
        This is the new method "registerForActivityResult" but used in a similar way to the old "startActivityForResult"
        so we need to create manually the implicit intent

        val startForResult =
            registerForActivityResult(
                ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    photo_im.setImageURI(result.data?.data)
                }
            )

        val intent = Intent().apply {
            action = Intent.ACTION_GET_CONTENT
            Intent.EXTRA_LOCAL_ONLY
            type = "image/jpeg"
        }

        photo_im.setImageURI(result.data?.data)
        */

        /* This method replaced the "startActivityForResult" method because with this you get the following advantages:
        - Code is more readable and compact. (You don't have to override the "onActivityResult" method
        - Is easier to request permissions
        - It's easier for programmers to understand what input they need to pass for the operation they're trying to perform
        Note: You need to call registerForActivityResult() before your fragment or activity is created and you cannot
        launch the ActivityResultLauncher until the fragment or activity’s Lifecycle has reached CREATED.*/
        val startForResult =
            registerForActivityResult(
                ActivityResultContracts.GetContent(),
                ActivityResultCallback {
                    photo_im.setImageURI(it)
                    if (photo_im.drawable != null) {
                        clickHere_tv.isEnabled = false
                        clickHere_tv.isVisible = false
                        photo_im.isEnabled = true
                    }else {
                        clickHere_tv.isEnabled = true
                        clickHere_tv.isVisible = true
                        photo_im.isEnabled = false
                    }
                }
            )

        clickHere_tv.setOnClickListener {
            startForResult.launch("image/*")
        }

        photo_im.setOnClickListener {
            startForResult.launch("image/*")
        }

    }
}

/*
This is the old method "startActivityForResult" that is now deprecated

private fun sendIntent(){
    val sendIntent = Intent().apply {
        action = Intent.ACTION_GET_CONTENT
        Intent.EXTRA_LOCAL_ONLY
        type = "image/jpeg"
    }

    try {
        startActivityForResult(sendIntent, 0)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(this, "There are not application where you can select a photo", Toast.LENGTH_SHORT).show()
    }
}

override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if(resultCode == Activity.RESULT_OK && requestCode == 0)
        photo_im.setImageURI(data?.data)
}
*/