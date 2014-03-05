package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class GButton extends Button {

    protected Drawable icon = null;

    /**
     *  Button with both an image icon and text.     *
     *
     * @param buttonText    Text to display on button.
     * @param buttonIcon    Icon to display on button.
     * @param context       Interface to global information about an application environment.
     * @param attrs         A collection of attributes, as found associated with a tag in an XML document.
     */
    public GButton(String buttonText, Drawable buttonIcon, Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setStyle();

        if (buttonIcon != null)
        {
            this.icon = buttonIcon;
            this.setIconSpace();
        }
    }

    /**
     *  Button with text.     *
     *
     * @param buttonText    Text to display on button.
     * @param context       Interface to global information about an application environment.
     */
    public GButton(String buttonText, Context context) {
        this(buttonText, context, null);
    }

    /**
     *  Button with text.     *
     *
     * @param buttonText    Text to display on button.
     * @param context       Interface to global information about an application environment.
     * @param attrs         A collection of attributes, as found associated with a tag in an XML document.
     */
    public GButton(String buttonText, Context context, AttributeSet attrs) {
        this(buttonText, null, context, attrs);
    }

    /**
     *  Button with an image icon.     *
     *
     * @param buttonIcon    Icon to display on button.
     * @param context       Interface to global information about an application environment.
     */
    public GButton(Drawable buttonIcon, Context context) {
        this(buttonIcon, context, null);
    }

    /**
     *  Button with an image icon.     *
     *
     * @param buttonIcon    Icon to display on button.
     * @param context       Interface to global information about an application environment.
     * @param attrs         A collection of attributes, as found associated with a tag in an XML document.
     */
    public GButton(Drawable buttonIcon, Context context, AttributeSet attrs) {
        this(null, buttonIcon, context, attrs);
    }

    /**
     *  Button with both an image icon and text.     *
     *
     * @param buttonText    Text to display on button.
     * @param buttonIcon    Icon to display on button.
     * @param context       Interface to global information about an application environment.
     */
    public GButton(String buttonText, Drawable buttonIcon, Context context) {
        this(buttonText, buttonIcon, context, null);
    }

    /**
     * Resizes the icon to fit the button.
     * @param icon      The icon. Obviously.
     * @param height    The dimensions to resize to.
     * @return          New drawable that is the resized icon.
     */
    private Drawable resizeIcon(Drawable icon, int height){
        Bitmap tempIcon = ((BitmapDrawable)icon).getBitmap();

        final int oldWidth = tempIcon.getWidth();
        final int oldHeight = tempIcon.getHeight();

        float scale = ((float) height) / ((float)tempIcon.getHeight());

        final Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);

        tempIcon = Bitmap.createBitmap(tempIcon, 0, 0, oldWidth, oldHeight, matrix, true);

        return new BitmapDrawable(tempIcon);
    }

    /**
     * Sets style/theme of the button.
     */
    private void setStyle() {
        this.setBackgroundResource(R.drawable.gbutton);
        this.setTextColor(Color.parseColor("#9E6435"));

    }

    /**
     * Makes room for the icon if there is any.
     */
    private void setIconSpace(){
        int trueHeight = this.getHeight() - (this.getPaddingTop()+this.getPaddingBottom());
        this.setCompoundDrawablesWithIntrinsicBounds(resizeIcon(icon, trueHeight), null, null, null);
    }
}
