package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created on 24/02/15.
 */
public class GirafButton extends Button {

    /**
     * <p>Objects that implements this interface will have methods that are used to describe how
     * a button should be styled</p>
     */
    private interface IButtonStyleable {
        /**
         * The icon of the button style
         *
         * @param icon the icon of the button style
         */
        public void setIcon(Drawable icon);

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

        public ButtonStyle(Drawable icon) {
            enabled = new ButtonStateStyle(icon);
            pressed = new ButtonStateStyle(icon);
            focused = new ButtonStateStyle(icon);
            disabled = new ButtonStateStyle(icon);

            // Set default for different button states
            int cornerRadius = 10;
            int borderRadius = 2;

            setCornerRadius(cornerRadius);
            setStrokeWidth(borderRadius);
            setBaseColor(getColorCode(R.color.button_default_color));
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
         * Sets the icon of all button states
         */
        @Override
        public void setIcon(Drawable icon) {
            enabled.setIcon(icon);
            pressed.setIcon(icon);
            focused.setIcon(icon);
            disabled.setIcon(icon);
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
        Drawable icon;
        int cornerRadius;
        int fillColor;
        int strokeColor;
        int strokeWidth;

        public ButtonStateStyle(Drawable icon) {
            setIcon(icon);
        }

        @Override
        public void setIcon(Drawable icon) {
            this.icon = icon;
        }

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
     * <p>If more are needed, these should be added to the switch-statement in the {@link #generateGirafButtonDrawable}-method</p>
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

        // Finds the style if no stylle was found -1 is fallback
        int buttonTypeId = a.getInt(R.styleable.GirafButton_type, -1);
        BUTTON_TYPE buttonType = BUTTON_TYPE.values()[buttonTypeId];

        this.setBackgroundDrawable(generateGirafButtonDrawable(buttonType));
    }

    /**
     * <p>Used to generate the drawable part of the button.</p>
     *
     * @param buttonType indicates the layout of the button. See {@link dk.aau.cs.giraf.gui.GirafButton.BUTTON_TYPE}
     * @return A {@link android.graphics.drawable.StateListDrawable} with the correct button style
     */
    public StateListDrawable generateGirafButtonDrawable(BUTTON_TYPE buttonType) {
        switch (buttonType) {
            case ACCEPT: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_accept));
                bs.setBaseColor(getColorCode(R.color.button_success_color));
                return generateStateListDrawable(bs);
            }
            case ADD: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_add));
                bs.setBaseColor(getColorCode(R.color.button_success_color));
                return generateStateListDrawable(bs);
            }
            case ARROW_DOWN: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_arrow_down));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                return generateStateListDrawable(bs);
            }
            case ARROW_LEFT: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_arrow_left));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                return generateStateListDrawable(bs);
            }
            case ARROW_RIGHT: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_arrow_right));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                return generateStateListDrawable(bs);
            }
            case ARROW_UP: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_arrow_up));
                bs.setBaseColor(getColorCode(R.color.button_arrows_color));
                return generateStateListDrawable(bs);
            }
            case BACK: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_back));
                bs.setBaseColor(getColorCode(R.color.button_utility_action_color));
                return generateStateListDrawable(bs);
            }
            case CAMERA: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_camera));
                bs.setBaseColor(getColorCode(R.color.button_camera_color));
                return generateStateListDrawable(bs);
            }
            case CAMERA_SWITCH: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_camera_switch));
                bs.setBaseColor(getColorCode(R.color.button_camera_color));
                return generateStateListDrawable(bs);
            }
            case CANCEL: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_cancel));
                bs.setBaseColor(getColorCode(R.color.button_negative_color));
                return generateStateListDrawable(bs);
            }
            case CHANGE_USER: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_change_user));
                bs.setBaseColor(getColorCode(R.color.button_user_color));
                return generateStateListDrawable(bs);
            }
            case COPY: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_copy));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                return generateStateListDrawable(bs);
            }
            case DELETE: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_delete));
                bs.setBaseColor(getColorCode(R.color.button_negative_color));
                return generateStateListDrawable(bs);
            }
            case HELP: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_help));
                bs.setBaseColor(getColorCode(R.color.button_help_color));
                return generateStateListDrawable(bs);
            }
            case LOG_OUT: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_logout));
                bs.setBaseColor(getColorCode(R.color.button_user_color));
                return generateStateListDrawable(bs);
            }
            case MICROPHONE: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_microphone));
                bs.setBaseColor(getColorCode(R.color.button_microphone_color));
                return generateStateListDrawable(bs);
            }
            case MICROPHONE_OFF: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_microphone_off));
                bs.setBaseColor(getColorCode(R.color.button_microphone_color));
                return generateStateListDrawable(bs);
            }
            case MICROPHONE_ON: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_microphone_on));
                bs.setBaseColor(getColorCode(R.color.button_microphone_color));
                return generateStateListDrawable(bs);
            }
            case PLAY: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_play));
                bs.setBaseColor(getColorCode(R.color.button_media_play_color));
                return generateStateListDrawable(bs);
            }
            case RECORD: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_record));
                bs.setBaseColor(getColorCode(R.color.button_media_record_color));
                return generateStateListDrawable(bs);
            }
            case RESIZE: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_resize));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                return generateStateListDrawable(bs);
            }
            case ROTATE: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_rotate));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                return generateStateListDrawable(bs);
            }
            case SAVE: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_save));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                return generateStateListDrawable(bs);
            }
            case SEARCH: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_search));
                bs.setBaseColor(getColorCode(R.color.button_utility_tool_color));
                return generateStateListDrawable(bs);
            }
            case SETTINGS: {
                ButtonStyle bs = new ButtonStyle(this.getContext().getResources().getDrawable(R.drawable.icon_settings));
                bs.setBaseColor(getColorCode(R.color.button_utility_action_color));
                return generateStateListDrawable(bs);
            }
        }

        // This statement should never be reached
        return new StateListDrawable();
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
        sld.addState(PRESSED_ENABLED_STATE_SET, generateLayerDrawable(buttonStyle.pressed.generateBackground(), buttonStyle.pressed.icon));
        sld.addState(FOCUSED_STATE_SET, generateLayerDrawable(buttonStyle.focused.generateBackground(), buttonStyle.enabled.icon));
        sld.addState(ENABLED_STATE_SET, generateLayerDrawable(buttonStyle.enabled.generateBackground(), buttonStyle.enabled.icon));
        sld.addState(EMPTY_STATE_SET, generateLayerDrawable(buttonStyle.disabled.generateBackground(), buttonStyle.enabled.icon));

        return sld;
    }

    /**
     * <p>Will generate a {@link android.graphics.drawable.LayerDrawable} with a background and an icon</p>
     *
     * @param background background of the button
     * @param icon icon of the button
     * @return a layered drawable with the defined background and icon
     */
    private LayerDrawable generateLayerDrawable(Drawable background, Drawable icon) {
        icon.setBounds(10,10,10,10);
        Drawable[] drawables = {background, icon};
        return new LayerDrawable(drawables);
    }

    /**
     * Gets the label of the {@link dk.aau.cs.giraf.gui.GirafButton}
     *
     * @return label of the {@link dk.aau.cs.giraf.gui.GirafButton}. See {@link dk.aau.cs.giraf.gui.GirafButton#label}
     */
    public String getLabel() {
        return label;
    }

    /**
     * Sets the label of the {@link dk.aau.cs.giraf.gui.GirafButton}
     *
     * @param label the label of the {@link dk.aau.cs.giraf.gui.GirafButton}. See {@link dk.aau.cs.giraf.gui.GirafButton#label}
     */
    public void setLabel(String label) {
        this.label = label;
    }

}
