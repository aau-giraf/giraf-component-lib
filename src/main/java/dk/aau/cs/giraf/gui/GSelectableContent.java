package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Malakahh on 5/1/14.
 */
@Deprecated
public class GSelectableContent extends RelativeLayout {
    private GradientDrawable d;

    public GSelectableContent(Context context)
    {
        super(context);
        SetStyle();
    }

    public GSelectableContent(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        SetStyle();
    }

    public GSelectableContent(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        SetStyle();
    }

    public void SetSelected(boolean selected)
    {
        if (selected)
            this.setBackgroundDrawable(d);
        else
            this.setBackgroundDrawable(null);
    }

    private void SetStyle()
    {
        d = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{0x00000000,0x00000000});
        d.setStroke(3, GStyler.selectColor, 10, 10);
        this.setPadding(4,4,4,4);
    }
}
