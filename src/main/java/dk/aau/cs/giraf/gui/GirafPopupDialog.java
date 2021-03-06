package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

/**
 * Created on 05-05-2017.
 */
public class GirafPopupDialog extends Dialog {

    private Context context;
    private TextView message;
    private TextView warning;
    private GirafButton button1;
    private GirafButton button2;


    /**
     * Constructor overloading such that we can use resource strings.
     * @param titleId the id for a resource string with the tile.
     * @param messageId the id for a resource string with the message.
     * @param context the context of the parent.
     */
    public GirafPopupDialog(@StringRes int titleId, @StringRes int messageId, Context context) {
        this(context.getString(titleId), context.getString(messageId), context);
    }

    /**
     * Constructor overloading such that we can use resource strings.
     * @param title the title for the dialog.
     * @param messageId the id for a resource string with the message.
     * @param context the context of the parent.
     */
    public GirafPopupDialog(String title, @StringRes int messageId, Context context) {
        this(title, context.getString(messageId), context);
    }

    /**
     * Constructor overloading such that we can use resource strings.
     * @param titleId the id for a resource string with the tile.
     * @param message the message o the dialog.
     * @param context the context of the parent.
     */
    public GirafPopupDialog(@StringRes int titleId, String message, Context context) {
       this(context.getString(titleId), message, context);
    }

    /**
     * The constructor for the Giraf Popup Dialog.
     * @param title the title of the dialog.
     * @param message the message of the dialog.
     * @param context the context of the parent.
     */
    public GirafPopupDialog(String title, String message, Context context) {
        super(context,R.style.GirafDialog);
        this.context = context;
        this.setContentView(R.layout.giraf_popup_dialog);
        super.setTitle(title);
        this.message = (TextView) findViewById(R.id.description);
        warning = (TextView) findViewById(R.id.warning);
        button1 = (GirafButton) findViewById(R.id.button_1);
        button2 = (GirafButton) findViewById(R.id.button_2);
        this.message.setText(message);
        button1.setVisibility(View.INVISIBLE);
        button2.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Makes it possible to change the message of the dialog.
     * @param message the message for the dialog.
     */
    public void setMessage(String message) {
        this.message.setText(message);
    }

    /**
     * Makes it possible to change the message of the dialog.
     * @param messageId the resource for the message for the dialog.
     */
    public void setMessage(@StringRes int messageId) {
        this.message.setText(messageId);
    }

    /**
     * Shows a warning.
     * @param warningId the resource for the warning.
     */
    public void showWarning(@StringRes int warningId) {
        this.warning.setText(warningId);
        this.warning.setVisibility(View.VISIBLE);
    }

    /**
     * Shows a warning.
     * @param warning the text for the warning.
     */
    public void showWarning(String warning) {
        this.warning.setText(warning);
        this.warning.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the warning.
     */
    public void hideWarning() {
        this.warning.setVisibility(View.INVISIBLE);
    }

    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonTextId the id for a resource with the text for the button.
     * @param buttonIconId the id for a resource icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton1(@StringRes int buttonTextId, @DrawableRes int buttonIconId, View.OnClickListener listener) {
        setButton1(buttonTextId, context.getResources().getDrawable(buttonIconId), listener);
    }

    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonText the text for the button.
     * @param buttonIconId the id for a resource icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton1(String buttonText, @DrawableRes int buttonIconId, View.OnClickListener listener) {
        setButton1(buttonText, context.getResources().getDrawable(buttonIconId), listener);
    }

    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonTextId the id for a resource with the text for the button.
     * @param buttonIcon the icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton1(@StringRes int buttonTextId, Drawable buttonIcon, View.OnClickListener listener) {
        setButton1(context.getText(buttonTextId).toString(),buttonIcon,listener);
    }

    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonText the text for the button.
     * @param buttonIcon the icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton1(String buttonText, Drawable buttonIcon, View.OnClickListener listener) {
        button1.setIcon(buttonIcon);
        setButton1(buttonText,listener);
    }

    /**
     * Sets up the button with text and the listener.
     * Auto shows the button.
     * @param buttonTextId the id for a resource with the text for the button.
     * @param listener the listener for the button.
     */
    public void setButton1(@StringRes int buttonTextId, View.OnClickListener listener) {
        setButton1(context.getText(buttonTextId).toString(),listener);
    }

    /**
     * Sets up the button with text and the listener.
     * Auto shows the button.
     * @param buttonText the text for the button.
     * @param listener the listener for the button.
     */
    public void setButton1(String buttonText, View.OnClickListener listener) {
        button1.setText(buttonText);
        button1.setOnClickListener(listener);
        button1.setVisibility(View.VISIBLE);
    }


    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonTextId the id for a resource with the text for the button.
     * @param buttonIconId the id for a resource icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton2(@StringRes int buttonTextId, @DrawableRes int buttonIconId, View.OnClickListener listener) {
        setButton2(buttonTextId, context.getResources().getDrawable(buttonIconId), listener);
    }

    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonText the text for the button.
     * @param buttonIconId the id for a resource icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton2(String buttonText, @DrawableRes int buttonIconId, View.OnClickListener listener) {
        setButton2(buttonText, context.getResources().getDrawable(buttonIconId), listener);
    }

    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonTextId the id for a resource with the text for the button.
     * @param buttonIcon the icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton2(@StringRes int buttonTextId, Drawable buttonIcon, View.OnClickListener listener) {
        setButton2(context.getText(buttonTextId).toString(),buttonIcon,listener);
    }

    /**
     * Sets up the button with text, icon and listener.
     * Auto shows the button.
     * @param buttonText the text for the button.
     * @param buttonIcon the icon for the button.
     * @param listener the listener for the button.
     */
    public void setButton2(String buttonText, Drawable buttonIcon, View.OnClickListener listener) {
        button2.setIcon(buttonIcon);
        setButton2(buttonText,listener);
    }

    /**
     * Sets up the button with text and the listener.
     * Auto shows the button.
     * @param buttonTextId the id for a resource with the text for the button.
     * @param listener the listener for the button.
     */
    public void setButton2(@StringRes int buttonTextId, View.OnClickListener listener) {
        setButton2(context.getText(buttonTextId).toString(),listener);
    }

    /**
     * Sets up the button with text and the listener.
     * Auto shows the button.
     * @param buttonText the text for the button.
     * @param listener the listener for the button.
     */
    public void setButton2(String buttonText, View.OnClickListener listener) {
        button2.setText(buttonText);
        button2.setOnClickListener(listener);
        button2.setVisibility(View.VISIBLE);
    }

    /**
     * Disables the button.
     */
    public void disableButton1() {
        button1.setEnabled(false);
    }

    /**
     * Enables the button.
     */
    public void enableButton1() {
        button1.setEnabled(true);
    }

    /**
     * Disables the button.
     */
    public void disableButton2() {
        button2.setEnabled(false);
    }

    /**
     * Enables the button.
     */
    public void enableButton2() {
        button2.setEnabled(true);
    }

    /**
     * Hides the button.
     */
    public void hideButton1() {
        button1.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the button.
     * The setButton methods auto show the button.
     */
    public void showButton1() {
        button1.setVisibility(View.VISIBLE);
    }

    /**
     * Hides the button.
     */
    public void hideButton2() {
        button2.setVisibility(View.INVISIBLE);
    }

    /**
     * Shows the button.
     * The setButton methods auto show the button.
     */
    public void showButton2() {
        button2.setVisibility(View.VISIBLE);
    }

}
