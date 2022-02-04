package com.example.duos.ui.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup04Binding
import com.example.duos.ui.BaseFragment
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.duos.databinding.FragmentSignup03Binding
import com.example.duos.utils.SignUpInfoViewModel


class SignUpFragment04() : Fragment() {

    lateinit var binding: FragmentSignup04Binding
    lateinit var viewModel: SignUpInfoViewModel
    lateinit var signupNextBtnListener: SignUpNextBtnInterface
    lateinit var mContext: SignUpActivity
    var savedState: Bundle? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignUpActivity) {
            mContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignup04Binding.inflate(inflater, container, false)
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "04"
        signupNextBtnListener = mContext
        viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("savedState")
        }
        if (savedState != null) {
            Log.d("ㅎㅇ", "저장")
        } else {
            Log.d("ㅎㅇ", "저장X")
            viewModel.experience.value = null
            signupNextBtnListener.onNextBtnUnable()
        }
        savedState = null


        for (i in 1..14) {
            var btnId: Int = resources.getIdentifier(
                "signup_04_table_" + i.toString() + "_btn",
                "id",
                requireActivity().packageName
            )
            var btn: Button = requireView().findViewById(btnId)
            val num: String = i.toString()
            btn.text = resources.getString(
                resources.getIdentifier(
                    "signup_length_of_play_$num",
                    "string",
                    requireActivity().packageName
                )
            )
            btn.tag = i.toString()
        }

        viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)
        binding.viewmodel = viewModel

        viewModel.experience.observe(viewLifecycleOwner, Observer {
            if (it != null){
                signupNextBtnListener.onNextBtnEnable()
                viewModel.signUp04Avail.value = true
            } else {signupNextBtnListener.onNextBtnUnable()
                viewModel.signUp04Avail.value = false}
        })
    }

    fun setRadioButton(tag:Int) {
        viewModel.experience.value = tag
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("ondestroyview", "ㅎㅇ")
        savedState = saveState()
    }

    private fun saveState(): Bundle { /* called either from onDestroyView() or onSaveInstanceState() */
        val state = Bundle()
        state.putCharSequence("save", "true")

        return state
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("onsave", "ㅎㅇ")
        super.onSaveInstanceState(outState)
        outState.putBundle(
            "savedState",
            if (savedState != null) savedState else saveState()
        )
    }
}