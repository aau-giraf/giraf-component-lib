package dk.aau.cs.giraf.gui;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created by AndersBender on 08-05-14.
 */
public class GWidgetProfileSelection  extends ImageView{

    private Context context;

    public GWidgetProfileSelection(Context context) {
        super(context);
        this.context = context;
        this.setStyle();
    }

    public GWidgetProfileSelection(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.setStyle();
    }

    public GWidgetProfileSelection(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        this.setStyle();
    }

    private void setStyle()
    {
        this.setImageResource(dk.aau.cs.giraf.gui.R.drawable.no_profile_pic);
        GradientDrawable backgroundDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {0xffffffff, 0xffffffff});
        backgroundDrawable.setCornerRadius(5);
        backgroundDrawable.setStroke(2, ColorificationBisimulationRelation.ProportionallyAlterVS(0.9f));
        this.setBackgroundDrawable(backgroundDrawable);
    }
}
