package com.example.duos.ui.main.friendList


import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.Friend
import com.example.duos.databinding.FragmentLastRecommendFriendListBinding
import com.example.duos.ui.BaseFragment


class LastRecommendFriendListFragment ()  : BaseFragment<FragmentLastRecommendFriendListBinding>(
    FragmentLastRecommendFriendListBinding::inflate){

    private var todayFriendListDatas = ArrayList<Friend>()
    private var yesterdayFriendListDatas = ArrayList<Friend>()

    override fun initAfterBinding() {

        todayFriendListDatas.apply {
            add(Friend(R.drawable.friend_list_profile_01, "4evertennis", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_02, "uiii_88", 20, "남"))
            add(Friend(R.drawable.friend_list_profile_03, "qop123", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_04, "djeikd0620", 50, "남"))
            add(Friend(R.drawable.friend_list_profile_05, "4evertennis", 60, "여"))
            add(Friend(R.drawable.friend_list_profile_06, "eiwdk22", 70, "여"))
        }

        yesterdayFriendListDatas.apply{
            add(Friend(R.drawable.friend_list_profile_02, "uiii_88", 20, "남"))
            add(Friend(R.drawable.friend_list_profile_03, "qop123", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_07, "4evertennis", 10, "남"))
            add(Friend(R.drawable.friend_list_profile_04, "djeikd0620", 50, "남"))
            add(Friend(R.drawable.friend_list_profile_08, "oplew201", 30, "여"))
            add(Friend(R.drawable.friend_list_profile_01, "4evertennis", 30, "여"))

        }

        binding.lastRecommendFriendListTodayRecyclerviewRv.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.lastRecommendFriendListYesterdayRecyclerviewRv.layoutManager= LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)


        val todayFriendListRVAdapter = LastRecommendFriendListTodayRVAdapter(todayFriendListDatas)
        val yesterdayFriendListRVAdapter= LastRecommendFriendListYesterdayRVAdapter(yesterdayFriendListDatas)

        todayFriendListRVAdapter.setMyItemClickListener(object : LastRecommendFriendListTodayRVAdapter.MyItemClickListener{
            override fun onDeleteFriend(friendId: String) {
                // 추천친구 목록에서 삭제
            }

            override fun onAddFriend(friendId: String) {
                // 찜한친구 목록으로 추가
            }
        })

        yesterdayFriendListRVAdapter.setMyItemClickListener(object : LastRecommendFriendListYesterdayRVAdapter.MyItemClickListener{
            override fun onDeleteFriend(friendId: String) {
                // 찜한친구 목록에서 삭제
            }

            override fun onAddFriend(friendId: String) {
                // 찜한친구 목록으로 추가
            }
        })
        binding.lastRecommendFriendListTodayRecyclerviewRv.adapter = todayFriendListRVAdapter
        binding.lastRecommendFriendListYesterdayRecyclerviewRv.adapter = yesterdayFriendListRVAdapter

    }
}