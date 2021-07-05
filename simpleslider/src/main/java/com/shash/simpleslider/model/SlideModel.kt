package com.shash.simpleslider.model

import android.os.Parcelable
import android.widget.ImageView
import com.example.imagesliderdemo.slider.constants.ScaleTypes
import kotlinx.parcelize.Parcelize

/**
 *@author = Shashi
 *@date = 03/07/21
 *@description = This class handles
 */
@Parcelize
data class SlideModel(var imagePath:String?, var title:String?=null, var scaleType: ScaleTypes?=null):Parcelable
