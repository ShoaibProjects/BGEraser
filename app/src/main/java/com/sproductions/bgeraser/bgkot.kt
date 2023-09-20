package com.sproductions.bgeraser;
import android.graphics.Bitmap
import android.net.Uri
import com.slowmac.autobackgroundremover.BackgroundRemover
import com.slowmac.autobackgroundremover.OnBackgroundChangeListener

class bgkot {
    lateinit var bgbitmap : Bitmap;
    fun bgfun (bgbtmp :
               Bitmap){
        BackgroundRemover.bitmapForProcessing(
            bgbtmp,true,
            object: OnBackgroundChangeListener {
                override fun onSuccess(bitmap: Bitmap) {
                    //do what ever you want to do with this bitmap
                    bgbitmap=bitmap;
                }

                override fun onFailed(exception: Exception) {
                    //exception
                }
            }
        )

    }
}