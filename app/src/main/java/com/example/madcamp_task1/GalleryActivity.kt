package com.example.madcamp_task1

import android.content.ClipData
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.EventAdapter
import com.example.madcamp_task1.adapter.ImageAdapter
import com.example.madcamp_task1.adapter.MediaAdapter
import com.example.madcamp_task1.databinding.ActivityGalleryBinding
import com.example.madcamp_task1.roomdb.*

class GalleryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGalleryBinding

    private lateinit var mediaViewModel: MediaViewModel
    private lateinit var mediaList: LiveData<List<Media>>
    private var eventNo: Long = 0
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
            getMediaFromGallery()
        }

        // 이전 Fragment에서 클릭된 이벤트번호
        eventNo = intent.getLongExtra("eventNo", 1)

        val db = AppDatabase.getInstance(this)
        val mediaDao = db!!.mediaDao()

        val factory = MediaViewModelFactory(mediaDao, eventNo)
        mediaViewModel = ViewModelProvider(this, factory).get(MediaViewModel::class.java)

        mediaList = mediaViewModel.getMediasByEventNo

        mediaList.observe(this, { media ->
            binding.rvImageList.adapter = MediaAdapter(media)
        })

        binding.rvImageList.layoutManager = GridLayoutManager(this, 2)
        binding.rvImageList.adapter = mediaList.value?.let { MediaAdapter(it) }

    }

    private fun getMediaFromGallery(){
        // 갤러리에서 이미지 및 비디오 가져오기
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/* video/*"
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        startActivityForResult(galleryIntent, REQUEST_GALLERY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQUEST_GALLERY -> {
                if (resultCode == RESULT_OK) {
                    if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                        Toast.makeText(this, "미디어를 선택하지 않았습니다.", Toast.LENGTH_LONG).show()
                    }
                    else{
                        if(data.clipData == null){     //이미지 또는 비디오 1개 선택
                            val mediaUri : Uri? = data.data

                            val mediaType = if (mediaUri.toString().contains("image")) MediaType.IMAGE else MediaType.VIDEO

                            val newMedia = mediaUri?.let {
                                Media(
                                    uri = it,
                                    eventNo = eventNo,
                                    latitude = 0.0,
                                    longitude = 0.0,
                                    type = mediaType
                                )
                            }
                            if (newMedia != null) {
                                mediaViewModel.addMedia(newMedia)
                            }
                        }
                        else{      // 이미지를 여러장 선택한 경우
                            val clipData : ClipData = data.clipData!!

                            if(clipData.itemCount > 10){
                                Toast.makeText(this, "사진 및 비디오는 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show()
                            }
                            else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                                for (i in 0 until clipData.itemCount) {
                                    val mediaUri: Uri? = clipData.getItemAt(i).uri
                                    val mediaType = if (mediaUri.toString().contains("image")) MediaType.IMAGE else MediaType.VIDEO
                                    try {
                                        val newMedia = mediaUri?.let {
                                            Media(
                                                uri = it,
                                                eventNo = eventNo,
                                                latitude = 0.0,
                                                longitude = 0.0,
                                                type = mediaType
                                            )
                                        }
                                        if (newMedia != null) {
                                            mediaViewModel.addMedia(newMedia)
                                        }
                                    } catch (e: Exception) {
                                        Log.e(ContentValues.TAG, "File select error", e)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}