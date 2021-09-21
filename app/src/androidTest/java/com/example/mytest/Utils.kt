package com.example.mytest

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher

internal fun ViewInteraction.isDisplayed() = check(matches(ViewMatchers.isDisplayed()))

internal fun ViewInteraction.withText(stringRes: Int) =
    check(matches(ViewMatchers.withText(stringRes)))

internal fun ViewInteraction.withImage(drawable: Int) =
    check(matches(withImageDrawable(drawable)))

fun withImageDrawable(resourceId: Int): Matcher<View> {
    return object : BoundedMatcher<View, ImageView>(ImageView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has image drawable resource $resourceId")
        }

        override fun matchesSafely(item: ImageView): Boolean {
            if (resourceId < 0) {
                return item.drawable == null
            }
            val expectedDrawable = item.context.let { context ->
                context?.resources?.getDrawable(resourceId, context.theme) ?: return false
            }

            val testColor = item.context.getColor(R.color.white)
            val bitmap = getBitmap(item.drawable, testColor)
            val otherBitmap = getBitmap(expectedDrawable, testColor)
            return bitmap?.sameAs(otherBitmap) == true
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
}
