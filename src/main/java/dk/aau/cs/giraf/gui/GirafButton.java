package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageButton;

/**
 * Created on 24/02/15.
 * <p>To use this component in xml remember to set the type of the button:</p>
 * <p><b>Example:</b></p>
 * <pre>{@code
 *  <dk.aau.cs.giraf.gui.GirafButton
 *      android:id="@+id/giraf_button"
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      app:type="CAMERA" />}</pre>
 */
public class GirafButton extends ImageButton {

    /**
     * <p>Objects that implements this interface will have methods that are used to describe how
     * a button should be styled</p>
     */
    private interface IButtonStyleable {
        /**
         * The corner radius of the button style
         *
         * @param cornerRadius the corner radius of the button style
         */
        public void setCornerRadius(int cornerRadius);

        /**
         * The background color of the button style
         *
         * @param fillColor background color of the button style in HEX
         */
        public void setFillColor(String fillColor);

        /**
         * The stroke color of the button style
         *
         * @param strokeColor color of the stroke (border) of the style
         */
        public void setStrokeColor(String strokeColor);

        /**
         * The stroke width of the button style
         *
         * @param strokeWidth width of the stroke (border) of the style in dp
         */
        public void setStrokeWidth(int strokeWidth);
    }

    /**
     * <p>Used to configure the style of buttons the different states: Enabled, Disabled,
     * Pressed and Focused</p>
     */
    private class ButtonStyle implements IButtonStyleable {
        public ButtonStateStyle enabled;
        public ButtonStateStyle pressed;
        public ButtonStateStyle focused;
        public ButtonStateStyle disabled;

        public Drawable icon;

        public ButtonStyle(Context context, Drawable icon) {
            enabled = new ButtonStateStyle();
            pressed = new ButtonStateStyle();
            focused = new ButtonStateStyle();
            disabled = new ButtonStateStyle();

            setCornerRadius(context.getResources().getInteger(R.integer.giraf_button_corner_radius));
            setStrokeWidth(context.getResources().getInteger(R.integer.giraf_button_stroke_width));
            setBaseColor(getColorCode(R.color.button_default_color));
            this.setIcon(icon);
        }

        /**
         * <p>Sets the base color of the button. Will generate different colors for the different stats
         * of the button. For instance darken when the button is pressed.</p>
         * @param color the color in HEX without #.
         */
        public void setBaseColor(String color) {
            enabled.setStrokeColor("#" + adjustColor(color, -5));
            enabled.setFillColor("#" + color);

            pressed.setStrokeColor("#" + adjustColor(color, -15));
            pressed.setFillColor("#" + adjustColor(color, -10));

            focused.setStrokeColor("#" + adjustColor(color, 25));
            focused.setFillColor("#" + adjustColor(color, 20));

            disabled.setStrokeColor("#59" + adjustColor(color, -5));
            disabled.setFillColor("#59" + color);
        }

        /**
         * Found <a href="http://stackoverflow.com/questions/5560248/programmatically-lighten-or-darken-a-hex-color-or-rgb-and-blend-colors">here</a>
         * @param color color to adjust. In HEX without #.
         * @param percent percentage to adjust. Use negative values to darken.
         * @return the adjusted color with "#" prefixed.
         */
        private String adjustColor(String color, int percent) {
            int num = Integer.parseInt(color, 16);
            int amt = (int) Math.round(2.55 * percent);
            int R = (num >> 16) + amt;
            int G = (num >> 8 & 0x00FF) + amt;
            int B = (num & 0x0000FF) + amt;
            return Integer.toHexString(0x1000000 + (R < 255 ? R < 1 ? 0 : R : 255) * 0x10000 + (G < 255 ? G < 1 ? 0 : G : 255) * 0x100 + (B < 255 ? B < 1 ? 0 : B : 255)).substring(1);
        }

        /**
         * Sets the icon of the button
         */
        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

        /**
         * Sets the corder radius of all button states
         */
        @Override
        public void setCornerRadius(int cornerRadius) {
            enabled.setCornerRadius(cornerRadius);
            pressed.setCornerRadius(cornerRadius);
            focused.setCornerRadius(cornerRadius);
            disabled.setCornerRadius(cornerRadius);
        }

        /**
         * Sets the background of all button states
         */
        @Override
        public void setFillColor(String fillColor) {
            enabled.setFillColor(fillColor);
            pressed.setFillColor(fillColor);
            focused.setFillColor(fillColor);
            disabled.setFillColor(fillColor);
        }

        /**
         * Sets the stroke (border) color of all button states
         */
        @Override
        public void setStrokeColor(String strokeColor) {
            enabled.setStrokeColor(strokeColor);
            pressed.setStrokeColor(strokeColor);
            focused.setStrokeColor(strokeColor);
            disabled.setStrokeColor(strokeColor);
        }

        /**
         * Sets the stroke width of all button states in dp
         */
        @Override
        public void setStrokeWidth(int strokeWidth) {
            enabled.setStrokeWidth(strokeWidth);
            pressed.setStrokeWidth(strokeWidth);
            focused.setStrokeWidth(strokeWidth);
            disabled.setStrokeWidth(strokeWidth);
        }
    }

    /**
     * <p>Used to configure the style of a button in a specific state. See {@link dk.aau.cs.giraf.gui.GirafButton.ButtonStyle}</p>
     */
    private class ButtonStateStyle implements IButtonStyleable {
        int cornerRadius;
        int fillColor;
        int strokeColor;
        int strokeWidth;

        @Override
        public void setCornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
        }

        @Override
        public void setFillColor(String fillColor) {
            this.fillColor = Color.parseColor(fillColor);
        }

        @Override
        public void setStrokeColor(String strokeColor) {
            this.strokeColor = Color.parseColor(strokeColor);
        }

        @Override
        public void setStrokeWidth(int strokeWidth) {
            this.strokeWidth = strokeWidth;
        }

        public GradientDrawable generateBackground() {
            GradientDrawable gd = new GradientDrawable();
            gd.setColor(fillColor);
            gd.setCornerRadius(cornerRadius);
            gd.setStroke(strokeWidth, strokeColor);

            return gd;
        }
    }

    /**
     * <p>A list of all available buttons</p>
     * <p>If more are needed, these should be added to the switch-statement in the {@link #getButtonStyle(dk.aau.cs.giraf.gui.GirafButton.BUTTON_TYPE)}-method</p>
     */
    public enum BUTTON_TYPE {
        ACCEPT,
        ADD,
        ARROW_DOWN,
        ARROW_LEFT,
        ARROW_RIGHT,
        ARROW_UP,
        BACK,
        CAMERA,
        CAMERA_SWITCH,
        CANCEL,
        CHANGE_USER,
        COPY,
        DELETE,
        HELP,
        LOG_OUT,
        MICROPHONE,
        MICROPHONE_OFF,
        MICROPHONE_ON,
        PLAY,
        RECORD,
        RESIZE,
        ROTATE,
        SAVE,
        SEARCH,
        SETTINGS
    };

    /**
     * Will display a small label below the button. Will not be displayed if not set
     * <p>Use this do describe the functionality of the button (for instance if the icon is unclear)</p>
     */
    private String label; // TODO: Implement functionality that adds this below the button

    public GirafButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeButton(attrs);
    }

    public GirafButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initializeButton(attrs);
    }

    /**
     * Used to initialize the style of the button depending on the {@link dk.aau.cs.giraf.gui.GirafButton.BUTTON_TYPE}
     */
    private void initializeButton(AttributeSet attrs) {
        final TypedArray a = this.getContext().obtainStyledAttributes(attrs, R.styleable.GirafButton);

        // Finds the style if no style was found -1 is fallback
        int buttonTypeId = a.getInt(R.styleable.GirafButton_type, -1);
        BUTTON_TYPE buttonType = BUTTON_TYPE.values()[buttonTypeId];

        // Find the button style
        ButtonStyle buttonStyle = getButtonStyle(buttonType);

        // Set the background and icon of the button depending on the style of button
        this.setBackgroundDrawable(this.generateStateListDrawable(buttonStyle));
        this.setImageDrawable(buttonStyle.icon);

        // Set the image to scale fitting the center
        this.setScaleType(ScaleType.FIT_CENTER);

        // Sets the padding of the button to 20dp
        int pad = (int) convertDpToPixel(this.getContext().getResources().getInteger(R.integer.giraf_button_padding), this.getContext()); // Convert dp to pixels
        this.setPadding(pad,pad,pad,pad); // Set the padding in all directions
    }

    /**
     * Gets the button style based on the button type
     * @param buttonType the type of the button
     * @return a {@link dk.aau.cs.giraf.gui.GirafButton.ButtonStyle} corresponding to the given button type, null if given incorrect type.
     */
    private ButtonStyle getButtonStyle(BUTTON_TYPE buttonType) {
        ButtonStyle bs = null;
        switch (buttonType) {
            case ACCEPT: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_accept));
                bs.setBaseColor(getColorCode(R.color.button_success_color));
                break;
            }
            case ADD: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_add));
                bs.setBaseColor(getColorCode(R.color.button_success_color));
                break;
            }
            case ARROW_DOWN: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_arrow_down));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                break;
            }
            case ARROW_LEFT: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_arrow_left));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                break;
            }
            case ARROW_RIGHT: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_arrow_right));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                break;
            }
            case ARROW_UP: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_arrow_up));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                break;
            }
            case BACK: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_back));
                bs.setBaseColor(getColorCode(R.color.button_utility_action_color));
                break;
            }
            case CAMERA: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_camera));
                bs.setBaseColor(getColorCode(R.color.button_camera_color));
                break;
            }
            case CAMERA_SWITCH: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_camera_switch));
                bs.setBaseColor(getColorCode(R.color.button_camera_color));
                break;
            }
            case CANCEL: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_cancel));
                bs.setBaseColor(getColorCode(R.color.button_negative_color));
                break;
            }
            case CHANGE_USER: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_change_user));
                bs.setBaseColor(getColorCode(R.color.button_user_color));
                break;
            }
            case COPY: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_copy));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                break;
            }
            case DELETE: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_delete));
                bs.setBaseColor(getColorCode(R.color.button_negative_color));
                break;
            }
            case HELP: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_help));
                bs.setBaseColor(getColorCode(R.color.button_help_color));
                break;
            }
            case LOG_OUT: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_logout));
                bs.setBaseColor(getColorCode(R.color.button_user_color));
                break;
            }
            case MICROPHONE: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_microphone));
                bs.setBaseColor(getColorCode(R.color.button_microphone_color));
                break;
            }
            case MICROPHONE_OFF: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_microphone_off));
                bs.setBaseColor(getColorCode(R.color.button_microphone_color));
                break;
            }
            case MICROPHONE_ON: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_microphone_on));
                bs.setBaseColor(getColorCode(R.color.button_microphone_color));
                break;
            }
            case PLAY: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_play));
                bs.setBaseColor(getColorCode(R.color.button_media_play_color));
                break;
            }
            case RECORD: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_record));
                bs.setBaseColor(getColorCode(R.color.button_media_record_color));
                break;
            }
            case RESIZE: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_resize));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                break;
            }
            case ROTATE: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_rotate));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                break;
            }
            case SAVE: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_save));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                break;
            }
            case SEARCH: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_search));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                break;
            }
            case SETTINGS: {
                bs = new ButtonStyle(this.getContext(),this.getContext().getResources().getDrawable(R.drawable.icon_settings));
                bs.setBaseColor(getColorCode(R.color.button_utility_action_color));
                break;
            }
        }

        // This statement should never be reached
        return bs;
    }

    /**
     * Will generate a HEX-color code from a color id (from resources).
     *
     * @param id the id of the color. For instance R.color.x
     * @return the color code without #.
     */
    private String getColorCode(int id) {
        return String.format("%06X", (0xffffff & this.getContext().getResources().getColor(id)));
    }

    /**
     * <p>Used to generate a drawable for a specific button</p>
     *
     * @param buttonStyle the style of the button
     * @return A {@link android.graphics.drawable.StateListDrawable} with the correct button style
     */
    private StateListDrawable generateStateListDrawable(ButtonStyle buttonStyle) {
        StateListDrawable sld = new StateListDrawable();

        // Add different states
        sld.addState(PRESSED_ENABLED_STATE_SET, buttonStyle.pressed.generateBackground());
        sld.addState(FOCUSED_STATE_SET, buttonStyle.focused.generateBackground());
        sld.addState(ENABLED_STATE_SET, buttonStyle.enabled.generateBackground());
        sld.addState(EMPTY_STATE_SET, buttonStyle.disabled.generateBackground());

        return sld;
    }

    /**
     * This method converts dp unit to equivalent pixels, depending on device density.
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * (metrics.densityDpi / 160f);
    }

}
