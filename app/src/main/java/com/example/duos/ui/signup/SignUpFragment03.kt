package com.example.duos.ui.signup

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup02Binding
import com.example.duos.databinding.FragmentSignup03Binding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.signup.localSearch.LocationDialogFragment
import com.example.duos.utils.SignUpInfoViewModel

class SignUpFragment03() : Fragment() {
    lateinit var binding: FragmentSignup03Binding
    lateinit var signupNextBtnListener: SignUpNextBtnInterface
    lateinit var mContext: SignUpActivity
    lateinit var viewModel: SignUpInfoViewModel
    var savedState: Bundle? = null
    var locationText: TextView? = null


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
        binding = FragmentSignup03Binding.inflate(inflater, container, false)
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "03"
        signupNextBtnListener = mContext
        viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        locationText = binding.signup03LocationTextTv
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("savedState")
        }
        if (savedState != null) {
            Log.d("ㅎㅇ", "저장")
        } else {
            Log.d("ㅎㅇ", "저장X")
            viewModel.locationName.value = ""
            viewModel.locationDialogShowing.value = false
            signupNextBtnListener.onNextBtnUnable()
            binding.signup03LocationTextTv.setText(getString(R.string.signup_local_set))
        }
        savedState = null

        binding.signup03LinearLayoutLl.setOnClickListener {
            val dialog = LocationDialogFragment()
            activity?.supportFragmentManager?.let { fragmentManager ->
                dialog.show(
                    fragmentManager,
                    "지역 선택"
                )
            }
        }

        viewModel.locationDialogShowing.observe(viewLifecycleOwner, Observer {
            if (it) {
                binding.signup03LocationTextTv.text = viewModel.locationCateName.value + " " +
                        viewModel.locationName.value
            }
        })

        viewModel.locationName.observe(viewLifecycleOwner, Observer {
            if (it!!.isNotEmpty()) {
                signupNextBtnListener.onNextBtnEnable()
                viewModel.signUp03Avail.value = true
            } else {
                signupNextBtnListener.onNextBtnUnable()
                viewModel.signUp03Avail.value = false
            }
        })
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