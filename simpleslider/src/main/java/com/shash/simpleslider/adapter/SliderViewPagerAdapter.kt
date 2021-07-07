package com.shash.simpleslider.adapter

import android.content.Context
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shash.simpleslider.constants.ActionTypes
import com.example.imagesliderdemo.slider.constants.ScaleTypes
import com.shash.simpleslider.`interface`.SliderItemClickListener
import com.shash.simpleslider.`interface`.TouchListener
import com.shash.simpleslider.databinding.PagerRowBinding
import com.shash.simpleslider.model.SlideModel

/**
 *@author = Shashi
 *@date = 03/07/21
 *@description = This class handles
 */
class SliderViewPagerAdapter(context: Context,
                             private val imageList: List<SlideModel>,
                             private var radius: Int,
                             private var errorImage: Int,
                             private var placeholder: Int,
                             private var titleBackground: Int,
                             private var scaleType: ScaleTypes?,
                             private var textAlign: String) : RecyclerView.Adapter<SliderViewPagerAdapter.ImageViewHolder>() {

    constructor(context: Context, imageList: List<SlideModel>, radius: Int, errorImage: Int, placeholder: Int, titleBackground: Int, textAlign: String) :
            this(context, imageList, radius, errorImage, placeholder, titleBackground, null, textAlign)

    private var sliderItemClickListener: SliderItemClickListener? = null
    private var touchListener: TouchListener? = null

    companion object{
        const val TAG="SliderPagerAdapter"
    }

    inner class ImageViewHolder(private  val binding: PagerRowBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(slideModel: SlideModel)
        {
            //setting scale type
            when(scaleType)
            {
                ScaleTypes.FIT -> Glide.with(binding.root.context)
                    .load(slideModel.imagePath)
                    .fitCenter()
                    .into(binding.imageView)
                ScaleTypes.CENTER_CROP -> Glide.with(binding.root.context)
                    .load(slideModel.imagePath)
                    .centerCrop()
                    .into(binding.imageView)
                ScaleTypes.CENTER_INSIDE -> Glide.with(binding.root.context)
                    .load(slideModel.imagePath)
                    .centerInside()
                    .into(binding.imageView)
                null -> Glide.with(binding.root.context)
                    .load(slideModel.imagePath)
                    .into(binding.imageView)
            }
            //title alignment
            binding.textView.gravity=getGravityFromAlign(textAlign)
            binding.textView.setBackgroundResource(titleBackground)

            //Setting listeners
            binding.imageView.setOnClickListener {
                sliderItemClickListener?.onItemSelected(adapterPosition)
            }

            binding.imageView.setOnTouchListener { v, event ->
                touchListener?.let {
                    when (event.action) {
                        MotionEvent.ACTION_MOVE -> it.onTouched(ActionTypes.MOVE)
                        MotionEvent.ACTION_DOWN -> it.onTouched(ActionTypes.DOWN)
                        MotionEvent.ACTION_UP -> it.onTouched(ActionTypes.UP)
                    }
                }

                false
            }
        }
    }

    /**
     * Get layout gravity value from textAlign variable
     *
     * @param  textAlign  text align by user
     */
    fun getGravityFromAlign(textAlign: String): Int {
        return when (textAlign) {
            "END" -> {
                Gravity.END
            }
            "CENTER" -> {
                Gravity.CENTER
            }
            else -> {
                Gravity.START
            }
        }
    }

    /**
     * Set item click listener
     *
     * @param  sliderItemClickListener  callback by user
     */
    fun setItemClickListener(sliderItemClickListener: SliderItemClickListener) {
        this.sliderItemClickListener = sliderItemClickListener
    }

    /**
     * Set touch listener for listen to image touch
     *
     * @param  touchListener  interface callback
     */
    fun setTouchListener(touchListener: TouchListener) {
        this.touchListener = touchListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
            val binding = PagerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
             return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount()= imageList.size

}