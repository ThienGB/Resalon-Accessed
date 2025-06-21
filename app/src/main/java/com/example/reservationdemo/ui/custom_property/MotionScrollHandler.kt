package com.example.reservationdemo.ui.custom_property

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.animation.doOnEnd
import androidx.core.widget.NestedScrollView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MotionScrollHandler(
    private val lifecycleScope: CoroutineScope,
    private val scrollView: NestedScrollView,
    private val motionLayout: MotionLayout,
    private val headerLayout: MotionLayout,
    private val scrollValue: Float = 300f,
    private val delayTime: Long = 300L
) {
    private var isTouching = false
    private var isAutoScrolling = false
    private var scrollAnimator: ValueAnimator? = null
    private var scrollDebounceJob: Job? = null

    @SuppressLint("ClickableViewAccessibility")
    fun setup() {
        scrollView.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                    isTouching = true
                    scrollAnimator?.cancel()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    isTouching = false
                    scrollAnimator?.cancel()
                    scrollDebounceJob?.cancel()
                    scrollDebounceJob = lifecycleScope.launch {
                        delay(delayTime)
                        if (!isTouching && !isAutoScrolling) {
                            handleReleaseScroll()
                        }
                    }
                }
            }
            false
        }

        scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            val progress = (scrollY / scrollValue).coerceIn(0.01f, 0.9999f)
            motionLayout.progress = progress
            headerLayout.progress = progress

            if (isAutoScrolling) return@setOnScrollChangeListener

            scrollDebounceJob?.cancel()
            scrollDebounceJob = lifecycleScope.launch {
                delay(delayTime)
                if (!isTouching && !isAutoScrolling) {
                    handleReleaseScroll()
                }
            }
        }
    }

    private fun handleReleaseScroll() {
        val scrollY = scrollView.scrollY
        val maxScroll = scrollValue.toInt()

        when {
            scrollY > maxScroll -> return
            scrollY < maxScroll / 2 -> smoothScrollTo(0)
            else -> smoothScrollTo(maxScroll)
        }
    }

    private fun smoothScrollTo(toY: Int) {
        scrollAnimator?.cancel()
        val fromY = scrollView.scrollY
        isAutoScrolling = true
        scrollAnimator = ValueAnimator.ofInt(fromY, toY).apply {
            duration = 1100
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                val value = it.animatedValue as Int
                scrollView.scrollTo(0, value)
            }
            doOnEnd {
                isAutoScrolling = false
            }
            start()
        }
    }
}
