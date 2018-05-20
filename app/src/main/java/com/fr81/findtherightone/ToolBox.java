package com.fr81.findtherightone;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

public class ToolBox {

    public static void blackAndWhitePic(ImageView img){
        ColorMatrix matrix;
        matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter;
        filter = new ColorMatrixColorFilter(matrix);
        img.setColorFilter(filter);
    }

}
