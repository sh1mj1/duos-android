package com.example.duos.ui.main.dailyMatching

import android.util.Log
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.BottomSheetDialog01
import com.example.duos.BottomSheetDialog02
import com.example.duos.BottomSheetDialog03
import com.example.duos.data.entities.dailyMatching.DailyMatchingDetail
import com.example.duos.data.entities.dailyMatching.DailyMatchingOption
import com.example.duos.data.remote.dailyMatching.DailyMatchingListService
import com.example.duos.data.remote.dailyMatching.DailyMatchingService
import com.example.duos.databinding.ActivityDailyMatchingDetailBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.utils.getUserIdx

class DailyMatchingDetail : BaseActivity<ActivityDailyMatchingDetailBinding>(
    ActivityDailyMatchingDetailBinding::inflate
), GetDailyMatchingDetailView, GetDailyMatchingOptionView, DailyMatchingOptionListener, DailyMatchingEndView,
DailyMatchingDeleteView{
    var boardIdx: Int = -1
    override fun initAfterBinding() {

        val intent = intent
        boardIdx = intent.getIntExtra("boardIdx", -1)
        if (boardIdx == -1) {
            showToast("존재하지 않는 게시물 입니다.")
            finish()
        } else {
            DailyMatchingListService.getDailyMatchingDetail(this, boardIdx, getUserIdx()!!)
        }

        binding.dailyMatchingDetailBackArrowIv.setOnClickListener {
            finish()
        }
        binding.dailyMatchingDetailOptionIconIv.setOnClickListener {
            DailyMatchingService.getDailyMatchingOption(this, boardIdx, getUserIdx()!!)
        }

    }

    override fun onGetDailyMatchingDetailSuccess(dailyMatchingDetail: DailyMatchingDetail) {
        Glide.with(binding.dailyMatchingDetailProfileImageIv.context)
            .load(dailyMatchingDetail.profileUrl)
            .into(binding.dailyMatchingDetailProfileImageIv)
        binding.dailyMatchingDetailNicknameTv.text = dailyMatchingDetail.nickname
        binding.dailyMatchingDetailReviewCountTv.text = dailyMatchingDetail.review_Num.toString()
        binding.dailyMatchingDetailRatingTv.text = String.format("%.1f", dailyMatchingDetail.rating)
        binding.dailyMatchingPostPreviewLocationTv.text = dailyMatchingDetail.matchPlace
        binding.dailyMatchingDetailDateTv.text =
            dailyMatchingDetail.matchDate + " (" + dailyMatchingDetail.dayOfWeek + ")"
        binding.dailyMatchingDetailTimeTv.text =
            dailyMatchingDetail.startTime + " - " + dailyMatchingDetail.endTime
        binding.dailyMatchingDetailTimeCountTv.text = dailyMatchingDetail.duration.toString() + "시간"
        binding.dailyMatchingDetailContentTv.text = dailyMatchingDetail.content
        binding.dailyMatchingDetailContentTimeTv.text = dailyMatchingDetail.regBefore
        binding.dailyMatchingDetailSeeCountTv.text = dailyMatchingDetail.viewCount.toString()
        binding.dailyMatchingDetailChatCountTv.text = dailyMatchingDetail.messageCount.toString()
        binding.dailyMatchingDetailPreviewDateTv.text = dailyMatchingDetail.stringForMatchDateGap
        for (i in dailyMatchingDetail.urls) {
            var urlId: Int = resources.getIdentifier(
                "daily_matching_detail_content_image_0" + i + 1 + "_iv",
                "id",
                this.packageName
            )
            var imgView: ImageView = this.findViewById(urlId)
            Glide.with(imgView)
                .load(i)
                .into(imgView)
        }

    }

    override fun onGetDailyMatchingDetailFailure(code: Int, message: String) {
        Log.d(TAG, code.toString() + " : " + message)
    }

    override fun onGetDailyMatchingOptionSuccess(dailyMatchingOption: DailyMatchingOption) {
       when (dailyMatchingOption.options.size){
           1 ->{
               val bottomSheet = BottomSheetDialog02()
               bottomSheet.show(supportFragmentManager, bottomSheet.tag)
           }
           2->{
               val bottomSheet = BottomSheetDialog03()
               bottomSheet.show(supportFragmentManager, bottomSheet.tag)
           }
           3->{
               val bottomSheet = BottomSheetDialog01(boardIdx)
               bottomSheet.show(supportFragmentManager, bottomSheet.tag)
           }
       }
    }

    override fun onGetDailyMatchingOptionFailure(code: Int, message: String) {
        Log.d(TAG, code.toString() + " : " + message)
    }

    override fun onClickEdit() {

    }

    override fun onClickDelete() {
        DailyMatchingService.dailyMatchingEnd(this, boardIdx, getUserIdx()!!)
    }

    override fun onClickChat() {

    }

    override fun onClickEnd() {
        DailyMatchingService.dailyMatchingEnd(this, boardIdx, getUserIdx()!!)
    }

    override fun onDailyMatchingEndSuccess() {
        showToast("마감이 완료되었습니다.")
        finish()
    }

    override fun onDailyMatchingEndFailure(code: Int, message: String) {
        showToast(message)
    }

    override fun onDailyMatchingDeleteSuccess() {
        showToast("게시물 삭제가 완료되었습니다.")
        finish()
    }

    override fun onDailyMatchingDeleteFailure(code: Int, message: String) {
        showToast(message)
    }
}