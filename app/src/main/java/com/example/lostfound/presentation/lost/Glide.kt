package com.example.lostfound.presentation.lost

import android.widget.ImageView
import com.bumptech.glide.RequestManager
import com.example.lostfound.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Glide @Inject constructor(
    private val glide: RequestManager
) {

    fun loadImage(imageUrl: String, image: ImageView){
        glide.load(imageUrl)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(image)
    }
}