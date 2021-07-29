package com.shash.simpleimageslider

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.imagesliderdemo.slider.constants.ScaleTypes
import com.shash.simpleslider.`interface`.SliderItemClickListener
import com.shash.simpleslider.`interface`.TouchListener
import com.shash.simpleslider.constants.ActionTypes
import com.shash.simpleslider.model.SlideModel
import com.shash.simpleslider.view.SimpleSliderView

class MainActivity : AppCompatActivity() {
    lateinit var sliderView: SimpleSliderView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val list2 = arrayListOf(
            SlideModel(
                "https://www.bigbasket.com/media/uploads/banner_images/L1-NNP11224-1200x300-5thmar.jpg",
                "one"
            ),
            SlideModel(
                "https://www.bigbasket.com/media/customPage/867fc27d-be52-4858-8957-fd28a25b07ba/439c2958-4cbe-4c19-a6be-f98c496742bc/4bea1631-316f-4c04-bcf6-8c91cdbb8158/cheese--1130X400-16thoct.jpg",
                "two"
            ),
            SlideModel(
                "https://www.bigbasket.com/media/uploads/banner_images/L1-CXNP786-1200x300-29thapr.jpg",
                "three"
            ),
            SlideModel(
                "https://www.bigbasket.com/media/uploads/banner_images/hp_cm_460_050721_Bangalore.jpeg",
                "four"
            )
        )

        sliderView = findViewById<SimpleSliderView>(R.id.slider).also {
            it.setImageList(list2,scale = ScaleTypes.FIT)
        }

        val itemClickListener = object : SliderItemClickListener {
            override fun onItemSelected(position: Int) {
                Toast.makeText(
                    this@MainActivity,
                    list2[position].title,
                    Toast.LENGTH_SHORT
                ).show()

            }
        }
        val itemTouchListener = object : TouchListener {
            override fun onTouched(touched: ActionTypes) {
                when (touched) {

                    ActionTypes.DOWN -> {
                        Toast.makeText(
                            this@MainActivity,
                            "down",
                            Toast.LENGTH_SHORT
                        ).show()
                        sliderView.stopSliding()
                    }
                    ActionTypes.UP -> {
                        Toast.makeText(
                            this@MainActivity,
                            "up",
                            Toast.LENGTH_SHORT
                        ).show()
                        sliderView.resumeSlider()
                    }
                    else -> {}
                }

            }
        }
        sliderView.setSliderItemClickListener(itemClickListener)

        // sliderView.setTouchListener(itemTouchListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderView.destroyView()
    }

    override fun onPause() {
        super.onPause()
        sliderView.pauseSlider()
    }

    override fun onResume() {
        super.onResume()
        sliderView.resumeSlider()
    }
}