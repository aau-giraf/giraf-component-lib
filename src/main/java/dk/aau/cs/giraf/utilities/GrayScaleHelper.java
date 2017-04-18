package dk.aau.cs.giraf.utilities;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;

import static android.view.View.LAYER_TYPE_HARDWARE;

/**
 * Created by Christoffer D. Mouritzen on 12-04-2017.
 */
public class GrayScaleHelper {
    public static void setGrey(View view, boolean state){
        ColorMatrix cm = new ColorMatrix();
        if(state) {
            cm.setSaturation(0);
        }
        else{
            cm.setSaturation(100);
        }
        Paint greyscalePaint = new Paint();
        greyscalePaint.setColorFilter(new ColorMatrixColorFilter(cm));
        // Create a hardware layer with the greyscale paint
        view.setLayerType(LAYER_TYPE_HARDWARE, greyscalePaint);
    }
}
