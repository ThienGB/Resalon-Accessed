package com.example.reservationdemo.ui.module.company

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.lifecycleScope
import com.example.reservationdemo.R
import com.example.reservationdemo.databinding.ActivityCompanyDetailBinding
import com.example.reservationdemo.ui.custom_property.MotionScrollHandler

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
    }

    fun setupScrollBehavior() {
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
    override fun onDestroy() {
        super.onDestroy()
    }
}