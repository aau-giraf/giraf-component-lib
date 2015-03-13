package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * Created by Marhlder on 10-03-2015.
 */
public class GViewPager extends android.support.v4.view.ViewPager {

    public GViewPager(Context context) {
        super(context);
        init();
    }

    public GViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        int glowDrawableId = getContext().getResources().getIdentifier("overscroll_glow", "drawable", "android");
        Drawable androidGlow = getContext().getResources().getDrawable(glowDrawableId);
        androidGlow.setColorFilter(getResources().getColor(R.color.giraf_fading_edge_glow), PorterDuff.Mode.SRC_IN);
    }

}
