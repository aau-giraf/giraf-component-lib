package dk.aau.cs.giraf.utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.view.View;
import android.view.Window;
import dk.aau.cs.giraf.gui.R;

import static android.view.View.LAYER_TYPE_HARDWARE;
import static android.view.View.LAYER_TYPE_NONE;

public class GrayScaleHelper {

    public static void setGrayScaleForActivity(Activity activity, boolean shouldBeGray){
        View screen = activity.getWindow().getDecorView();
        int barId = activity.getResources().getIdentifier("action_bar_container", "id", "android");
        View bar = screen.findViewById(barId);
        View view = activity.findViewById(android.R.id.content);
        setGray(view,shouldBeGray);
        setGray(bar,shouldBeGray);
    }

    private static void setGray(View view, boolean state){
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
