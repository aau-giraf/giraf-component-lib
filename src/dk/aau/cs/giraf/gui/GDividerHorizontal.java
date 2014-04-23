package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import java.util.jar.Attributes;

public class GDividerHorizontal extends RelativeLayout {

    public GDividerHorizontal(Context context)
    {
        super(context);
        SetStyle();
    }

    public GDividerHorizontal(Context context, AttributeSet attr)
    {
        super(context, attr);
        SetStyle();
    }

    public GDividerHorizontal(Context context, AttributeSet attr, int defStyle)
    {
        super(context, attr, defStyle);
        SetStyle();
    }

    public void SetStyle()
    {
        this.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, GStyler.dividerBaseColors));
    }

}
