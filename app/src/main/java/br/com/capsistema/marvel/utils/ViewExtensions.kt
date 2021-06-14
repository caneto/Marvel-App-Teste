package br.com.capsistema.marvel.utils

import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import android.view.View

fun View.setPaletteColor(image: Bitmap) {
    Palette.from(image).generate { palette ->
        val bgColor = palette?.getDarkMutedColor(ContextCompat.getColor(context, android.R.color.black))
        bgColor?.let { setBackgroundColor(it) }
    }
}