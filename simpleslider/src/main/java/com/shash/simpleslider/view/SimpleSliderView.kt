package com.shash.simpleslider.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.shash.simpleslider.*
import com.shash.simpleslider.`interface`.ItemChangeListener
import com.shash.simpleslider.`interface`.SliderItemClickListener
import com.shash.simpleslider.`interface`.TouchListener
import com.shash.simpleslider.adapter.SliderViewPagerAdapter
import com.shash.simpleslider.constants.ActionTypes
import com.shash.simpleslider.model.SlideModel
import java.util.*

/**
 *@author = Shashi
 *@date = 03/07/21
 *@description = Custom slider view based on viewpager2
 */
class SimpleSliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    RelativeLayout(context, attrs, defStyleAttr) {

    private var viewPager: ViewPager2? = null
    private var pagerDotsLayout: LinearLayout? = null
    private var sliderViewPagerAdapter: SliderViewPagerAdapter? = null
    private var pagerDotsArray: Array<TextView?>? = null
    private var currentPage = 0
    private var imageCount = 0
    private var cornerRadius: Int = 0
    private var period: Long = 0
    private var delay: Long = 0
    private var autoCycle = false
    private var selectedDotColor = 0
    private var unselectedDotColor = 0
    private var errorImage = 0
    private var placeholder = 0
    private var titleBackground = 0
    private var headerBackground = 0
    private var titleTextColor:Int=0
    private var headerTextColor:Int=0
    private var textAlign = "LEFT"
    private var indicatorAlign = "CENTER"
    private var swipeTimer = Timer()
    private var itemChangeListener: ItemChangeListener? = null
    private var touchListener: TouchListener? = null
    private var isTouchMoving :Boolean = false

    private var onImageChangeCallback: ViewPager2.OnPageChangeCallback =
        object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                addDotView(position)
                super.onPageSelected(position)
            }
        }

    init {
        LayoutInflater.from(getContext()).inflate(R.layout.image_slider, this, true)
        viewPager = findViewById(R.id.view_pager)
        pagerDotsLayout = findViewById(R.id.pager_dots)

        val typedArray = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SimpleSlider,
            defStyleAttr,
            defStyleAttr
        )

        cornerRadius = typedArray.getInt(R.styleable.SimpleSlider_ss_corner_radius, 1)
        period = typedArray.getInt(R.styleable.SimpleSlider_ss_period, 1000).toLong()
        delay = typedArray.getInt(R.styleable.SimpleSlider_ss_delay, 1000).toLong()
        autoCycle = typedArray.getBoolean(R.styleable.SimpleSlider_ss_auto_slide, false)
        placeholder =
            typedArray.getResourceId(R.styleable.SimpleSlider_ss_placeholder, R.drawable.loading)
        errorImage =
            typedArray.getResourceId(R.styleable.SimpleSlider_ss_error_image, R.drawable.error)
        selectedDotColor = typedArray.getResourceId(
            R.styleable.SimpleSlider_ss_selected_dot_color,
            R.color.black
        )
        unselectedDotColor = typedArray.getResourceId(
            R.styleable.SimpleSlider_ss_unselected_dot_color,
            R.color.white
        )
        titleBackground = typedArray.getResourceId(
            R.styleable.SimpleSlider_ss_title_background,
            R.drawable.gradient
        )

        headerBackground = typedArray.getResourceId(
            R.styleable.SimpleSlider_ss_header_background,
            R.drawable.gradient
        )

        headerTextColor = typedArray.getColor(R.styleable.SimpleSlider_ss_header_text_color, Color.WHITE)

        titleTextColor = typedArray.getColor(R.styleable.SimpleSlider_ss_title_text_color, Color.WHITE)

        typedArray.getString(R.styleable.SimpleSlider_ss_text_align)?.let {
            textAlign = it
        }

        typedArray.getString(R.styleable.SimpleSlider_ss_indicator_align)?.let {
            indicatorAlign = it
        }

        touchListener?.let {

            viewPager?.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        touchListener!!.onTouched(ActionTypes.MOVE)
                        isTouchMoving = true
                    }
                    MotionEvent.ACTION_DOWN -> {
                        touchListener!!.onTouched(ActionTypes.DOWN)
                    }
                    MotionEvent.ACTION_UP -> {
                        touchListener!!.onTouched(ActionTypes.UP)
                        isTouchMoving =false
                    }
                }
                false
            }
        }
        //register for page change callback
        onImageChangeCallback.let {
            viewPager?.registerOnPageChangeCallback(it)
        }
    }

    fun destroyView() {
        onImageChangeCallback.let {
            viewPager?.unregisterOnPageChangeCallback(it)
        }

        //stopTimer
        stopSliding()
    }

    /**
     * Pause the timer when the onPause lifecycle is triggered
     */
    fun pauseSlider()
    {
        stopSliding()
    }

    /**
     * Start the slider when onResume method is called
     */
    fun resumeSlider(){
        if (autoCycle) startSliding()
    }

    /**
     * Set image list to adapter.
     *
     * @param  imageList  the image list by user
     */
    fun setImageList(imageList: List<SlideModel>,scale: ImageView.ScaleType? = null) {

        sliderViewPagerAdapter =
            SliderViewPagerAdapter(
                context,
                imageList,
                cornerRadius,
                errorImage,
                placeholder,
                titleBackground,
                headerBackground,
                headerTextColor = headerTextColor,
                titleTextColor = titleTextColor,
                scale,
                textAlign,
            )
        imageCount = imageList.size
        currentPage=0
        viewPager?.adapter = sliderViewPagerAdapter
        addDotView(currentPage)
        if (imageList.isNotEmpty()) {
            //setupDots(imageList.size)
            if (autoCycle) {
                stopSliding()
                startSliding()
            }
        }
    }

    /**
     * Adds dot to the bottom of the layout
     */
    private fun addDotView(currentPage: Int) {
        pagerDotsLayout?.removeAllViews()
        pagerDotsLayout?.gravity = getGravityFromAlign(indicatorAlign)
        pagerDotsArray = arrayOfNulls(imageCount)
        pagerDotsArray?.let { dots ->


            for (i in 0 until imageCount) {

                dots[i] = TextView(context)
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.N) {
                    dots[i]?.text = Html.fromHtml("&#8226", Html.FROM_HTML_MODE_LEGACY)
                } else {
                    dots[i]?.text = Html.fromHtml("&#8226")
                }

                dots[i]?.apply {
                    textSize = 28f
                    setTextColor(ContextCompat.getColor(context,unselectedDotColor))
                }

                pagerDotsLayout?.addView(dots[i])
            }

            if (dots.isNotEmpty()) {
                dots[currentPage]?.setTextColor(ContextCompat.getColor(context,selectedDotColor))
                dots[currentPage]?.textSize=35f
                Log.d("adsfafsasdf","${pagerDotsLayout==null} shashi")
            }
        }
    }
    /**
     * Start image sliding.
     *
     * @param  changeablePeriod  optional period value
     */
    fun startSliding(changeablePeriod: Long = period) {
        stopSliding()
        scheduleTimer(changeablePeriod)
    }

    /**
     * Stop image sliding.
     *
     */
    fun stopSliding() {
        swipeTimer.cancel()
        swipeTimer.purge()
    }

    private fun scheduleTimer(period: Long) {

        val handler = Handler(Looper.getMainLooper())

        val update = Runnable {
            if (currentPage == imageCount) {
                currentPage = 0
            }
            viewPager?.setCurrentItem(currentPage++, true)
        }
        swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, delay,period)
    }

    /**
     * Set item click listener for listen to image click
     *
     * @param  itemClickListener  interface callback
     */
    fun setSliderItemClickListener(itemClickListener: SliderItemClickListener) {
        sliderViewPagerAdapter?.setItemClickListener(itemClickListener)
    }

    /**
     * Set item change listener for listen to image click
     *
     * @param  itemChangeListener  interface callback
     */
    fun setSliderItemChangeListener(itemChangeListener: ItemChangeListener) {
        this.itemChangeListener = itemChangeListener
    }

    /**
     * Set touch listener for listen to image touch
     *
     * @param  touchListener  interface callback
     */
    fun setTouchListener(touchListener: TouchListener) {
        this.touchListener = touchListener
        this.sliderViewPagerAdapter?.setTouchListener(touchListener)
    }

    /**
     * Get layout gravity value from indicatorAlign variable
     *
     * @param  indicatorAlign  indicator align by user
     */
    private fun getGravityFromAlign(textAlign: String): Int {
        return when (textAlign) {
            "END" -> {
                Gravity.END
            }
            "START" -> {
                Gravity.START
            }
            else -> {
                Gravity.CENTER
            }
        }
    }
}