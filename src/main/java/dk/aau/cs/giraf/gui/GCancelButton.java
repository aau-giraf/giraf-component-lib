package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public final class GCancelButton extends GButton {
    private Drawable cancelIcon;

	public GCancelButton(Context context) {
		super(context);
        setCancelIcon();
	}

	public GCancelButton(Context context, AttributeSet attrs) {
		super(context, attrs);
        setCancelIcon();
    }


	public GCancelButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCancelIcon();
	}


    /**
     * Sets the icon of the button to be gcancel_icon from the resources
     */
    private void setCancelIcon()
    {
        cancelIcon = this.getResources().getDrawable(R.drawable.gcancel_icon);
        this.setCompoundDrawablesWithIntrinsicBounds(cancelIcon, null, null, null);
    }

    /**
     * Overrides onFinishInflates
     * Tests whether a text custom text has been given to the button in XML
     * If not give the default string "Fortryd"
     */
    @Override
    public void onFinishInflate() {
        if(this.getText() == "")
        {
            this.setText(R.string.cancelButtonString);
        }
    }
}
