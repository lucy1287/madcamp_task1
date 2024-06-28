package com.example.madcamp_task1

import android.content.ClipData
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.ImageAdapter
import com.example.madcamp_task1.databinding.ActivityGalleryBinding

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    var images: ArrayList<Uri?> = ArrayList<Uri?>()
    private val REQUEST_CAMERA = 1
    private val REQUEST_GALLERY = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGalleryBinding.inflate(layoutInflater)
        initializeViews()
        setContentView(binding.root)
    }

    private fun initializeViews(){
        binding.btnGallery.setOnClickListener {
            getImageFromGallery()
        }

    }

    private fun getImageFromGallery(){
        //갤러리에서 이미지 가져오기
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
//            REQUEST_CAMERA ->{
//                var imageUri : Uri? = photoURI
//                images.add(imageUri);
//
//                imageAdapter = ImageAdapter(images);
//                binding.rvImageList.adapter = imageAdapter
//                val imageGridLayoutManager = GridLayoutManager(this, 2)
//                imageGridLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
//                binding.rvAccidentImage.layoutManager = reportImageLinearLayoutManager
//            }
            REQUEST_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                        Toast.makeText(this, "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(data.clipData == null){     //이미지 1개 선택
                            Log.e("single choice: ", data.data.toString());
                            var imageUri : Uri? = data.data
                            images.add(imageUri);

                            binding.rvImageList.layoutManager = GridLayoutManager(this, 2)
                            binding.rvImageList.adapter = ImageAdapter(images)
                        }
                        else{      // 이미지를 여러장 선택한 경우
                            var clipData : ClipData = data.clipData!!;

                            if(clipData.itemCount > 10){
                                Toast.makeText(this, "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                            }
                            else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                                Log.e(ContentValues.TAG, "multiple choice");

                                for (i in 0 until clipData.itemCount) {
                                    val imageUri: Uri? = clipData.getItemAt(i).uri
                                    try {
                                        images.add(imageUri)
                                    } catch (e: Exception) {
                                        Log.e(ContentValues.TAG, "File select error", e)
                                    }
                                }
                                Log.d("이미지 리스트", images.toString())
                                binding.rvImageList.layoutManager = GridLayoutManager(this, 2)
                                binding.rvImageList.adapter = ImageAdapter(images)
                            }
                        }
                    }
                }
            }
        }
    }

}