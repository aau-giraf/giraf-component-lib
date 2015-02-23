package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by AndersBender on 13-05-14.
 */
public class GButtonSearch extends GButton {

    public GButtonSearch(Context context) {
        super(context);
        setIcon();
    }

    public GButtonSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIcon();
    }

    public GButtonSearch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setIcon();
    }

    private void setIcon()
    {
        this.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.magnifying_glass), null, null, null);
    }
}
