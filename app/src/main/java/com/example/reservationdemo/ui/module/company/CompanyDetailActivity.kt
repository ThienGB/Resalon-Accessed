package com.example.reservationdemo.ui.module.company

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.reservationdemo.R
import com.example.reservationdemo.databinding.ActivityCompanyDetailBinding
import com.example.reservationdemo.ui.custom_property.MotionScrollHandler
import com.google.android.material.tabs.TabLayoutMediator

class CompanyDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCompanyDetailBinding
    private lateinit var scrollHandler: MotionScrollHandler
    private val scrollValue by lazy {
        resources.getDimension(R.dimen.company_scroll_value)
    }
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.statusBarColor = Color.TRANSPARENT
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = true
        binding = ActivityCompanyDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupScrollBehavior()
        setupTabLayout()
    }

    private fun setupScrollBehavior() {
        scrollHandler = MotionScrollHandler(
            lifecycleScope = this.lifecycleScope,
            scrollView = binding.nestedScroll,
            motionLayout = binding.motionLayout,
            headerLayout = binding.headerContainer,
            scrollValue = scrollValue,
            delayTime = 100L
        )
        scrollHandler.setup()
    }

    private fun setupTabLayout() {
       // val tabTitles = listOf("Home", "Introduce", "Reviews", "Jobs", "Salary")
        val tabTitles = listOf("Home", "Introduce", "Reviews")
        val adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = tabTitles.size

            override fun createFragment(position: Int): Fragment {
                return when (position) {
                    0 -> CompanyDetailHomeFragment()
                    1 -> CompanyDetailHomeFragment()
                    2 -> CompanyDetailHomeFragment()
                    else -> throw IllegalArgumentException("Invalid tab position")
                }
            }
        }
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
    override fun onDestroy() {
        super.onDestroy()
    }
}