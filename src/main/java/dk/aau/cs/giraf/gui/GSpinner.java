package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * <p></p>
 * @deprecated Old spinner {@link dk.aau.cs.giraf.gui.GirafSpinner} instead.
 */
public class GSpinner extends Spinner {

    public GSpinner(Context context)
    {
        super(context);
        SetStyle();
    }

    public GSpinner(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        SetStyle();
    }

    public GSpinner(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        SetStyle();
    }

    public GSpinner(Context context, int mode)
    {
        super(context, mode);
        SetStyle();
    }

    public GSpinner(Context context, AttributeSet attrs, int defStyle, int mode)
    {
        super(context, attrs, defStyle, mode);
        SetStyle();
    }

    private void SetStyle()
    {
        int[] colors = GStyler.getColors(GStyler.spinnerBaseColor);
        GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        gd.setCornerRadius(10);
        gd.setStroke(3, GStyler.calculateGradientColor(colors[0], 0.75f));

        this.setBackgroundDrawable(gd);

        this.setPadding(GStyler.dpToPixel(10, this.getContext()),
                        GStyler.dpToPixel(5, this.getContext()),
                        GStyler.dpToPixel(10, this.getContext()),
                        GStyler.dpToPixel(5, this.getContext()));
    }
}
