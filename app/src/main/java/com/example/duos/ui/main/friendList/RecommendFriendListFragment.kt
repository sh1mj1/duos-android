package com.example.duos.ui.main.friendList


import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.RecommendedFriend
import com.example.duos.data.remote.myFriendList.FriendListService
import com.example.duos.data.remote.myFriendList.RecommendedFriendListOnDate
import com.example.duos.databinding.FragmentRecommendFriendListBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.utils.getFriendListDiaglogNotShowing
import com.example.duos.utils.getUserIdx
import org.threeten.bp.LocalDate
import org.threeten.bp.Period




class RecommendFriendListFragment() :
    BaseFragment<FragmentRecommendFriendListBinding>(FragmentRecommendFriendListBinding::inflate),
    RecommendedFriendListView {

    private var adapterList = arrayOfNulls<RecommendFriendListRVAdapter>(8)

    override fun initAfterBinding() {
        FriendListService.getRecommendedFriendList(this, getUserIdx()!!)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetRecommendedFriendListSuccess(starredFriendList: List<RecommendedFriendListOnDate>) {
        if (!getFriendListDiaglogNotShowing()) {
            activity?.supportFragmentManager?.let { fragmentManager ->
                FriendListDialogFragment().show(
                    fragmentManager,
                    "친구목록 다이얼로그"
                )
            }
        }
        // 날짜 파싱 & Dday 별로 recyclerview 에 매칭
        for (recommendedFriendListOnData in starredFriendList) {
            val recommendedAt = recommendedFriendListOnData.recommendedDate.toLocalDate()

            val period: Int = 7 - (Period.between(recommendedAt, LocalDate.now()).days)

            var recyclerviewId: Int = resources.getIdentifier(
                "recommend_friend_list_d_" + period.toString()
                        + "_recyclerview_rv", "id", requireActivity().packageName
            )

            // recyclerview 설정 & VISIBLE 하게 하기
            var recyclerview: RecyclerView = requireView().findViewById(recyclerviewId)
            recyclerview.visibility = View.VISIBLE
            recyclerview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapterList[period] =
                RecommendFriendListRVAdapter(recommendedFriendListOnData.recommendedFriendList as ArrayList<RecommendedFriend>)
            recyclerview.adapter = adapterList[period]
            recyclerview.itemAnimator = null

            // D-day text 설정 & VISIBLE 하게 하기
            var textviewId: Int = resources.getIdentifier(
                "recommend_friend_list_d_" + period.toString()
                        + "_text_tv", "id", requireActivity().packageName
            )
            var textview: TextView = requireView().findViewById(textviewId)
            textview.visibility = View.VISIBLE

            adapterList[period]?.setMyItemClickListener(object :
                RecommendFriendListRVAdapter.MyItemClickListener {
                override fun onDeleteFriend(friendId: String) {

                }

                override fun onAddFriend(friendId: String) {

                }

                override fun onDeleteText() {
                    textview.visibility = View.GONE
                }

            })

        }
    }

    override fun onGetRecommendedFriendListFailure(code: Int, message: String) {
        showToast("code : $code, message : $message")
    }
}