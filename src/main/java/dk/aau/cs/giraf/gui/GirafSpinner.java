package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import dk.aau.cs.giraf.utilities.GirafScalingUtilities;

/**
 * Created on 26/03/15.
 */
public class GirafSpinner extends Spinner {

    public GirafSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeSpinner(attrs);
    }

    private void initializeSpinner(AttributeSet attrs) {
        this.setBackgroundResource(R.drawable.giraf_button_background);
        this.setPadding(0,0,0,0);
        this.setGravity(Gravity.CENTER);
    }

}
