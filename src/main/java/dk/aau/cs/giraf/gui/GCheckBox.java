package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

/**
 * Created by Jelly on 27-03-14.
 */
public class GCheckBox extends CheckBox {

    public GCheckBox(Context context)
    {
        super(context);
        SetStyle();
    }

    public GCheckBox(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        SetStyle();
    }

    public GCheckBox(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        SetStyle();
    }

    private void SetStyle()
    {
        //when GText colors are put in GStyler, use them to set text color for this
    }
}
