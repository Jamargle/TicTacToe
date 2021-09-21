package com.example.mytest

import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers

internal fun ViewInteraction.isDisplayed() = check(matches(ViewMatchers.isDisplayed()))

internal fun ViewInteraction.withText(stringRes: Int) =
    check(matches(ViewMatchers.withText(stringRes)))

internal fun ViewInteraction.withText(text: String) = check(matches(ViewMatchers.withText(text)))
