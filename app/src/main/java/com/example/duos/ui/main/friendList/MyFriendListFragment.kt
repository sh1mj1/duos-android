package com.example.duos.ui.main.friendList

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.Friend
import com.example.duos.databinding.FragmentMyFriendListBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity

class MyFriendListFragment() : BaseFragment<FragmentMyFriendListBinding>(FragmentMyFriendListBinding::inflate){

    private var friendListDatas = ArrayList<Friend>()

    override fun initAfterBinding() {

        friendListDatas.apply {
            add(Friend(R.drawable.friend_list_profile_01, "4evertennis", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_02, "uiii_88", 20, "남"))
            add(Friend(R.drawable.friend_list_profile_03, "qop123", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_04, "djeikd0620", 50, "남"))
            add(Friend(R.drawable.friend_list_profile_05, "4evertennis", 60, "여"))
            add(Friend(R.drawable.friend_list_profile_06, "eiwdk22", 70, "여"))
            add(Friend(R.drawable.friend_list_profile_07, "4evertennis", 10, "남"))
            add(Friend(R.drawable.friend_list_profile_08, "oplew201", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_01, "4evertennis", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_02, "uiii_88", 20, "남"))
            add(Friend(R.drawable.friend_list_profile_03, "qop123", 30, "여"))
        }

        binding.myFriendListRecyclerviewRc.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val myFriendListRVAdapter = MyFriendListRVAdapter(friendListDatas)

        myFriendListRVAdapter.setMyItemClickListener(object : MyFriendListRVAdapter.MyItemClickListener{
            override fun onDeleteFriend(friendId: String) {
                // 찜한친구 목록에서 삭제
            }
        })
        binding.myFriendListRecyclerviewRc.adapter = myFriendListRVAdapter

    }
}