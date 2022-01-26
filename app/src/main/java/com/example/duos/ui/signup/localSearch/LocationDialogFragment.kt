package com.example.duos.ui.signup.localSearch

import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.data.entities.LocationCategoryList
import com.example.duos.data.entities.LocationList
import com.example.duos.data.entities.StarredFriend
import com.example.duos.data.remote.localList.LocationService
import com.example.duos.databinding.SignupLocalDialogBinding
import com.example.duos.ui.main.chat.ChatListRVAdapter
import com.example.duos.ui.main.friendList.StarredFriendListRVAdapter
import com.example.duos.utils.SignUpInfoViewModel

class LocationDialogFragment() : DialogFragment(), LocationView {

    lateinit var locationList : List<LocationList>
    lateinit var myLocationCategory: LocationCategoryList
    lateinit var myLocation: LocationList
    private lateinit var binding: SignupLocalDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SignupLocalDialogBinding.inflate(inflater, container, false)

        binding.locationCategoryListRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.locationCategoryListRc.adapter = LocationCategoryRVAdapter(ArrayList<LocationCategoryList>())

        binding.locationListRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.locationListRc.adapter = LocationRVAdapter(ArrayList<LocationList>())

        LocationService.getLocationList(this)

        //화면터치시 꺼짐
        setCancelable(true);

        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.locationCloseBtn.setOnClickListener {
            this.dismiss()
        }
        binding.locationOkBtn.setOnClickListener {
            this.dismiss()
            val viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)
            viewModel.locationCate.value = myLocationCategory.locationCategoryIdx
            viewModel.locationCateName.value = myLocationCategory.locationCategoryName
            viewModel.location.value = myLocation.locationIdx
            viewModel.locationName.value = myLocation.locationName
            viewModel.locationDialogShowing.value = true
        }
    }

    override fun onLocationCategoryListOnView(locationCategoryList: List<LocationCategoryList>) {
        val locationCategoryRVAdapter = LocationCategoryRVAdapter(locationCategoryList)
        binding.locationCategoryListRc.adapter = locationCategoryRVAdapter
        myLocationCategory = locationCategoryList[0]

        locationCategoryRVAdapter.setMyItemClickListener(object : LocationCategoryRVAdapter.MyItemClickListener{

            override fun onSetLocationList(locationCategory: LocationCategoryList) {
                myLocationCategory = locationCategory
                locationCategory.locationCategoryIdx?.let { setLocalList(it) }
            }

        })

    }

    override fun onLocationListOnView(locatonList: List<LocationList>) {
        locationList = locatonList
        myLocation = locatonList[0]

        // 처음에는 서울 시/구/군을 보여준다.
        setLocalList(1)
    }

    override fun onLocationFailure(code: Int, message: String) {
        Toast.makeText(requireContext(), "code : $code , message : $message", Toast.LENGTH_SHORT)
    }

    fun setLocalList(cateIdx : Int){

        // category 에 해당하는 시/구/군을 보여준다.
        val filteredLocationList = ArrayList<LocationList>()
        for (location in locationList){
            if(location.locationCategoryIdx == cateIdx)
                filteredLocationList.add(location);
        }

        val locationRVAdapter = LocationRVAdapter(filteredLocationList)
        binding.locationListRc.adapter = locationRVAdapter

        locationRVAdapter.setMyItemClickListener(object : LocationRVAdapter.MyItemClickListener{
            override fun onChooseLocation(location: LocationList) {
                myLocation = location
            }
        })
    }

    fun filter(){

    }


    override fun onResume() {
        super.onResume()
        val windowManager =
            requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
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
