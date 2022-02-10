package com.example.duos.ui.main.friendList

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.duos.data.entities.StarredFriend
import com.example.duos.data.remote.myFriendList.FriendListService
import com.example.duos.databinding.FragmentStarredFriendListBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity
import com.example.duos.utils.FriendListCountViewModel
import com.example.duos.utils.getUserIdx

class StarredFriendListFragment() : BaseFragment<FragmentStarredFriendListBinding>
    (FragmentStarredFriendListBinding::inflate), StarredFriendListView, DeleteStarredFriendView {

    private var friendListDatas = ArrayList<StarredFriend>()
    lateinit var mContext: MainActivity
    lateinit var sharedViewModel: FriendListCountViewModel


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }


    override fun initAfterBinding() {
        binding.starredFriendListRecyclerviewRc.itemAnimator = null
        binding.starredFriendListRecyclerviewRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.starredFriendListRecyclerviewRc.adapter = StarredFriendListRVAdapter(ArrayList<StarredFriend>())
        FriendListService.getStarredFriendList(this, getUserIdx()!!)
    }

    override fun onGetStarredFriendListSuccess(starredFriendList: List<StarredFriend>) {
        Log.d("찜한친구","성공")
        friendListDatas.addAll(starredFriendList)

        val starredFriendListRVAdapter = StarredFriendListRVAdapter(friendListDatas)

        starredFriendListRVAdapter.setMyItemClickListener(object : StarredFriendListRVAdapter.MyItemClickListener {
            override fun onDeleteFriend(friendIdx : Int) {
                // 찜한친구 목록에서 삭제
                onDeleteFriendApi(friendIdx)
            }

            override fun onGetFriendCount() {
                // 뷰 모델 프로바이더를 통해 뷰모델 가져오기
                sharedViewModel = ViewModelProvider(requireActivity()).get(FriendListCountViewModel::class.java)
                // 뷰모델이 가지고 있는 값의 변경사항을 관찰할 수 있는 라이브 데이터를 관찰한다
                sharedViewModel.updateValue(starredFriendListRVAdapter.itemCount)
            }
        })
        binding.starredFriendListRecyclerviewRc.adapter = starredFriendListRVAdapter
    }

    fun onDeleteFriendApi(friendIdx : Int){
        FriendListService.deleteStarredFriend(this, getUserIdx()!!, friendIdx)
    }

    override fun onGetStarredFriendListFailure(code: Int, message: String) {
        showToast("code : $code, message : $message")
    }

    override fun onDeleteStarredFriendSuccess() {
        Log.d("찜한친구삭제","성공")
    }

    override fun onDeleteStarredFriendFailure(code: Int, message: String) {
        showToast("code : $code, message : $message")
    }
}