package com.example.madcamp_task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.madcamp_task1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (findViewById<View>(R.id.fragment_gallery) != null) {
            if (savedInstanceState == null) {
                // GalleryFragment가 이미 추가되었는지 확인
                if (supportFragmentManager.findFragmentById(R.id.fragment_gallery) == null) {
                    val galleryFragment = GalleryFragment()

                    val fragmentManager: FragmentManager = supportFragmentManager
                    val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
                    fragmentTransaction.add(R.id.fragment_gallery, galleryFragment)
                    fragmentTransaction.commit()
                }
            }
        }
    }
}