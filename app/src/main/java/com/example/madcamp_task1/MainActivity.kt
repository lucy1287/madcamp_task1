package com.example.madcamp_task1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.madcamp_task1.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tabLayout : TabLayout = findViewById(R.id.tab_layout)
        val profileFragment: Fragment = ProfileFragment()
        val galleryFragment: Fragment = GalleryFragment()
//        val mapFragment: Fragment = MapFragment()

        supportFragmentManager.beginTransaction().replace(R.id.main_view, galleryFragment).commit()
        // default tab

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{

            override fun onTabSelected(tab: TabLayout.Tab) {
                when(tab.position) {
                    0 -> {
                        // Profile Tab
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, profileFragment).commit()
                    }
                    1 -> {
                        // Gallery Tab
                        supportFragmentManager.beginTransaction().replace(R.id.main_view, galleryFragment).commit()
                    }
                    2 -> {
                        // Map Tab
//                        supportFragmentManager.beginTransaction().replace(R.id.main_view, mapFragment).commit()
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // No event
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // No event
            }
        })

    }
}