package dk.aau.cs.giraf.utilities;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

import static android.view.View.LAYER_TYPE_HARDWARE;
import static android.view.View.LAYER_TYPE_NONE;

public class GrayScaleHelper {
    public static void setGray(View view, boolean state){
        if(state) {
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0); //Set the color saturation to 0
            Paint grayscalePaint = new Paint();
            grayscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
            // Create a hardware layer with the greyscale paint
            view.setLayerType(LAYER_TYPE_HARDWARE, grayscalePaint);
        }else{
            //Removes the grayscale
            view.setLayerType(LAYER_TYPE_NONE, null);
        }
    }
}
