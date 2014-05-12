package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Jelly on 12-05-14.
 */
public class GEditText extends EditText {


    public GEditText(Context context) {
        super(context);
        SetStyle();
    }

    public GEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        SetStyle();
    }

    public GEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        SetStyle();
    }

    private void SetStyle()
    {
        this.setTextColor(GStyler.textBaseColor);
    }
}
