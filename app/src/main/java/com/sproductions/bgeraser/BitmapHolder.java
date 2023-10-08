package com.sproductions.bgeraser;

import android.graphics.Bitmap;

public class BitmapHolder {
    // Singleton class
    private static BitmapHolder instance;
    private static Bitmap bitmap;

    // Private constructor to prevent external instantiation
    private BitmapHolder() {
        // Initialization code here
    }

    // Static method to provide access to the single instance
    public static BitmapHolder getInstance() {
        if (instance == null) {
            instance = new BitmapHolder();
        }
        return instance;
    }
        public static void setBitmap(Bitmap b) {
            bitmap = b;
        }

        public static Bitmap getBitmap() {
            return bitmap;
        }
}
