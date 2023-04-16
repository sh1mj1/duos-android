package com.example.duos.ui.main.dailyMatching

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.ApplicationClass
import com.example.duos.CustomDialog
import com.example.duos.R
import com.example.duos.data.entities.dailyMatching.DailyMatchingDetail
import com.example.duos.data.remote.dailyMatching.DailyMatchingWriteService
import com.example.duos.data.remote.dailyMatching.DailyWriteResult

import com.example.duos.databinding.ActivityDailyMatchingEditBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getUserIdx
import com.squareup.picasso.Picasso
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.BufferedSink
import org.json.JSONObject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.temporal.ChronoUnit
import java.io.File
import java.util.*
import android.graphics.drawable.Drawable
import java.lang.Exception


class DailyMatchingEdit :
    BaseActivity<ActivityDailyMatchingEditBinding>(ActivityDailyMatchingEditBinding::inflate),
    DailyMatchingEditView {

    var setTime: Int = 0
    var start: Int = 0
    var end: Int = 0
    var boardIdx: Int = -1
    lateinit var matchDate: LocalDate
    lateinit var startTime: LocalDateTime
    lateinit var endTime: LocalDateTime
    lateinit var viewModel: ViewModel
    var contentBitmap: Bitmap? = null
    var isSelectedImg: Boolean = false
    lateinit var dailyMatchingInfo: DailyMatchingDetail
    lateinit var currentTime: LocalDateTime
    lateinit var dailyMatchingEditRVAdapter: DailyMatchingTimeSelectRVAdapter

    lateinit var now : LocalDate
    lateinit var tomorrow : LocalDate
    lateinit var dayAfterTomorrow : LocalDate
    lateinit var strNow : String
    lateinit var strTomorrow : String
    lateinit var strDayAfterTomorrow: String

    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_PERMISSION_REQUEST = 100
    lateinit var contentUri: Uri
    val multiplePermissionsCode1 = 200
    val multiplePermissionsCode2 = 300

    // 멀티퍼미션 카메라1
    val requiredPermissions1 = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA,
    )

    // 멀티퍼미션 갤러리(앨범)2
    val permission_list = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onResume() {
        super.onResume()
        viewModel.dailyMatchingDateCheck.value = true
        viewModel.dailyMatchingTimeCheck.value = true
        viewModel.dailyMatchingImgCount.value = 0

        viewModel.dailyMatchingEditTitle.value = dailyMatchingInfo.title
        viewModel.dailyMatchingEditPlace.value = dailyMatchingInfo.matchPlace
        viewModel.dailyMatchingEditContent.value = dailyMatchingInfo.content

        currentTime = LocalDateTime.now()

        binding.dailyMatchingEditTodayDateTv.text = strNow
        binding.dailyMatchingEditTomorrowDateTv.text = strTomorrow
        binding.dailyMatchingEditDayAfterTomorrowDateTv.text = strDayAfterTomorrow

        Log.d("dailyMatchinginfo",dailyMatchingInfo.toString())

        when (dailyMatchingInfo.urls.size) {
            1 -> {
                setImage01()
            }
            2 -> {
                setImage01()
                setImage02()
            }
            3 -> {
                setImage01()
                setImage02()
                setImage03()
            }
        }
        when (dailyMatchingInfo.stringForMatchDateGap) {
            "오늘" -> {
                binding.dailyMatchingEditTodayTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primary
                    )
                )
                binding.dailyMatchingEditTodayRadioBtn.isChecked = true
                setTime = currentTime.hour + 1
                dailyMatchingEditRVAdapter.updateStartTime(setTime)
                dailyMatchingEditRVAdapter.setTime(
                    dailyMatchingInfo.startTime.substring(0 until 2).toInt(),
                    dailyMatchingInfo.duration
                )
                matchDate = now
                start = dailyMatchingInfo.startTime.substring(0 until 2).toInt()
                end = dailyMatchingInfo.startTime.substring(0 until 2)
                    .toInt() + dailyMatchingInfo.duration

            }
            "내일" -> {
                binding.dailyMatchingEditTomorrowTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primary
                    )
                )
                binding.dailyMatchingEditTomorrowRadioBtn.isChecked = true
                setTime = 0
                dailyMatchingEditRVAdapter.updateStartTime(setTime)
                dailyMatchingEditRVAdapter.setTime(
                    dailyMatchingInfo.startTime.substring(0 until 2).toInt(),
                    dailyMatchingInfo.duration
                )
                matchDate = tomorrow
                start = dailyMatchingInfo.startTime.substring(0 until 2).toInt()
                end = dailyMatchingInfo.startTime.substring(0 until 2)
                    .toInt() + dailyMatchingInfo.duration
            }
            "모레" -> {
                binding.dailyMatchingEditDayAfterTomorrowTv.setTextColor(
                    ContextCompat.getColor(
                        this,
                        R.color.primary
                    )
                )
                binding.dailyMatchingEditDayAfterTomorrowRadioBtn.isChecked = true
                setTime = 0
                dailyMatchingEditRVAdapter.updateStartTime(setTime)
                dailyMatchingEditRVAdapter.setTime(
                    dailyMatchingInfo.startTime.substring(0 until 2).toInt(),
                    dailyMatchingInfo.duration
                )
                matchDate = dayAfterTomorrow
                start = dailyMatchingInfo.startTime.substring(0 until 2).toInt()
                end = dailyMatchingInfo.startTime.substring(0 until 2)
                    .toInt() + dailyMatchingInfo.duration
            }
            else -> {}
        }
        binding.dailyMatchingEditSelectTimeTv.text =
            (start.toString() + ":00 - " +
                    end.toString() + ":00" + "(" + (dailyMatchingInfo.duration).toString() + "시간)")

    }

    override fun initAfterBinding() {

        // 데이터 받아오기 & 초기화하기
        boardIdx = intent.getIntExtra("boardIdx", -1)
        dailyMatchingInfo = intent.getSerializableExtra("dailyMatchingInfo") as DailyMatchingDetail

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ViewModel::class.java)

        binding.viewmodel = viewModel

        dailyMatchingEditRVAdapter = DailyMatchingTimeSelectRVAdapter(setTime)
        binding.dailyMatchingEditSelectTimeRecyclerviewRv.setHasFixedSize(false)
        binding.dailyMatchingEditSelectTimeRecyclerviewRv.itemAnimator = null
        binding.dailyMatchingEditSelectTimeRecyclerviewRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dailyMatchingEditSelectTimeRecyclerviewRv.adapter = dailyMatchingEditRVAdapter

        now = LocalDate.now()
        tomorrow = LocalDate.now().plus(1, ChronoUnit.DAYS)
        dayAfterTomorrow = LocalDate.now().plus(2, ChronoUnit.DAYS)
        strNow = now.format(DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREAN))
        strTomorrow = tomorrow.format(DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREAN))
        strDayAfterTomorrow =
            dayAfterTomorrow.format(DateTimeFormatter.ofPattern("M월 d일(E)", Locale.KOREAN))


        setEditBtnEnable()

        this.viewModel.dailyMatchingEditTitle.observe(this, {
            Log.d("1", "ㅎㅇ")
            if (it!!.isNotEmpty() && this.viewModel.dailyMatchingEditPlace.value?.isNotEmpty() == true
                && this.viewModel.dailyMatchingEditContent.value?.isNotEmpty() == true &&
                this.viewModel.dailyMatchingDateCheck.value == true &&
                this.viewModel.dailyMatchingTimeCheck.value == true
            ) {
                setEditBtnEnable()
            } else setEditBtnUnable()
        })
        this.viewModel.dailyMatchingEditPlace.observe(this, {
            Log.d("2", "ㅎㅇ")
            if (it!!.isNotEmpty() && this.viewModel.dailyMatchingEditTitle.value?.isNotEmpty() == true
                && this.viewModel.dailyMatchingEditContent.value?.isNotEmpty() == true &&
                this.viewModel.dailyMatchingDateCheck.value == true &&
                this.viewModel.dailyMatchingTimeCheck.value == true
            ) {
                setEditBtnEnable()
            } else setEditBtnUnable()
        })
        this.viewModel.dailyMatchingEditContent.observe(this, {
            Log.d("3", "ㅎㅇ")
            if (it!!.isNotEmpty() && this.viewModel.dailyMatchingEditTitle.value?.isNotEmpty() == true
                && this.viewModel.dailyMatchingEditPlace.value?.isNotEmpty() == true &&
                this.viewModel.dailyMatchingDateCheck.value == true &&
                this.viewModel.dailyMatchingTimeCheck.value == true
            ) {
                setEditBtnEnable()
            } else setEditBtnUnable()
        })
//        this.viewModel.dailyMatchingDateCheck.observe(this, {
//            Log.d("4", "ㅎㅇ")
//            if (it!! && this.viewModel.dailyMatchingEditTitle.value?.isNotEmpty() == true
//                && this.viewModel.dailyMatchingEditPlace.value?.isNotEmpty() == true &&
//                this.viewModel.dailyMatchingEditContent.value?.isNotEmpty() == true &&
//                this.viewModel.dailyMatchingTimeCheck.value == true
//            ) {
//                setEditBtnEnable()
//            } else setEditBtnUnable()
//        })
        this.viewModel.dailyMatchingTimeCheck.observe(this, {
            Log.d("5", "ㅎㅇ")
            if (it!! && this.viewModel.dailyMatchingEditTitle.value?.isNotEmpty() == true
                && this.viewModel.dailyMatchingEditPlace.value?.isNotEmpty() == true &&
                this.viewModel.dailyMatchingEditContent.value?.isNotEmpty() == true &&
                this.viewModel.dailyMatchingDateCheck.value == true
            ) {
                setEditBtnEnable()
            } else setEditBtnUnable()
        })




        dailyMatchingEditRVAdapter.setMyItemClickListener(object :
            DailyMatchingTimeSelectRVAdapter.MyItemClickListener {
            override fun onClickItem(startPosition: Int, endPosition: Int) {
                for (i in startPosition until endPosition + 1) {
                    val view =
                        binding.dailyMatchingEditSelectTimeRecyclerviewRv.findViewHolderForAdapterPosition(
                            i
                        )?.itemView
                    val btn = view?.findViewById<Button>(R.id.daily_matching_time_selector_btn)
                    btn?.isSelected = true
                    start = setTime + startPosition
                    end = setTime + endPosition + 1
                    binding.dailyMatchingEditSelectTimeTv.text =
                        ((setTime + startPosition).toString() + ":00 - " +
                                (setTime + endPosition + 1).toString() + ":00" + "(" + (endPosition - startPosition + 1).toString() + "시간)")
                    if (viewModel.dailyMatchingTimeCheck.value == false)
                        viewModel.dailyMatchingTimeCheck.value = true
                }
            }

            override fun onResetItem(startPosition: Int) {
                if (viewModel.dailyMatchingTimeCheck.value == false)
                    viewModel.dailyMatchingTimeCheck.value = true
                binding.dailyMatchingEditSelectTimeTv.text =
                    ((setTime + startPosition).toString() + ":00 - " +
                            (setTime + startPosition + 1).toString() + ":00" + "(" + "1시간)")
                for (i in 0 until dailyMatchingEditRVAdapter.itemCount) {
                    val view =
                        binding.dailyMatchingEditSelectTimeRecyclerviewRv.findViewHolderForAdapterPosition(
                            i
                        )?.itemView
                    val btn = view?.findViewById<Button>(R.id.daily_matching_time_selector_btn)
                    btn?.isSelected = false
                }
                start = setTime + startPosition
                end = setTime + startPosition + 1
            }
        })

        binding.dailyMatchingEditRadioGroupRg.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            when (radio.tag) {
                "today" -> {
                    setTime = currentTime.hour + 1
                    dailyMatchingEditRVAdapter.updateStartTime(setTime)
                    binding.dailyMatchingEditTodayTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.primary
                        )
                    )
                    binding.dailyMatchingEditTomorrowTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray_A
                        )
                    )
                    binding.dailyMatchingEditDayAfterTomorrowTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray_A
                        )
                    )
                    matchDate = now
                    viewModel.dailyMatchingDate.value = "오늘"
                }
                "tomorrow" -> {
                    setTime = 0
                    dailyMatchingEditRVAdapter.updateStartTime(setTime)

                    binding.dailyMatchingEditTomorrowTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.primary
                        )
                    )
                    binding.dailyMatchingEditDayAfterTomorrowTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray_A
                        )
                    )
                    binding.dailyMatchingEditTodayTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray_A
                        )
                    )
                    matchDate = tomorrow
                    viewModel.dailyMatchingDate.value = "내일"
                }
                "dayaftertomorrow" -> {
                    setTime = 0
                    dailyMatchingEditRVAdapter.updateStartTime(setTime)

                    binding.dailyMatchingEditDayAfterTomorrowTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.primary
                        )
                    )
                    binding.dailyMatchingEditTomorrowTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray_A
                        )
                    )
                    binding.dailyMatchingEditTodayTv.setTextColor(
                        ContextCompat.getColor(
                            this,
                            R.color.dark_gray_A
                        )
                    )
                    matchDate = dayAfterTomorrow
                    viewModel.dailyMatchingDate.value = "모레"

                }
            }
        })

        viewModel.dailyMatchingImgCount.observe(this, {
            binding.dailyMatchingEditImageCountTv.text = it.toString()
        })

        binding.dailyMatchingEditBackArrowIv.setOnClickListener {
            showDialog(this)
        }

        binding.dailyMatchingEditBtn.setOnClickListener {
            Log.d("완료", "클릭")
            startTime =
                LocalDateTime.of(matchDate.year, matchDate.month, matchDate.dayOfMonth, start, 0, 0)
            if (end == 24) {
                endTime =
                    LocalDateTime.of(
                        matchDate.plus(1, ChronoUnit.DAYS).year,
                        matchDate.plus(1, ChronoUnit.DAYS).month,
                        matchDate.plus(1, ChronoUnit.DAYS).dayOfMonth,
                        0,
                        0,
                        0
                    )
            } else {
                endTime =
                    LocalDateTime.of(
                        matchDate.year,
                        matchDate.month,
                        matchDate.dayOfMonth,
                        end,
                        0,
                        0
                    )
            }


            val JSON = "application/json; charset=utf-8".toMediaTypeOrNull()
            val json = JSONObject()


            json.put("boardIdx", boardIdx)
            json.put("writerIdx", getUserIdx()!!)
            json.put("title", viewModel.dailyMatchingEditTitle.value.toString())
            json.put("content", viewModel.dailyMatchingEditContent.value.toString())
            json.put("matchDate", matchDate)
            json.put("matchPlace", viewModel.dailyMatchingEditPlace.value.toString())
            json.put("startTime", startTime)
            json.put("endTime", endTime)

            Log.d("boardIdx", boardIdx.toString())
            Log.d("writerIdx", getUserIdx()!!.toString())
            Log.d("title", viewModel.dailyMatchingEditTitle.value.toString())
            Log.d("content", viewModel.dailyMatchingEditContent.value.toString())
            Log.d("matchDate", matchDate.toString())
            Log.d("matchPlace", viewModel.dailyMatchingEditPlace.value.toString())
            Log.d("startTime", startTime.toString())
            Log.d("endTime", endTime.toString())

            val editInfo = json.toString().toRequestBody(JSON)

            Log.d("json", editInfo.toString())

            val bitmapMultipartBody: MutableList<MultipartBody.Part> = mutableListOf()
            if (viewModel.dailyMatchingImg01Bitmap.value != null) {
                Log.d("사진1", viewModel.dailyMatchingImg01Bitmap.value.toString())
                val bitmapRequestBody =
                    viewModel.dailyMatchingImg01Bitmap.value.let { BitmapRequestBody(it!!) }
                bitmapMultipartBody.add(
                    MultipartBody.Part.createFormData(
                        "mFiles",
                        "img01",
                        bitmapRequestBody
                    )
                )
                isSelectedImg = true
            }
            if (viewModel.dailyMatchingImg02Bitmap.value != null) {
                Log.d("ㅎㅇ", "사진2")
                val bitmapRequestBody =
                    viewModel.dailyMatchingImg02Bitmap.value.let { BitmapRequestBody(it!!) }
                bitmapMultipartBody.add(
                    MultipartBody.Part.createFormData(
                        "mFiles",
                        "img02",
                        bitmapRequestBody
                    )
                )
                isSelectedImg = true
            }
            if (viewModel.dailyMatchingImg03Bitmap.value != null) {
                Log.d("ㅎㅇ", "사진3")
                val bitmapRequestBody =
                    viewModel.dailyMatchingImg03Bitmap.value.let { BitmapRequestBody(it!!) }
                bitmapMultipartBody.add(
                    MultipartBody.Part.createFormData(
                        "mFiles",
                        "img03",
                        bitmapRequestBody
                    )
                )
                isSelectedImg = true
            }

            // 이미지를 선택했다면
            if (isSelectedImg) {
                Log.d("사진", bitmapMultipartBody.toString())
                DailyMatchingWriteService.dailyMatchingEditWithImg(
                    this,
                    bitmapMultipartBody,
                    editInfo
                )
            }
            // 만약 이미지 사진을 선택안했다면
            else {
                DailyMatchingWriteService.dailyMatchingEditWithoutImg(this, editInfo)
            }
        }


        binding.dailyMatchingEditSelectImageLayoutCl.setOnClickListener {
            requestImg()
        }

        binding.dailyMatchingEditSelectErase01Iv.setOnClickListener {
            viewModel.dailyMatchingImg01.value = false
            viewModel.dailyMatchingImg01Bitmap.value = null
            viewModel.dailyMatchingImgCount.value = viewModel.dailyMatchingImgCount.value?.minus(1)
            binding.dailyMatchingEditSelectImageLayout01Iv.visibility = View.GONE
            isSelectedImg = false
        }

        binding.dailyMatchingEditSelectErase02Iv.setOnClickListener {
            viewModel.dailyMatchingImg02.value = false
            viewModel.dailyMatchingImg02Bitmap.value = null
            viewModel.dailyMatchingImgCount.value = viewModel.dailyMatchingImgCount.value?.minus(1)
            binding.dailyMatchingEditSelectImageLayout02Iv.visibility = View.GONE
        }

        binding.dailyMatchingEditSelectErase03Iv.setOnClickListener {
            viewModel.dailyMatchingImg03.value = false
            viewModel.dailyMatchingImgCount.value = viewModel.dailyMatchingImgCount.value?.minus(1)
            viewModel.dailyMatchingImg03Bitmap.value = null
            binding.dailyMatchingEditSelectImageLayout03Iv.visibility = View.GONE
        }
    }


    fun setEditBtnEnable() {
        binding.dailyMatchingEditBtn.isEnabled = true
        binding.dailyMatchingEditBtn.background =
            getDrawable(R.drawable.signup_next_btn_done_rectangular)
        binding.dailyMatchingEditBtn.setTextColor(getColor(R.color.white))
    }

    fun setEditBtnUnable() {
        binding.dailyMatchingEditBtn.isEnabled = false
        binding.dailyMatchingEditBtn.background =
            getDrawable(R.drawable.signup_next_btn_rectangular)
        binding.dailyMatchingEditBtn.setTextColor(getColor(R.color.dark_gray_B0))
    }

    fun showDialog(context: Context) {
        // 만약 액티비티에서 사용한다면 아래 requireContext() 가 아닌 context를 사용하면 됨.
        val dialog = CustomDialog.Builder(context)
            .setCommentMessage(getString(R.string.daily_matching_edit_cancel))// Dialog텍스트 설정하기 "~~~ "
            .setRightButton(
                getString(R.string.daily_matching_write_delete),
                object : CustomDialog.CustomDialogCallback {
                    override fun onClick(
                        dialog: CustomDialog,
                        message: String
                    ) {//오른쪽 버튼 클릭시 이벤트 설정하기
                        finish()
                        dialog.dismiss()
                    }
                })
            .setLeftButton(
                getString(R.string.daily_matching_write_continue),
                object : CustomDialog.CustomDialogCallback {
                    override fun onClick(
                        dialog: CustomDialog,
                        message: String
                    ) {//왼쪽 버튼 클릭시 이벤트 설정하기
                        dialog.dismiss()
                    }
                })
        dialog.show()

    }


    override fun onBackPressed() {
        showDialog(this)
    }


    fun requestImg() {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(R.string.upload_pic_dialog_title)
            // setItems 대신 setAdapter()를 사용하여 목록을 지정 가능
            // 이렇게 하면 동적 데이터가 있는 목록(예: 데이터베이스에서 가져온 것을 ListAdapter로 지원할 수 있다.)
            .setItems(
                R.array.upload_pic_dialog_title,
                DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        // 카메라 0 썸네일로
                        0 -> {
                            var permissionResult0 = ContextCompat.checkSelfPermission(
                                this,
                                CAMERA_PERMISSION[0]
                            )
                            Log.d("Signup_Image_Upload_Dialog", "checkSelfPermission$which")

                            when (permissionResult0) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    Log.d(
                                        "Signup_Image_Upload_Dialog",
                                        "PERMISSION_GRANTED$which"
                                    )
                                    // 카메라 권한이 이미 허용된 상태일 때 바로 카메라 액티비티 호출
                                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    startActivityForResult(intent, CAMERA_PERMISSION_REQUEST)
                                }
                                PackageManager.PERMISSION_DENIED -> {
                                    Log.d(
                                        "Signup_Image_Upload_Dialog",
                                        "PERMISSION_DENIED$which"
                                    )
                                    // 카메라 권한이 허용된 상태가 아닐 때
                                    // ActivityCompat.requestPermissions(requireActivity(), CAMERA_PERMISSION, CAMERA_PERMISSION_REQUEST)
                                    // Fragment에서 onRequestPermissionsResult 호출하려면 requestPermissions만 쓰기
                                    requestPermissions(
                                        CAMERA_PERMISSION,
                                        CAMERA_PERMISSION_REQUEST
                                    )
                                    // 이 떄 onRequestPermissionsResult 메소드 호출
                                }
                            }
                        }

                        // 파일 접근
                        1 -> {
                            Log.d("Signup_Image_Upload_Dialog", "파일 접근 $which")
                            val rejectedPermissionList = ArrayList<String>()
                            // 필요한 퍼미션들이 현재 권한을 받았는지 체크
                            for (permission in permission_list) {
                                if (ContextCompat.checkSelfPermission(
                                        this,
                                        permission
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    //만약 권한이 없다면 rejectedPermissionList에 추가
                                    rejectedPermissionList.add(permission)
                                }
                            }
                            if (rejectedPermissionList.isNotEmpty()) {   // 거절된 퍼미션 있다면?
                                val array = arrayOfNulls<String>(rejectedPermissionList.size)
                                requestPermissions(
                                    rejectedPermissionList.toArray(array),
                                    multiplePermissionsCode2
                                )
                            } else {
                                // 앨범에서 사진을 선택할 수 있도록 Intent
                                val albumIntent = Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                                // 실행할 액티비티의 타입 설정(이미지를 선택할 수 있는 것)
                                albumIntent.type = "image/*"
                                // 선택할 파일의 타입 지정
                                val mimeType = arrayOf("image/*")
                                albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                                albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                                startActivityForResult(albumIntent, multiplePermissionsCode2)
                            }
                        }
                    }
                })
        dialogBuilder.create().show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        Log.d("Signup_Image_Upload_Dialog", "OnRequestPermissionsResult 호출댐.")
        val file_path = this.getExternalFilesDir(null).toString()
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 허용을 눌렀을 때 바로 카메라로 ㄱㄱ
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, CAMERA_PERMISSION_REQUEST)

                } else {
                    // 권한 거부 시 로그 띄우기
                    Log.d("Signup_Image_Upload_Dialog", "OnRequestPermissionsResult에서 카메라0 권한 거부.")
                    Toast.makeText(
                        this,
                        "프로필 사진을 업로드하려면 카메라 접근 권한을 허용해야 합니다.",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }

            multiplePermissionsCode1 -> {
                var startCam = true
                if (grantResults.isNotEmpty()) {
                    for ((i, permission) in permissions.withIndex()) {
                        // 권한이 없는 permission이 있다면
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("Signup", "사용하려면 권한 체크 해야되")
                            Toast.makeText(
                                this,
                                "프로필 사진을 업로드하려면 카메라 접근 권한을 허용해야 합니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            startCam = false
                        }
                    }
                    if (startCam) {
                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                        // 촬영한 사진이 저장될 파일 이름
                        val file_name = "/temp_${System.currentTimeMillis()}.jpg"
                        // 경로 + 파일 이름
                        val pic_path = "$file_path/$file_name"
                        val file = File(pic_path)

                        // 사진이 저장될 위치를 관리하는 Uri 객체
                        // val contentUri = Uri(pic_path) // 예전에는 파일명을 기술하면 바로 접근 가능
                        // -> 현재 안드로이드 OS 6.0 부터는 OS에서 해당 경로를 집어 넣으면 이 경로로 접근할 수 있는지 없는지를 판단. 접근할 수 있으면 Uri 객체를 넘겨줌.
                        contentUri = FileProvider.getUriForFile(
                            this,
                            "com.duos.camera.file_provider",
                            file
                        )

                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                        startActivityForResult(intent, 200)
                    }
                }
            }
            multiplePermissionsCode2 -> {
                var startAlb = true
                if (grantResults.isNotEmpty()) {
                    for ((i, permission) in permissions.withIndex()) {
                        // 권한이 없는 permission이 있다면
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            Log.d("Signup", "사용하려면 권한 체크 해야되")
                            Toast.makeText(
                                this,
                                "프로필 사진을 업로드하려면 카메라 접근 권한을 허용해야 합니다.",
                                Toast.LENGTH_LONG
                            ).show()
                            startAlb = false
                        }
                    }
                    if (startAlb) {
                        // 앨범에서 사진을 선택할 수 있도록 Intent
                        val albumIntent =
                            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        // 실행할 액티비티의 타입 설정(이미지를 선택할 수 있는 것)
                        albumIntent.type = "image/*"
                        // 선택할 파일의 타입 지정
                        val mimeType = arrayOf("image/*")
                        albumIntent.action = Intent.ACTION_GET_CONTENT
                        albumIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimeType)
                        albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                        startActivityForResult(albumIntent, multiplePermissionsCode2)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CAMERA_PERMISSION_REQUEST -> {
                if (resultCode == Activity.RESULT_OK) {
                    // data : Intent 안에 사진 정보가 들어감
                    val bitmap = data?.getParcelableExtra<Bitmap>("data")
                    contentBitmap = bitmap!!
                    if (viewModel.dailyMatchingImg01.value == false) {
                        Log.d("사진1번", "false")
                        viewModel.dailyMatchingImgCount.value =
                            viewModel.dailyMatchingImgCount.value?.plus(1)
                        viewModel.dailyMatchingImg01Bitmap.value = contentBitmap
                        viewModel.dailyMatchingImg01.value = true
                        binding.dailyMatchingEditSelectImageLayout01Iv.visibility = View.VISIBLE
                        binding.dailyMatchingEditSelectImage01Iv.scaleType =
                            ImageView.ScaleType.FIT_XY
                        binding.dailyMatchingEditSelectImage01Iv.setImageBitmap(bitmap)

                    } else if (viewModel.dailyMatchingImg02.value == false) {
                        Log.d("사진2번", "false")
                        viewModel.dailyMatchingImgCount.value =
                            viewModel.dailyMatchingImgCount.value?.plus(1)
                        viewModel.dailyMatchingImg02Bitmap.value = contentBitmap
                        viewModel.dailyMatchingImg02.value = true
                        binding.dailyMatchingEditSelectImageLayout02Iv.visibility = View.VISIBLE
                        binding.dailyMatchingEditSelectImage02Iv.scaleType =
                            ImageView.ScaleType.FIT_XY
                        binding.dailyMatchingEditSelectImage02Iv.setImageBitmap(bitmap)

                    } else if (viewModel.dailyMatchingImg03.value == false) {
                        Log.d("사진3번", "false")
                        viewModel.dailyMatchingImgCount.value =
                            viewModel.dailyMatchingImgCount.value?.plus(1)
                        viewModel.dailyMatchingImg03Bitmap.value = contentBitmap
                        viewModel.dailyMatchingImg03.value = true
                        binding.dailyMatchingEditSelectImageLayout03Iv.visibility = View.VISIBLE
                        binding.dailyMatchingEditSelectImage03Iv.scaleType =
                            ImageView.ScaleType.FIT_XY
                        binding.dailyMatchingEditSelectImage03Iv.setImageBitmap(bitmap)

                    } else {
                        showToast("사진은 3장까지 선택 가능합니다.")
                    }
                }
            }

            multiplePermissionsCode2 -> {
                if (resultCode == Activity.RESULT_OK) {
                    Log.d("ㅎㅇ2", resultCode.toString())
                    // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체를 추출
                    if (data != null) {

                        if (data?.clipData?.getItemCount()!! > 3) {
                            showToast("사진은 3장까지 선택 가능합니다.")
                            return
                        }

                        // 이미지를 하나만 선택한 경우
                        if (data.clipData == null) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                Log.d("하나", "선택1")
                                val uri = data?.data!!

                                val source =
                                    ImageDecoder.createSource(this.contentResolver, uri)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                contentBitmap = bitmap

                            } else {
                                Log.d("하나", "선택2")
                                val uri = data?.data!!
                                val cursor =
                                    this.contentResolver.query(uri, null, null, null, null)
                                if (cursor != null) {
                                    cursor.moveToNext()
                                    // 이미지 경로를 가져온다.
                                    val index = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                    val source = cursor.getString(index)
                                    // 이미지 생성
                                    val bitmap = BitmapFactory.decodeFile(source)
                                    contentBitmap = bitmap
                                }
                            }
                            if (viewModel.dailyMatchingImg01.value == false) {
                                viewModel.dailyMatchingImgCount.value =
                                    viewModel.dailyMatchingImgCount.value?.plus(1)
                                viewModel.dailyMatchingImg01Bitmap.value = contentBitmap
                                viewModel.dailyMatchingImg01.value = true
                                binding.dailyMatchingEditSelectImageLayout01Iv.visibility =
                                    View.VISIBLE
                                binding.dailyMatchingEditSelectImage01Iv.scaleType =
                                    ImageView.ScaleType.FIT_XY
                                binding.dailyMatchingEditSelectImage01Iv.setImageBitmap(
                                    contentBitmap
                                )
                            } else if (viewModel.dailyMatchingImg02.value == false) {
                                viewModel.dailyMatchingImgCount.value =
                                    viewModel.dailyMatchingImgCount.value?.plus(1)
                                viewModel.dailyMatchingImg02Bitmap.value = contentBitmap
                                viewModel.dailyMatchingImg02.value = true
                                binding.dailyMatchingEditSelectImageLayout02Iv.visibility =
                                    View.VISIBLE
                                binding.dailyMatchingEditSelectImage02Iv.scaleType =
                                    ImageView.ScaleType.FIT_XY
                                binding.dailyMatchingEditSelectImage02Iv.setImageBitmap(
                                    contentBitmap
                                )
                            } else if (viewModel.dailyMatchingImg03.value == false) {
                                viewModel.dailyMatchingImgCount.value =
                                    viewModel.dailyMatchingImgCount.value?.plus(1)
                                viewModel.dailyMatchingImg03Bitmap.value = contentBitmap
                                viewModel.dailyMatchingImg03.value = true
                                binding.dailyMatchingEditSelectImageLayout03Iv.visibility =
                                    View.VISIBLE
                                binding.dailyMatchingEditSelectImage03Iv.scaleType =
                                    ImageView.ScaleType.FIT_XY
                                binding.dailyMatchingEditSelectImage03Iv.setImageBitmap(
                                    contentBitmap
                                )
                            } else {
                                showToast("사진은 3장까지 선택 가능합니다.")
                            }
                        } else if (data.clipData!!.itemCount >= 1 && data.clipData!!.itemCount <= 3) {
                            for (i in 0 until data.clipData!!.getItemCount()) {
                                val uri = data.clipData!!.getItemAt(i).uri
                                val source =
                                    ImageDecoder.createSource(this.contentResolver, uri)
                                val bitmap = ImageDecoder.decodeBitmap(source)
                                contentBitmap = bitmap

                                if (viewModel.dailyMatchingImg01.value == false) {
                                    viewModel.dailyMatchingImgCount.value =
                                        viewModel.dailyMatchingImgCount.value?.plus(1)
                                    viewModel.dailyMatchingImg01Bitmap.value = contentBitmap
                                    viewModel.dailyMatchingImg01.value = true
                                    binding.dailyMatchingEditSelectImageLayout01Iv.visibility =
                                        View.VISIBLE
                                    binding.dailyMatchingEditSelectImage01Iv.scaleType =
                                        ImageView.ScaleType.FIT_XY
                                    binding.dailyMatchingEditSelectImage01Iv.setImageBitmap(
                                        contentBitmap
                                    )
                                } else if (viewModel.dailyMatchingImg02.value == false) {
                                    viewModel.dailyMatchingImgCount.value =
                                        viewModel.dailyMatchingImgCount.value?.plus(1)
                                    viewModel.dailyMatchingImg02Bitmap.value = contentBitmap
                                    viewModel.dailyMatchingImg02.value = true
                                    binding.dailyMatchingEditSelectImageLayout02Iv.visibility =
                                        View.VISIBLE
                                    binding.dailyMatchingEditSelectImage02Iv.scaleType =
                                        ImageView.ScaleType.FIT_XY
                                    binding.dailyMatchingEditSelectImage02Iv.setImageBitmap(
                                        contentBitmap
                                    )
                                } else if (viewModel.dailyMatchingImg03.value == false) {
                                    viewModel.dailyMatchingImgCount.value =
                                        viewModel.dailyMatchingImgCount.value?.plus(1)
                                    viewModel.dailyMatchingImg03Bitmap.value = contentBitmap
                                    viewModel.dailyMatchingImg03.value = true
                                    binding.dailyMatchingEditSelectImageLayout03Iv.visibility =
                                        View.VISIBLE
                                    binding.dailyMatchingEditSelectImage03Iv.scaleType =
                                        ImageView.ScaleType.FIT_XY
                                    binding.dailyMatchingEditSelectImage03Iv.setImageBitmap(
                                        contentBitmap
                                    )
                                } else {
                                    showToast("사진은 3장까지 선택 가능합니다.")
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    inner class BitmapRequestBody(private val bitmap: Bitmap) : RequestBody() {
        override fun contentType(): MediaType = "image/png".toMediaType()
        override fun writeTo(sink: BufferedSink) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, sink.outputStream())
        }
    }

    override fun onDailyMatchingEditSuccess(dailyWriteResult: DailyWriteResult) {
        Log.d(ApplicationClass.TAG, dailyWriteResult.boradIdx.toString())
        finish()
    }

    override fun onDailyMatchingEditFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
    }

    private fun setImage01() {

        Picasso.get().load(dailyMatchingInfo.urls[0]).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                binding.dailyMatchingEditSelectImage01Iv.setImageBitmap(bitmap)
                viewModel.dailyMatchingImg01Bitmap.value = bitmap
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        })

        viewModel.dailyMatchingImgCount.value =
            viewModel.dailyMatchingImgCount.value?.plus(1)
        binding.dailyMatchingEditSelectImageLayout01Iv.visibility = View.VISIBLE

        binding.dailyMatchingEditSelectImage01Iv.scaleType = ImageView.ScaleType.FIT_XY
        viewModel.dailyMatchingImg01.value = true
    }

    private fun setImage02() {

        Picasso.get().load(dailyMatchingInfo.urls[1]).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                binding.dailyMatchingEditSelectImage02Iv.setImageBitmap(bitmap)
                viewModel.dailyMatchingImg02Bitmap.value = bitmap
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        })

        viewModel.dailyMatchingImgCount.value =
            viewModel.dailyMatchingImgCount.value?.plus(1)
        binding.dailyMatchingEditSelectImageLayout02Iv.visibility = View.VISIBLE
        binding.dailyMatchingEditSelectImage02Iv.scaleType = ImageView.ScaleType.FIT_XY
        viewModel.dailyMatchingImg02.value = true
    }


    private fun setImage03() {

        Picasso.get().load(dailyMatchingInfo.urls[2]).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                binding.dailyMatchingEditSelectImage03Iv.setImageBitmap(bitmap)
                viewModel.dailyMatchingImg03Bitmap.value = bitmap
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

        })

        viewModel.dailyMatchingImgCount.value =
            viewModel.dailyMatchingImgCount.value?.plus(1)

        binding.dailyMatchingEditSelectImageLayout03Iv.visibility = View.VISIBLE
        binding.dailyMatchingEditSelectImage03Iv.scaleType = ImageView.ScaleType.FIT_XY
        viewModel.dailyMatchingImg03.value = true
    }

}