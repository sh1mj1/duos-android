package com.example.duos.ui.main.partnerSearch

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpacingItemDecoration(private val space: Int, private val marginTop: Int, private val marginBottom: Int): RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)

        val position = parent.getChildAdapterPosition(view)
        val count = state.itemCount

        if(position >= 2){  // 첫째줄 빼고 marginTop 20
            outRect.top = marginTop
            if(position >= count - 2){  //마지막줄은 marginBottom 16
                outRect.bottom = marginBottom
            }
        }

        // space 간격 똑같이 유지하면서 기기의 width에 따라 width 동적으로 조절
        val spanCount = 2
        val spacing = space
        val includeEdge = false

        val column: Int = position % spanCount // item column

        if (includeEdge) {
            outRect.left =
                spacing - column * spacing / spanCount // spacing - column * ((1f / spanCount) * spacing)
            outRect.right =
                (column + 1) * spacing / spanCount // (column + 1) * ((1f / spanCount) * spacing)
//            if (position < spanCount) { // top edge
//                outRect.top = spacing
//            }
//            outRect.bottom = spacing // item bottom
        } else {
            outRect.left = column * spacing / spanCount // column * ((1f / spanCount) * spacing)
            outRect.right =
                spacing - (column + 1) * spacing / spanCount // spacing - (column + 1) * ((1f /    spanCount) * spacing)
//            if (position >= spanCount) {
//                outRect.top = spacing // item top
//            }
        }
    }
}