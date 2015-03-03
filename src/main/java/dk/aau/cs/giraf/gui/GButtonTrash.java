package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
/**
 * <p></p>
 * @deprecated Old button use {@link dk.aau.cs.giraf.gui.GirafButton} instead. Remember to set the type to DELETE
 */
@Deprecated
public class GButtonTrash extends GButton {

    public GButtonTrash(Context context) {
        super(context);
        setTrashIcon();
    }

    public GButtonTrash(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTrashIcon();
    }

    public GButtonTrash(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setTrashIcon();
    }

    /**
     * Sets the icon of the button to be gtrash_icon from the resources
     */
    private void setTrashIcon()
    {

        this.setCompoundDrawablesWithIntrinsicBounds(this.getResources().getDrawable(R.drawable.gtrashcan_icon), null, null, null);
    }
}
