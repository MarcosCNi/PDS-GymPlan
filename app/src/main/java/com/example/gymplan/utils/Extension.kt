package com.example.gymplan.utils

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.gymplan.R
import timber.log.Timber

fun Fragment.toast(message: String, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(
        requireContext(),
        message,
        duration,
    ).show()
}

fun loadImg(
    imageView: ImageView,
    path: String
) {
    Timber.tag("LoadImg").e(path)
    Glide.with(imageView.context)
        .asGif()
        .load(path)
        .listener(object : RequestListener<GifDrawable> {
            override fun onResourceReady(
                resource: GifDrawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<GifDrawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                imageView.show()
                return false
            }

            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<GifDrawable>?,
                isFirstResource: Boolean
            ): Boolean {
                imageView.gone()
                return false
            }
        })
        .into(imageView)
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.gone(){
    visibility = View.GONE
}

fun String.limitCharacters(characters: Int): String {
    if(this.length > characters){
        val firstCharacter = 0
        return "${this.substring(firstCharacter, characters)}..."
    }
    return this
}