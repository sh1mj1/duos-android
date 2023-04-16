package com.example.duos

import android.content.Context
import android.widget.RadioButton
import android.view.View
import android.widget.TableRow
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.TableLayout
import android.os.Bundle
import android.os.Parcelable
import com.example.duos.ui.main.mypage.myprofile.editprofile.EditProfileActivity
import com.example.duos.ui.main.partnerSearch.PartnerFilterActivity
import com.example.duos.ui.signup.SignUpActivity

class ToggleButtonGroupTableLayout : TableLayout, View.OnClickListener {

    lateinit var radioBtnListener: ToggleButtonInterface
    lateinit var signupContext: Context
    lateinit var partnerFilterContext: Context
    lateinit var editProfileContext: Context

    var checkedRadioButtonId: Int =
        if (activeRadioButton != null) {
            activeRadioButton!!.id
        } else -1

    /**
     * @param context
     */
    constructor(context: Context?) : super(context) {
        if (context is SignUpActivity) {
            signupContext = context
        }
        else if (context is PartnerFilterActivity){
            partnerFilterContext = context
        }
        else if (context is EditProfileActivity){
            editProfileContext = context
        }
    }

    /**
     * @param context
     * @param attrs
     */
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        if (context is SignUpActivity) {
            signupContext = context
        }
        else if (context is PartnerFilterActivity){
            partnerFilterContext = context
        }
        else if (context is EditProfileActivity){
            editProfileContext = context
        }

    }

    override fun onClick(v: View) {
        val rb = v as RadioButton

        if (v.context is PartnerFilterActivity){
            radioBtnListener = partnerFilterContext as PartnerFilterActivity
            (radioBtnListener as PartnerFilterActivity).setRadiobutton("init")
        }

        if (v.context is EditProfileActivity){
            radioBtnListener = editProfileContext as EditProfileActivity
            (radioBtnListener as EditProfileActivity).setRadiobutton("0")
        }

        if (activeRadioButton != null) {
            activeRadioButton!!.isChecked = false
        }
        rb.isChecked = true
        activeRadioButton = rb
        checkedRadioButtonId = activeRadioButton!!.id


        if (v.context is SignUpActivity){
            radioBtnListener = signupContext as SignUpActivity
            (radioBtnListener as SignUpActivity).setRadiobutton(activeRadioButton!!.tag.toString())
        }
        else if (v.context is PartnerFilterActivity){
            radioBtnListener = partnerFilterContext as PartnerFilterActivity
            (radioBtnListener as PartnerFilterActivity).setRadiobutton(activeRadioButton!!.tag.toString())
        } else if(v.context is EditProfileActivity){
            radioBtnListener = editProfileContext as EditProfileActivity
            (radioBtnListener as EditProfileActivity).setRadiobutton(activeRadioButton!!.tag.toString())

        }

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

    override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("superState", super.onSaveInstanceState())
        activeRadioButton?.let { bundle.putInt("stuff", it.id) } // ... save stuff
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) // implicit null check
        {
            val bundle = state
            this.checkedRadioButtonId = bundle.getInt("stuff") // ... load stuff
            activeRadioButton = findViewById(checkedRadioButtonId)
            state = bundle.getParcelable("superState")
        }
        super.onRestoreInstanceState(state)
    }

    companion object {
        private const val TAG = "ToggleButtonGroupTableLayout"
        var activeRadioButton: RadioButton? = null
    }
}