package com.example.duos.ui.signup

import android.Manifest
import android.app.Activity.BLOB_STORE_SERVICE
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup05Binding
import com.example.duos.ui.BaseFragment
import java.io.File

class SignUpFragment05() : BaseFragment<FragmentSignup05Binding>(FragmentSignup05Binding::inflate) {

    lateinit var contentUri: Uri

    val CAMERA_PERMISSION = arrayOf(Manifest.permission.CAMERA)
    val CAMERA_PERMISSION_REQUEST = 100

    val STORAGE_PERMISSION =
        arrayOf(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val STORAGE_PERMISSION_REQUEST = 200


    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "05"

        val file_path = requireActivity().getExternalFilesDir(null).toString()

        binding.signup05ProfileImageBackgroundIv.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder.setTitle(R.string.upload_pic_dialog_title)
                // setItems 대신 setAdapter()를 사용하여 목록을 지정 가능
                // 이렇게 하면 동적 데이터가 있는 목록(예: 데이터베이스에서 가져온 것을 ListAdapter로 지원할 수 있다.)
                .setItems(R.array.upload_pic_dialog_title, DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        // 카메라 0
                        0 -> {
                            var permissionResult0 = ContextCompat.checkSelfPermission(requireContext(), CAMERA_PERMISSION[0])
                            Log.d("Signup_Image_Upload_Dialog", "checkSelfPermission$which")

                            when (permissionResult0) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    Log.d("Signup_Image_Upload_Dialog", "PERMISSION_GRANTED$which")
                                    // 카메라 권한이 이미 허용된 상태일 때 바로 카메라 액티비티 호출
                                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    startActivityForResult(intent, CAMERA_PERMISSION_REQUEST)
                                }
                                PackageManager.PERMISSION_DENIED -> {
                                    Log.d("Signup_Image_Upload_Dialog", "PERMISSION_DENIED$which")
                                    // 카메라 권한이 허용된 상태가 아닐 때
                                    // ActivityCompat.requestPermissions(requireActivity(), CAMERA_PERMISSION, CAMERA_PERMISSION_REQUEST)
                                    // Fragment에서 onRequestPermissionsResult 호출하려면 requestPermissions만 쓰기
                                    requestPermissions(CAMERA_PERMISSION, CAMERA_PERMISSION_REQUEST)
                                    // 이 떄 onRequestPermissionsResult 메소드 호출

                                }
                            }

                        }

                        // 카메라 2
                        1 -> {
                            var permissionResult1 = ContextCompat.checkSelfPermission(requireContext(), STORAGE_PERMISSION[0])
                            Log.d("Signup_Image_Upload_Dialog", "checkSelfPermission$which")

                            when (permissionResult1) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    // 카메라, 저장소 권한이 이미 허용된 상태일 때
                                    Log.d("Signup_Image_Upload_Dialog", "PERMISSION_GRANTED$which")
                                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                                    // 촬영한 사진이 저장될 파일 이름
                                    val file_name = "/temp_${System.currentTimeMillis()}.jpg"
                                    // 경로 + 파일 이름
                                    val pic_path = "$file_path/$file_name"
                                    val file = File(pic_path)

                                    // 사진이 저장될 위치를 관리하는 Uri 객체
                                    // val contentUri = Uri(pic_path) // 예전에는 파일명을 기술하면 바로 접근 가능
                                    // -> 현재 안드로이드 OS 6.0 부터는 OS에서 해당 경로를 집어 넣으면 이 경로로 접근할 수 있는지 없는지를 판단. 접근할 수 있으면 Uri 객체를 넘겨줌.
                                    contentUri = FileProvider.getUriForFile(requireContext(), "com.duos.camera.file_provider", file)

                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                                    startActivityForResult(intent, 200)

                                }

                                PackageManager.PERMISSION_DENIED -> {
                                    Log.d("Signup_Image_Upload_Dialog", "PERMISSION_DENIED$which")
                                    // 카메라, 저장소 권한이 거부된 상태일 때
                                    requestPermissions(STORAGE_PERMISSION, STORAGE_PERMISSION_REQUEST)
                                    // which가 0일 때와 똑같이 onRequestPermissionsResults 메서드 호출
//                                    ActivityCompat.requestPermissions(requireActivity(), STORAGE_PERMISSION, STORAGE_PERMISSION_REQUEST)
                                    // 이렇게 작성하면 허용 누르고 바로 카메라가 떠야 되는데 아닌가....
//                                    permissionResult1 = ContextCompat.checkSelfPermission(requireContext(), STORAGE_PERMISSION[0])
//                                    if (permissionResult1 == PackageManager.PERMISSION_GRANTED) {
//                                        Log.d("Signup", "permissionResult 가 넘어갔나?")
//                                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                                        // 촬영한 사진이 저장될 파일 이름
//                                        val file_name = "/temp_${System.currentTimeMillis()}.jpg"
//                                        // 경로 + 파일 이름
//                                        val pic_path = "$file_path/$file_name"
//
//                                        val file = File(pic_path)
//
//                                        // 사진이 저장될 위치를 관리하는 Uri 객체
//                                        // val contentUri = Uri(pic_path) // 예전에는 파일명을 기술하면 바로 접근 가능
//                                        // -> 현재 안드로이드 OS 6.0 부터는 OS에서 해당 경로를 집어 넣으면 이 경로로 접근할 수 있는지 없는지를 판단. 접근할 수 있으면 Uri 객체를 넘겨줌.
//                                        contentUri = FileProvider.getUriForFile(requireContext(), "com.duos.camera.file_provider", file)
//
//                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
//                                        startActivityForResult(intent, 200)
//                                    }
                                }
                            }
                        }
                        // 파일 접근
                        2 -> Log.d("Signup_Image_Upload_Dialog", "파일 접근 $which")

                    }

                })
            dialogBuilder.create().show()


            // 사진 1 ->갤러리에 저장 안함 (용량 작음)
            // 사진 2 -> 갤러리에 저장 함 (용량 큰)
            // 갤러리에서 선택


        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        Log.d("Signup_Image_Upload_Dialog", "OnRequestPermissionsResult 호출댐.")
        val file_path = requireActivity().getExternalFilesDir(null).toString()
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
                    Toast.makeText(requireContext(), "프로필 사진을 업로드하려면 카메라 접근 권한을 허용해야 합니다.", Toast.LENGTH_LONG).show()

                }
            }
            STORAGE_PERMISSION_REQUEST -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 허용을 눌렀을 때 바로 카메라(1번 방식)로
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                    // 촬영한 사진이 저장될 파일 이름
                    val file_name = "/temp_${System.currentTimeMillis()}.jpg"
                    // 경로 + 파일 이름
                    val pic_path = "$file_path/$file_name"
                    val file = File(pic_path)

                    // 사진이 저장될 위치를 관리하는 Uri 객체
                    // val contentUri = Uri(pic_path) // 예전에는 파일명을 기술하면 바로 접근 가능
                    // -> 현재 안드로이드 OS 6.0 부터는 OS에서 해당 경로를 집어 넣으면 이 경로로 접근할 수 있는지 없는지를 판단. 접근할 수 있으면 Uri 객체를 넘겨줌.
                    contentUri = FileProvider.getUriForFile(requireContext(), "com.duos.camera.file_provider", file)

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
                    startActivityForResult(intent, 200)
                } else {
                    Log.d("Signup_Image_Upload_Dialog", "OnRequestPermissionsResult에서 카메라1 권한 거부.")
                    Toast.makeText(requireContext(), "프로필 사진을 업로드하려면 카메라 접근 권한을 허용해야 합니다.", Toast.LENGTH_LONG).show()
                }
            }
        }


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            100 -> {
                if (resultCode == RESULT_OK) {
                    // data : Intent 안에 사진 정보가 들어감
                    val bitmap = data?.getParcelableExtra<Bitmap>("data")
                    binding.signup05ProfileImageBackgroundIv.setImageBitmap(bitmap)
                }
            }
            200 -> {
                if (resultCode == RESULT_OK) {
                    val bitmap = BitmapFactory.decodeFile(contentUri.path)
                    binding.signup05ProfileImageBackgroundIv.setImageBitmap(bitmap)

//                     사진 파일 삭제한다.
//                    val file = File(contentUri.path)
//                    file.delete()
                }
            }
        }
    }

}