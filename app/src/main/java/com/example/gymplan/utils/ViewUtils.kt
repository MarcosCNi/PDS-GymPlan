package com.example.gymplan.utils

import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
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
        .into(imageView)
}

fun View.show(){
    visibility = View.VISIBLE
}

fun View.hide(){
    visibility = View.INVISIBLE
}

fun View.gone(){
    visibility = View.GONE
}