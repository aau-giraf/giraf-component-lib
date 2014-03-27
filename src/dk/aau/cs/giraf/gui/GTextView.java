package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by AndersBender on 27-03-14.
 */
public class GTextView extends TextView{

    public GTextView(Context context) {
        super(context);
        this.setStyle();
    }

    public GTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStyle();

    }

    public GTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStyle();
    }

    private void setStyle()
    {
        this.setTypeface(Typeface.SANS_SERIF);
        this.setTextColor(GStyler.textBaseColor);
    }
}
