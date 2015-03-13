package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;

/**
 * <p></p>
 * @deprecated Old button use {@link dk.aau.cs.giraf.gui.GirafButton} instead. Remember to set the search icon
 */
@Deprecated
public class GButtonSettings extends GButton {

    public GButtonSettings(Context context) {
        super(context);
        setIcon();
    }

    public GButtonSettings(Context context, AttributeSet attrs) {
        super(context, attrs);
        setIcon();
    }

    public GButtonSettings(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setIcon();
    }

    private void setIcon()
    {

        this.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.gsettings_icon), null, null, null);
    }
}
