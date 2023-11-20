package com.example.moviesflickrapp.util.espressoutils

import android.content.res.Resources
import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

open class RecyclerViewMatcher {

    fun atPosition(position: Int, @IdRes recyclerViewId: Int): Matcher<View> {
        return atPositionOnView(position, -1, recyclerViewId)
    }

    fun atPositionOnView(
        position: Int,
        targetViewId: Int,
        @IdRes recyclerViewId: Int
    ): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var resources: Resources? = null
            var childView: View? = null
            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (this.resources != null) {
                    idDescription = try {
                        this.resources!!.getResourceName(recyclerViewId)
                    } catch (var4: Resources.NotFoundException) {
                        String.format(
                            "%s (resource name not found)",
                            Integer.valueOf(recyclerViewId)
                        )
                    }
                }
                description.appendText("with id: $idDescription")
            }

            override fun matchesSafely(view: View): Boolean {
                this.resources = view.resources
                if (childView == null) {
                    val recyclerView = view.rootView.findViewById(recyclerViewId) as RecyclerView
                    if (recyclerView.id == recyclerViewId) {
                        childView =
                            recyclerView.findViewHolderForAdapterPosition(position)?.itemView
                    } else {
                        return false
                    }
                }
                return if (targetViewId == -1) {
                    view === childView
                } else {
                    val targetView = childView?.findViewById(targetViewId) as View
                    view === targetView
                }
            }
        }
    }

    fun scrollToItem(position: Int, recyclerId: Int) {
        Espresso.onView(ViewMatchers.withId(recyclerId)).perform(
            RecyclerViewActions
                .scrollToPosition<RecyclerView.ViewHolder>(position)
        )
    }
}
