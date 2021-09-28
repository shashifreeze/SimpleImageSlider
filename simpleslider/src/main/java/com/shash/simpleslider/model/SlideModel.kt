package com.shash.simpleslider.model

import android.os.Parcelable
import android.widget.ImageView
import kotlinx.parcelize.Parcelize

/**
 *@author = Shashi
 *@date = 03/07/21
 *@description = This class handles
 */
@Parcelize
data class SlideModel(var imagePath:String?, val header:String?=null,  var title:String?=null, var scaleType: ImageView.ScaleType?=null):Parcelable
