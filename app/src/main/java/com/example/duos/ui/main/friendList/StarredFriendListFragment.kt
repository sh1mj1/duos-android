package com.example.duos.ui.main.friendList

import androidx.recyclerview.widget.LinearLayoutManager

import com.example.duos.data.entities.StarredFriend
import com.example.duos.data.remote.myFriendList.FriendListService
import com.example.duos.databinding.FragmentMyFriendListBinding
import com.example.duos.ui.BaseFragment

class StarredFriendListFragment() : BaseFragment<FragmentMyFriendListBinding>(FragmentMyFriendListBinding::inflate), StarredFriendListView{

    private var friendListDatas = ArrayList<StarredFriend>()

    override fun initAfterBinding() {

        FriendListService.myFriendList(this, 1)

    }

    override fun onGetMyFriendListSuccess(myFriendList: List<StarredFriend>) {

        friendListDatas.addAll(myFriendList)
        binding.myFriendListRecyclerviewRc.itemAnimator = null
        binding.myFriendListRecyclerviewRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val myFriendListRVAdapter = StarredFriendListRVAdapter(friendListDatas)

        myFriendListRVAdapter.setMyItemClickListener(object : StarredFriendListRVAdapter.MyItemClickListener{
            override fun onDeleteFriend(friendId: String) {
                // 찜한친구 목록에서 삭제
            }
        })
        binding.myFriendListRecyclerviewRc.adapter = myFriendListRVAdapter
    }

    override fun onGetMyFriendListFailure(code: Int, message: String) {
        showToast("code : $code, message : $message")
    }
}