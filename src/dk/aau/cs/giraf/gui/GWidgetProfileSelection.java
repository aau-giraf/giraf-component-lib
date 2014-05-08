package dk.aau.cs.giraf.gui;


import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created by AndersBender on 08-05-14.
 */
public class GWidgetProfileSelection  extends ImageView{


    public GWidgetProfileSelection(Context context) {
        super(context);
        this.setStyle();
    }

    public GWidgetProfileSelection(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStyle();
    }

    public GWidgetProfileSelection(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStyle();
    }

    private void setStyle()
    {
        this.setImageResource(dk.aau.cs.giraf.gui.R.drawable.no_profile_pic);
        this.setPadding(2,2,2,2);
        this.setBackgroundColor(Color.GRAY);
    }
}
