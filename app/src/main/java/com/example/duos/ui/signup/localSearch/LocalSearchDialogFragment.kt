package com.example.duos.ui.signup.localSearch

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.duos.databinding.SignupLocalDialogBinding

class LocalSearchDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true

//        AuthService.getLargeLocal(this)

    }

    private lateinit var binding: SignupLocalDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupLocalDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.test01.setOnClickListener {
            this.dismiss()
        }
        binding.test02.setOnClickListener {
            this.dismiss()
        }

    }

//    /* ----------
//    시/도를 roomDB 에 저장한다.
//     */
//    override fun LargeLocalToRoomDB(largeLocalList : List<LocalCategory>) {
//        val localDB = UserDatabase.getInstance(requireContext())!!
//        localDB.largeLocalDao().clearAll()
//        localDB.largeLocalDao().insertAll(largeLocalList)
//    }
//
//
//    /* ----------
//    시/도를 recyclerview 에 설정해준다.
//     */
//    override fun LargeLocalSearchOnView() {
//        val largeLocalList : List<LocalCategory>
//        val localDB = UserDatabase.getInstance(requireContext())!!
//        largeLocalList = localDB.largeLocalDao().getLargeRegs()
//
//        binding.signupLargeLocalListRc.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

//    }

    override fun onResume() {
        super.onResume()
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.9).toInt()
        params?.height = (deviceHeight * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }
}
