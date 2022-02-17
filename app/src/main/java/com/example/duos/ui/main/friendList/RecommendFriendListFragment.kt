package com.example.duos.ui.main.friendList


import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.RecommendedFriend
import com.example.duos.data.remote.myFriendList.FriendListService
import com.example.duos.data.remote.myFriendList.RecommendHistoryDto
import com.example.duos.databinding.FragmentRecommendFriendListBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getFriendListDiaglogNotShowing
import com.example.duos.utils.getUserIdx
import org.threeten.bp.LocalDate
import org.threeten.bp.Period




class RecommendFriendListFragment() :
    BaseFragment<FragmentRecommendFriendListBinding>(FragmentRecommendFriendListBinding::inflate),
    RecommendedFriendListView, AddStarredFriendView, DeleteStarredFriendView {

    lateinit var viewModel: ViewModel
    private var adapterList = arrayOfNulls<RecommendFriendListRVAdapter>(8)

    override fun onResume() {
       getRecommendedFriend()
        super.onResume()
    }

    override fun initAfterBinding() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetRecommendedFriendListSuccess(starredFriendList: List<RecommendHistoryDto>) {

        Log.d("지난추천친구","성공")

        if (!getFriendListDiaglogNotShowing()) {
            activity?.supportFragmentManager?.let { fragmentManager ->
                FriendListDialogFragment().show(
                    fragmentManager,
                    "친구목록 다이얼로그"
                )
            }
        }
        // 날짜 파싱 & Dday 별로 recyclerview 에 매칭
        for (friendList in starredFriendList) {
            Log.d("friendlist",friendList.toString())
            val recommendedAt = friendList.recommendedDate.toLocalDate()

            val period: Int = 7 - (Period.between(recommendedAt, LocalDate.now()).days)

            var recyclerviewId: Int = resources.getIdentifier(
                "recommend_friend_list_d_" + period.toString()
                        + "_recyclerview_rv", "id", requireActivity().packageName
            )

            // recyclerview 설정 & VISIBLE 하게 하기
            var recyclerview: RecyclerView = requireView().findViewById(recyclerviewId)
            recyclerview.setHasFixedSize(true)
            recyclerview.visibility = View.VISIBLE
            recyclerview.layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapterList[period] =
                RecommendFriendListRVAdapter(friendList.pastRecommendPartnerDto as ArrayList<RecommendedFriend>)
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
                override fun onDeleteFriend(partnerIdx : Int) {
                    Log.d("추천친구삭제","bye")
                }

                override fun onAddFriend(friend: RecommendedFriend) {
                    // 하트값을 viewModel 로 observe 해서 빈하트라면, 찜하기 , 채워져있는 하트라면, 찜하기 취소 api를 호출하자.
                    if (friend.recommendedFriendIsStarred == true){
                        Log.d("찜삭제","api 호출하기")
                        viewModel.friendCount.value = viewModel.friendCount.value?.minus(1)
                        deleteStarredFriend(friend.partnerIdx!!)
                    } else{
                        Log.d("찜하기","api 호출하기")
                        viewModel.friendCount.value = viewModel.friendCount.value?.plus(1)
                        addStarredFriend(friend.partnerIdx!!)
                    }
                }

                override fun onDeleteText() {
                    textview.visibility = View.GONE
                }

                override fun gotoPartnerProfileActivity(partnerIdx: Int) {
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

        }
    }

    fun getRecommendedFriend(){
        FriendListService.getRecommendedFriendList(this, getUserIdx()!!)
    }

    fun addStarredFriend(partnerIdx : Int){
        FriendListService.addStarredFriend(this, getUserIdx()!!, partnerIdx)
    }

    fun deleteStarredFriend(partnerIdx : Int){
        FriendListService.deleteStarredFriend(this, getUserIdx()!!, partnerIdx)
    }

    override fun onGetRecommendedFriendListFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //showToast("code : $code, message : $message")
    }

    override fun onAddStarredFriendSuccess() {
        Log.d("찜하기","성공")
    }

    override fun onAddStarredFriendFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //showToast("code : $code, message : $message")
    }

    override fun onDeleteStarredFriendSuccess() {
        Log.d("찜삭제","성공")
    }

    override fun onDeleteStarredFriendFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //showToast("code : $code, message : $message")
    }
}