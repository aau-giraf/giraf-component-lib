package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created on 11/05/2015.
 */
public class GirafButtonNew extends LinearLayout implements Checkable {

    // Views used in the layout
    private TextView textView;
    private ImageView iconView;
    private View inflatedView;

    // The content of the button itself
    private Drawable buttonIcon;
    private String buttonText;
    private boolean buttonIsEnabled;
    private boolean buttonScaleText;

    // Distances, padding and margins
    private float buttonPadding;

    // Used in the checkable methods to indicate the checked status of the button
    private boolean isChecked = false;

    // Event listeners that will be used
    private OnClickListener onDisabledClickCallBack;

    /*
     * Constructors to be used from XML
     */

    public GirafButtonNew(Context context, AttributeSet attrs) {
        super(context, attrs);

        // If attributes given in xml use them if the one given in the constructor is not set
        if (attrs != null) {
            // TODO: Add comment
            TypedArray girafButtonAttributes = getContext().obtainStyledAttributes(attrs, R.styleable.GirafButton);

            // Find the icon and text attributes
            buttonIcon = girafButtonAttributes.getDrawable(R.styleable.GirafButton_icon);
            buttonText = girafButtonAttributes.getString(R.styleable.GirafButton_text);
            buttonIsEnabled = girafButtonAttributes.getBoolean(R.styleable.GirafButton_enabled, true);
            buttonScaleText = girafButtonAttributes.getBoolean(R.styleable.GirafButton_scale_text, true);

            // Recycle the attributes
            girafButtonAttributes.recycle();
        }
        else {
            throw new IllegalArgumentException("Please provide properties for your button");
        }

        // Initialize the button
        initialize(context, buttonIcon, buttonText);
    }

    /*
     * Constructors to be used in java code
     */

    public GirafButtonNew(Context context, Drawable icon) {
        this(context, icon, null);
    }

    public GirafButtonNew(Context context, String text) {
        this(context, text, null);
    }

    public GirafButtonNew(Context context, String text, Drawable icon) {
        this(context, icon, text);
    }

    public GirafButtonNew(Context context, Drawable icon, String text) {
        super(context);

        // Set the local properties
        this.buttonText = text;
        this.buttonIcon = icon;

        // Initialize the button
        initialize(context, icon, text);
    }

    private void initialize(final Context context, final Drawable icon, final String text) {
        // Either icon or text must be specified
        if(icon == null && (text == null || text.equals(""))) {
            throw new IllegalArgumentException("You must specify either icon or text on a GirafButton");
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflatedView = inflater.inflate(R.layout.giraf_button, this);

        // Find views used in the layout
        textView = (TextView) this.findViewById(R.id.text_view);
        iconView = (ImageView) this.findViewById(R.id.icon_view);

        // Set the text and icon in the view
        setText(text);
        setIcon(icon);
        setEnabled(buttonIsEnabled);

        // Find the dimens used
        buttonPadding = context.getResources().getDimension(R.dimen.giraf_button_padding);

        updateMargin();
    }

    //<editor-fold desc="hide and show text and icon methods">

    public void setText(String text) {
        if(text == null || text.equals("")) {
            hideText();
        }
        else {
            showText();
        }

        textView.setText(text);

        textView.layout(10, 10, 10, 10);
    }

    public void hideText() {
        if(textView.getVisibility() == GONE) return;

        textView.setVisibility(GONE);
        updateMargin();
    }

    public void showText() {
        if(textView.getVisibility() == VISIBLE) return;

        textView.setVisibility(VISIBLE);
        updateMargin();
    }

    public void setIcon(Drawable icon) {
        if(icon == null) {
            hideIcon();
        }
        else {
            showIcon();
        }

        iconView.setImageDrawable(icon);
    }

    public void hideIcon() {
        if(iconView.getVisibility() == GONE) return;

        iconView.setVisibility(GONE);
        updateMargin();
    }

    public void showIcon() {
        if(iconView.getVisibility() == VISIBLE) return;

        iconView.setVisibility(VISIBLE);
        updateMargin();
    }

    //</editor-fold>

    private void updateMargin() {
        if(iconView.getVisibility() == VISIBLE && textView.getVisibility() == VISIBLE) {
            // Reset margin on text
            LayoutParams textViewParams = (LayoutParams) textView.getLayoutParams();
            textViewParams.setMargins((int) buttonPadding, 0, 0, 0);
            textView.setLayoutParams(textViewParams);
        }
        else {
            // Set margin on text
            LayoutParams textViewParams = (LayoutParams) textView.getLayoutParams();
            textViewParams.setMargins(0, 0, 0, 0);
            textView.setLayoutParams(textViewParams);
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        if(!enabled) {
            // This is 0x59 in hex (35% opaque)
            iconView.setAlpha(0.65F);
            textView.setAlpha(0.65F);
            this.setBackgroundResource(R.drawable.giraf_button_background_disabled);
        }
        else {
            // Fully opaque
            iconView.setAlpha(1F);
            textView.setAlpha(1F);
            this.setBackgroundResource(R.drawable.giraf_button_background);
        }
    }

    @Override
    public final boolean onTouchEvent(final MotionEvent event) {
        // If the button is disabled catch MotionEvent.ACTION_DOWN and invoke onDisabledClickCallBack
        if (!this.isEnabled() && onDisabledClickCallBack != null && event.getAction() == MotionEvent.ACTION_DOWN) {
            onDisabledClickCallBack.onClick(this);
            return true;
        }

        return super.onTouchEvent(event);
    }

    /**
     * Set a listener which is invoked when the button is disabled and clicked on
     * @param listener
     */
    public void setOnDisabledClickListener(final OnClickListener listener) {
        this.onDisabledClickCallBack = listener;
    }

    /**
     * Set if the button should be checked or not
     * @param checked is the newly checked state of the button
     */
    public void setChecked(boolean checked) {
        // Check if it is already in the correct checked-states
        if(checked == isChecked) {
            return;
        }

        // Set the background to look "pressed" if checked is true otherwise set the default background
        if(checked) {
            this.setBackgroundResource(R.drawable.giraf_button_background_checked);
        } else {
            this.setBackgroundResource(R.drawable.giraf_button_background);
        }

        // Set the internal boolean
        isChecked = checked;
    }

    /**
     * Get the checked-states
     * @return true of it is checked else false
     */
    public boolean isChecked() {
        return isChecked;
    }

    /**
     * Toggles if the button is checked
     */
    public void toggle() {
        setChecked(!isChecked);
    }
}
