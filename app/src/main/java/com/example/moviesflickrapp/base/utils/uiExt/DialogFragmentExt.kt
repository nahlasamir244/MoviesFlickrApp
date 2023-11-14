package com.example.moviesflickrapp.base.utils.uiExt

import android.app.Dialog
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

fun DialogFragment.setLayout(
    width: Int? = ViewGroup.LayoutParams.MATCH_PARENT,
    height: Int? = ViewGroup.LayoutParams.MATCH_PARENT
) {
    val dialog: Dialog? = dialog
    if (dialog != null && width != null && height != null) {
        dialog.window?.setLayout(width, height)
    }
}
