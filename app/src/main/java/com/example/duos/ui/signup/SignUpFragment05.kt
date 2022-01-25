package com.example.duos.ui.signup

import android.Manifest
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

    val STORAGE_PERMISSION = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    val STORAGE_PERMISSION_REQUEST = 200


    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "05"

//        val file_path = requireActivity().getExternalFilesDir(null).toString()
        val file_path = requireActivity().getExternalFilesDir(null).toString()

        binding.signup05ProfileImageBackgroundIv.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(activity)
            dialogBuilder.setTitle(R.string.upload_pic_dialog_title)
                // setItems 대신 setAdapter()를 사용하여 목록을 지정할 수 있다.
                // 이렇게 하면 동적 데이터가 있는 목록(예: 데이터베이스에서 가져온 것을 ListAdapter로 지원할 수 있다.)
                .setItems(R.array.upload_pic_dialog_title, DialogInterface.OnClickListener { dialog, which ->
                    when (which) {
                        // 카메라 1
                        0 -> {
                            var permissionResult0 = ContextCompat.checkSelfPermission(requireContext(), CAMERA_PERMISSION[0])
                            Log.d("Signup_Image_Upload_Dialog", "checkSelfPermission$which")

                            when (permissionResult0) {
                                PackageManager.PERMISSION_GRANTED -> {
                                    Log.d("Signup_Image_Upload_Dialog", "PERMISSION_GRANTED$which")
                                    Toast.makeText(requireContext(), "카메라 접근 권한이 허용되었습니다.", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                    startActivityForResult(intent, CAMERA_PERMISSION_REQUEST)
                                }
                                PackageManager.PERMISSION_DENIED -> {
                                    Log.d("Signup_Image_Upload_Dialog", "PERMISSION_DENIED$which")
                                    Toast.makeText(requireContext(), "현재 카메라 접근 권한이 허용되지 않았습니다.", Toast.LENGTH_SHORT).show()
                                    ActivityCompat.requestPermissions(requireActivity(), CAMERA_PERMISSION, CAMERA_PERMISSION_REQUEST)
                                    // 이렇게 작성하면 허용 누르고 바로 카메라가 떠야 되는데 아닌가....
                                    permissionResult0 = ContextCompat.checkSelfPermission(requireContext(), CAMERA_PERMISSION[0])
                                    if (permissionResult0 == PackageManager.PERMISSION_GRANTED) {
                                        Log.d("Signup", "permissionResult 가 넘어갔나?")
                                        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                        startActivityForResult(intent, CAMERA_PERMISSION_REQUEST)
                                    }

                                }
                            }

                        }

                        // 카메라 2
                        1 -> {
                            Log.d("Signup_Image_Upload_Dialog", "카메라2 $which")
                            var permissionResult1 = ContextCompat.checkSelfPermission(requireContext(), STORAGE_PERMISSION[0])
                            Log.d("Signup_Image_Upload_Dialog", "checkSelfPermission$which")

                            when (permissionResult1) {
                                PackageManager.PERMISSION_GRANTED -> {
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
                                    ActivityCompat.requestPermissions(requireActivity(), STORAGE_PERMISSION, STORAGE_PERMISSION_REQUEST)
                                    // 이렇게 작성하면 허용 누르고 바로 카메라가 떠야 되는데 아닌가....
                                    permissionResult1 = ContextCompat.checkSelfPermission(requireContext(), STORAGE_PERMISSION[0])
                                    if (permissionResult1 == PackageManager.PERMISSION_GRANTED) {
                                        Log.d("Signup", "permissionResult 가 넘어갔나?")
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
                                }
                            }


//                            val intent1 = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                            // 촬영한 사진이 저장될 파일 이름
//                            val file_name = "/temp_${System.currentTimeMillis()}.jpg"
//                            // 경로 + 파일 이름
//                            val pic_path = "$file_path/$file_name"
//                            val file = File(pic_path)
//
//                            contentUri = FileProvider.getUriForFile(requireContext(), "com.duos.camera.file_provider", file)
//
//                            intent1.putExtra(MediaStore.EXTRA_OUTPUT, contentUri)
//                            startActivityForResult(intent1, 200)
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


//         data : Intent 에 사진 정보


//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//            super.onActivityResult(requestCode, resultCode, data)
//
//            when (requestCode) {
//                100 -> {
//                    if (resultCode == RESULT_OK) {
//                        //data : Intent 안에 사진 정보가 들어감.
//                        val bitmap = data?.getParcelableExtra<Bitmap>("data")
//                        binding.signup05ProfileImageBackgroundIv.setImageBitmap(bitmap)
//
//                    }
//                }


//            200 -> {
//                if (resultCode == RESULT_OK) {
//                    val bitmap = BitmapFactory.decodeFile(contentUri.path)
//                    binding.signup05ProfileImageBackgroundIv.setImageBitmap(bitmap)
//
//                    // 사진 파일 삭제 코드
////                    val file = File(contentUri.path)
////                    file.delete()
//                }
//            }


}