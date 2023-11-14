package com.example.moviesflickrapp.base.view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.core.view.doOnAttach
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.findViewTreeViewModelStoreOwner

abstract class BaseCustomView(context: Context, attrs: AttributeSet? = null) :
    FrameLayout(context, attrs) {

    val parentViewLifecycleOwner: LifecycleOwner
        get() = findViewTreeLifecycleOwner()!!

    val parentViewModelStoreOwner: ViewModelStoreOwner
        get() = findViewTreeViewModelStoreOwner()!!


    init {
        if (!isInEditMode) {
            doOnAttach {
                onViewAttach()
            }
        }
    }

    /**
     * Called after the view is attached to the screen.
     *
     * Write your subviews initializations / observers / viewModels access here.
     */
    abstract fun onViewAttach()
}