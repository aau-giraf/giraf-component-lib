package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * <p></p>
 * @deprecated Old button use {@link dk.aau.cs.giraf.gui.GirafButton} instead. Remember to set the play icon
 */
@Deprecated
public class GButtonPlay extends GButton {

    public GButtonPlay(Context context) {
        super(context);
        setIcon();
    }

    public GButtonPlay(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIcon();
    }

    public GButtonPlay(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setIcon();
    }

    private void setIcon()
    {

        this.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.gplay_icon), null, null, null);
    }
}
