package com.example.duos

import android.content.Context
import android.widget.RadioButton

import android.view.View

import android.widget.TableRow

import android.util.AttributeSet
import android.util.Log
import android.view.ViewGroup

import android.widget.TableLayout


class ToggleButtonGroupTableLayout : TableLayout, View.OnClickListener {
    private var activeRadioButton: RadioButton? = null
    val checkedRadioButtonId: Int
        get() = if (activeRadioButton != null) {
            activeRadioButton!!.id
        } else -1

    /**
     * @param context
     */
    constructor(context: Context?) : super(context) {        // TODO Auto-generated constructor stub
    }

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {        // TODO Auto-generated constructor stub
    }

    override fun onClick(v: View) {
        val rb = v as RadioButton
        if (activeRadioButton != null) {
            activeRadioButton!!.isChecked = false
        }
        rb.isChecked = true
        activeRadioButton = rb
        Log.d("asdf", checkedRadioButtonId.toString())
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, int, android.view.ViewGroup.LayoutParams)
     */
    override fun addView(
        child: View, index: Int,
        params: ViewGroup.LayoutParams
    ) {
        super.addView(child, index, params)
        setChildrenOnClickListener(child as TableRow)
    }

    /* (non-Javadoc)
     * @see android.widget.TableLayout#addView(android.view.View, android.view.ViewGroup.LayoutParams)
     */
    override fun addView(child: View, params: ViewGroup.LayoutParams) {
        super.addView(child, params)
        setChildrenOnClickListener(child as TableRow)
    }

    private fun setChildrenOnClickListener(tr: TableRow) {
        val c = tr.childCount
        for (i in 0 until c) {
            val v = tr.getChildAt(i)
            (v as? RadioButton)?.setOnClickListener(this)
        }
    }

    companion object {
        private const val TAG = "ToggleButtonGroupTableLayout"
    }
}