package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class GVerifyButton extends GButton {

    private Drawable verifyIcon;
	
	public GVerifyButton(Context context) {
		super(context);
        setVerifyIcon();
	}

	public GVerifyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
        setVerifyIcon();
		
	}

	public GVerifyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        setVerifyIcon();
	}

    /**
     * Sets the icon of the button to be gcancel_icon from the resources
     */
    private void setVerifyIcon()
    {
        verifyIcon = this.getResources().getDrawable(R.drawable.ok_icon);
        this.setCompoundDrawablesWithIntrinsicBounds(verifyIcon, null, null, null);
    }

    /**
     * Overrides onFinishInflates
     * Tests whether a text custom text has been given to the button in XML
     * If not give the default string "Godkend"
     */
    @Override
    public void onFinishInflate() {
        if(this.getText() == "")
        {
            this.setText(R.string.verifyButtonString);
        }
    }
}
