package com.example.duos.ui.main.friendList

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.data.entities.StarredFriend
import com.example.duos.data.remote.myFriendList.FriendListService
import com.example.duos.databinding.FragmentStarredFriendListBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getUserIdx

class StarredFriendListFragment() : BaseFragment<FragmentStarredFriendListBinding>
    (FragmentStarredFriendListBinding::inflate), StarredFriendListView, DeleteStarredFriendView {

    private var friendListDatas = ArrayList<StarredFriend>()
    lateinit var mContext: MainActivity
    lateinit var viewModel: ViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onResume() {
        getStarredFriend()
        super.onResume()
    }

    override fun initAfterBinding() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        binding.starredFriendListRecyclerviewRc.setHasFixedSize(true)
        binding.starredFriendListRecyclerviewRc.itemAnimator = null
        binding.starredFriendListRecyclerviewRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.starredFriendListRecyclerviewRc.adapter = StarredFriendListRVAdapter(ArrayList<StarredFriend>())

    }

    fun getStarredFriend(){
        progressON()
        FriendListService.getStarredFriendList(this, getUserIdx()!!)
    }


    override fun onGetStarredFriendListSuccess(starredFriendList: List<StarredFriend>) {
        progressOFF()
        Log.d("찜한친구","성공")
        friendListDatas.clear()
        friendListDatas.addAll(starredFriendList)

        val starredFriendListRVAdapter = StarredFriendListRVAdapter(friendListDatas)

        viewModel.friendCount.value = starredFriendListRVAdapter.itemCount

        starredFriendListRVAdapter.setMyItemClickListener(object : StarredFriendListRVAdapter.MyItemClickListener {
            override fun onDeleteFriend(friendIdx : Int) {
                // 찜한친구 목록에서 삭제
                onDeleteFriendApi(friendIdx)
            }

            override fun onGetFriendCount() {
                // 뷰 모델 프로바이더를 통해 뷰모델 가져오기
                viewModel.friendCount.value = starredFriendListRVAdapter.itemCount
            }

            override fun gotoPartnerProfileActivity(partnerIdx : Int) {
                // 파트너 세부 화면으로 이동
                Log.d("그리드","itemClick")
                val intent = Intent(activity, MyProfileActivity::class.java)
                intent.apply {
                    this.putExtra("isFromSearch", true)
                    this.putExtra("partnerUserIdx", partnerIdx)
                }
                startActivity(intent)
            }
        })
        binding.starredFriendListRecyclerviewRc.adapter = starredFriendListRVAdapter
    }

    fun onDeleteFriendApi(friendIdx : Int){
        Log.d("friendIdx 삭제",friendIdx.toString())
        FriendListService.deleteStarredFriend(this, getUserIdx()!!, friendIdx)
    }

    override fun onGetStarredFriendListFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //showToast("code : $code, message : $message")
    }

    override fun onDeleteStarredFriendSuccess() {
        Log.d("찜한친구삭제","성공")
    }

    override fun onDeleteStarredFriendFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //showToast("code : $code, message : $message")
    }
}