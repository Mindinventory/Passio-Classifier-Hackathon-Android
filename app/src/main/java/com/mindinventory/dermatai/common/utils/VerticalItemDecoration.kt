package com.mindinventory.dermatai.common.utils

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalItemDecoration(context: Context, space: Int = 8) : RecyclerView.ItemDecoration() {

    private val spaceInDp = dpToPx(context, space)

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {

        outRect.left = 0
        outRect.right = 0
        outRect.bottom = spaceInDp
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
}
