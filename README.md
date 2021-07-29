# SimpleImageSlider
ImageSlider based on viewpager2 

Usage:
Add to your Activity/Fragment layout file
```XML
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".demo.Demo2">

    <com.example.imagesliderdemo.slider.SliderView
        android:layout_width="match_parent"
        android:layout_height="150dp"
        android:id="@+id/slider"
        app:is_delay="1000"
        app:is_auto_cycle="true"
        app:is_period="1000"
        app:is_selected_dot="@android:color/holo_red_dark"
        app:is_unselected_dot="@color/yellow"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```          
In Java or Kotlin File.

```kotlin
    
    //List of SlideModel class that we will set to SliderView
    val list = arrayListOf(
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
        //Finding siderview by id
        sliderView = findViewById<SliderView>(R.id.slider).also {
            //setting slide model list to slider view using below id 
            it.setImageList(list2)
        }
        //Implement SliderItemClickListener where we get onItem selected method
        val itemClickListener = object : SliderItemClickListener {
            
            //This method will be called when someone clicks on any slider item
            override fun onItemSelected(position: Int) {

                    Toast.makeText(
                        this@Demo2,
                        list2[position].title,
                        Toast.LENGTH_SHORT
                    ).show()

            }
        }
        //Set listener to sliderview
        sliderView.setSliderItemClickListener(itemClickListener)
    ```   
