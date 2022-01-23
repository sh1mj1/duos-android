package com.example.duos.ui.main.chat

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

enum class ButtonsState{ GONE, RIGHT_VISIBLE }
private const val buttonWidth: Float = 115.0f

class ChatListItemTouchHelperCallback(listener: ChatListItemTouchHelperListener): ItemTouchHelper.Callback() {
    private var listener = listener
    private var swipeBack = false
    private var buttonsShowedState: ButtonsState = ButtonsState.GONE
    private lateinit var buttonInstance: RectF
    private lateinit var currentItemViewHolder: RecyclerView.ViewHolder

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val swipe_flags = ItemTouchHelper.START
        return makeMovementFlags(0, swipe_flags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        listener.onItemSwipe(viewHolder.adapterPosition)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        //아이템이 스와이프 됐을경우 버튼을 그려주기 위해서 스와이프가 됐는지 확인
        if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            if(buttonsShowedState != ButtonsState.GONE){
                var dx = 0.0f
                if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE) dx = Math.min(dX, -buttonWidth);
                super.onChildDraw(c, recyclerView, viewHolder, dx, dY, actionState, isCurrentlyActive);
            }else{
                setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }

            if(buttonsShowedState == ButtonsState.GONE){
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }
        currentItemViewHolder = viewHolder;

        //버튼을 그려주는 함수
        drawButtons(c, currentItemViewHolder);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun drawButtons(c: Canvas, viewHolder: RecyclerView.ViewHolder) {
        var buttonWidthWithOutPadding = buttonWidth - 10.0f
        //var buttonWidthWithOutPadding = buttonWidth
        var corners = 0.0f
        var itemView = viewHolder.itemView
        var p = Paint()

        //왼쪽으로 스와이프 했을때 (오른쪽에 버튼이 보여지게 될 경우)
        if(buttonsShowedState == ButtonsState.RIGHT_VISIBLE){
            var rightButton = RectF(
                itemView.getRight() - buttonWidthWithOutPadding,
                itemView.top + 10.0f,
                itemView.right - 10.0f,
                itemView.bottom - 10.0f
            )
            p.color = Color.RED
            c.drawRoundRect(rightButton, corners, corners, p)
            drawText("삭제", c, rightButton, p)
            buttonInstance = rightButton
        }
    }

    //버튼의 텍스트 그려주기
   private fun drawText(text: String, c:Canvas, button:RectF, p:Paint){
        var textSize = 25.0f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize

        var textWidth = p.measureText(text)
        c.drawText(text, button.centerX() - (textWidth/2), button.centerY() + (textSize/2), p)
    }

    override fun convertToAbsoluteDirection(flags: Int, layoutDirection: Int): Int {
        if(swipeBack){
            swipeBack = false
            return 0
        }
        return super.convertToAbsoluteDirection(flags, layoutDirection)
    }

    private fun setTouchListener(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX:Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean){
        recyclerView.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                if (event != null) {
                    swipeBack = event.action == MotionEvent.ACTION_CANCEL || event.action == MotionEvent.ACTION_UP
                    //swipeBack = event.action == MotionEvent.ACTION_CANCEL
                }
                if(swipeBack){
                    if(dX < -buttonWidth) buttonsShowedState = ButtonsState.RIGHT_VISIBLE
                    if(buttonsShowedState != ButtonsState.GONE){
                        setTouchListener(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                        Log.d("clickable 실행 전", recyclerView.toString())
                        setItemsClickable(recyclerView, false)
                    }
                }
                return false
            }
        })
    }

    private fun setItemsClickable(recyclerView: RecyclerView, isClickable: Boolean){
        Log.d("isClickable 확인", isClickable.toString())
        for(i: Int in 0..recyclerView.childCount-1){
            Log.d("getChildAt 확인", recyclerView.getChildAt(i).toString())
            recyclerView.getChildAt(i).isClickable = isClickable
        }
    }
}