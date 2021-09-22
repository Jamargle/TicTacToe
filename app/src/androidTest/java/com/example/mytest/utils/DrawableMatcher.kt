package com.example.mytest.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.test.espresso.matcher.BoundedMatcher
import com.example.mytest.R
import org.hamcrest.Description

fun withCompoundDrawable(resourceId: Int) =
    object : BoundedMatcher<View, TextView>(TextView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has image drawable resource $resourceId")
        }

        override fun matchesSafely(item: TextView): Boolean {
            if (resourceId < 0 || item.compoundDrawables.isEmpty()) {
                return false
            }
            item.compoundDrawables.filterNotNull().forEach {
                return it.isSameAs(resourceId, item.context)
            }
            return false
        }

        private fun Drawable.isSameAs(expectedDrawableRes: Int, context: Context): Boolean {
            val expectedDrawable =
                context.resources?.getDrawable(expectedDrawableRes, context.theme) ?: return false

            val testColor = context.getColor(R.color.white)
            val bitmap = getBitmap(this, testColor)
            val expectedBitmap = getBitmap(expectedDrawable, testColor)
            return bitmap?.sameAs(expectedBitmap) == true
        }

        private fun getBitmap(drawable: Drawable, testColor: Int): Bitmap? {
            drawable.setTint(testColor)
            val bitmap = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        }
    }
