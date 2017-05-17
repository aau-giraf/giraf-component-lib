package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class GDividerVertical extends RelativeLayout {

    public GDividerVertical(Context context)
    {
        super(context);
        SetStyle();
    }

    public GDividerVertical(Context context, AttributeSet attr){
        super(context, attr);
        SetStyle();
    }

    public GDividerVertical(Context context, AttributeSet attr, int defStyle)
    {
        super(context, attr, defStyle);
        SetStyle();
    }

    public void SetStyle()
    {
        this.setBackgroundDrawable(new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, GStyler.dividerBaseColors));
    }

}
