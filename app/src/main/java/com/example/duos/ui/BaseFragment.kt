package com.example.duos.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.example.duos.ApplicationClass
import com.example.duos.R
import com.example.duos.utils.Inflate
import java.lang.Math.round


abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>
) : Fragment() {
    private var _binding: VB? = null
    protected val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate.invoke(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initAfterBinding()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    protected abstract fun initAfterBinding()

    fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }


    fun progressON() {
        ApplicationClass.getInstance().progressON(activity, null)
    }

    fun progressON(message: String) {
        ApplicationClass.getInstance().progressON(activity, message)
    }

    fun progressOFF() {
        ApplicationClass.getInstance().progressOFF()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false

            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            return connectivityManager.activeNetworkInfo?.isConnected ?: false
        }
    }

    fun toMyPagePhoneNumber(phoneNumber: String): String {
        return phoneNumber.substring(0, 3) + " " + phoneNumber.substring(3, 7) + "  ****"
    }

    fun toLocationStr(locationIdx: Int): String? {
        val array = resources.getStringArray(R.array.location_full_name)
        return array[locationIdx]
    }

    fun toGenderStr(genderIdx: Int): String {
        return when (genderIdx) {
            1 -> "남"
            else -> "여"
        }
    }

    fun toCareerStr(myPageCareerIdx: Int?): String {
        val myPageCareerStr: String
        when (myPageCareerIdx) {
            1 -> myPageCareerStr = "1개월 미만"
            2 -> myPageCareerStr = "3개월 미만"
            3 -> myPageCareerStr = "6개월 미만"
            4 -> myPageCareerStr = "1년 미만"
            5 -> myPageCareerStr = "2년 미만"
            6 -> myPageCareerStr = "3년 미만"
            7 -> myPageCareerStr = "4년 미만"
            8 -> myPageCareerStr = "5년 미만"
            9 -> myPageCareerStr = "6년 미만"
            10 -> myPageCareerStr = "7년 미만"
            11 -> myPageCareerStr = "8년 미만"
            12 -> myPageCareerStr = "9년 미만"
            13 -> myPageCareerStr = "10년 미만"
            else -> myPageCareerStr = "11년 이상"
        }
        return myPageCareerStr
    }

    fun toRatingStr(ratingFloat : Float): String{
        val ratingStr = String.format("%.0f", ratingFloat*10).toDouble()/10
        return ratingStr.toString()
    }

    fun strToInt(str: String):Int{
        val resultInt : Int = str.replace("[^0-9]".toRegex(), "").toInt()
        return resultInt
    }


}