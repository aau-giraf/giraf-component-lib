package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Spinner;

/**
 * Created on 26/03/15.
 */
public class GirafSpinner extends Spinner {

    public GirafSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeSpinner(attrs);
    }

    private void initializeSpinner(AttributeSet attrs) {
        this.setBackgroundResource(R.drawable.giraf_button_background); //This is where it is required to change to giraf_spinner_background when it is completed.
        this.setPadding(0,0,0,0);
        this.setGravity(Gravity.CENTER);
    }

}
