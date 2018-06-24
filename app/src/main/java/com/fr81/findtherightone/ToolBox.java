package com.fr81.findtherightone;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

/**
 * Toolbox of the app for useful method
 */
public class ToolBox {

    /**
     * Make a pic black and white
     *
     * @param img
     */
    public static void blackAndWhitePic(ImageView img) {
        ColorMatrix matrix;
        matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter;
        filter = new ColorMatrixColorFilter(matrix);
        img.setColorFilter(filter);
    }

}
