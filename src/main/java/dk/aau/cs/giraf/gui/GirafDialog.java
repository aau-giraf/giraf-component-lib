package dk.aau.cs.giraf.gui;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created on 24/03/15.
 */
public abstract class GirafDialog extends DialogFragment {

    // Members of the GirafDialog
    private LinearLayout dialogLayout; // The layout containing the dialog
    private TextView titleTextView; // The textView of the title
    private TextView descriptionTextView; // The textView of the description
    private FrameLayout customView; // The container for some custom layout
    private LinearLayout buttonContainer; // A container for the buttons in the bottom
    private AlertDialog.Builder dialogBuilder; // The Builder for the GirafDialog

    /**
     * Sets the title of the {@link dk.aau.cs.giraf.gui.GirafDialog}
     * @param title the title of the dialog
     */
    protected final void setTitle(final String title) {
        titleTextView.setText(title);
    }

    /**
     * Sets the description of the {@link dk.aau.cs.giraf.gui.GirafDialog}
     * @param description the description of the dialog
     */
    protected final void setDescription(final String description) {
        descriptionTextView.setText(description);
        descriptionTextView.setVisibility(View.VISIBLE);
    }

    /**
     * Sets a custom layout into the {@link dk.aau.cs.giraf.gui.GirafDialog}
     * @param viewGroup the custom layout of the dialog
     */
    protected final void setCustomView(final ViewGroup viewGroup) {
        customView.addView(viewGroup);
        customView.setVisibility(View.VISIBLE);
    }

    /**
     * Adds a button to the bottom of the {@link dk.aau.cs.giraf.gui.GirafDialog}
     * @param girafButton a {@link dk.aau.cs.giraf.gui.GirafButton}
     */
    protected final void addButton(final GirafButton girafButton) {
        // Add the button with the static params
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        buttonParams.leftMargin = getResources().getDimensionPixelSize(R.dimen.giraf_dialog_item_spacing) / 2;
        buttonParams.rightMargin = getResources().getDimensionPixelSize(R.dimen.giraf_dialog_item_spacing) / 2;
        buttonContainer.addView(girafButton, buttonParams);
        buttonContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public  Dialog onCreateDialog(Bundle savedInstanceState) {

        // Finds the dialog builder
        dialogBuilder = new AlertDialog.Builder(getActivity());

        // Finds the layout inflater
        final LayoutInflater inflater = getActivity().getLayoutInflater();

        // Create layout from xml
        dialogLayout = (LinearLayout) inflater.inflate(R.layout.giraf_dialog,null);

        // Create layout from withing outer layout
        buttonContainer = (LinearLayout) dialogLayout.findViewById(R.id.button_container);

        // Create views from withing outer layout
        titleTextView =  (TextView) dialogLayout.findViewById(R.id.title);
        descriptionTextView =  (TextView) dialogLayout.findViewById(R.id.description);
        customView = (FrameLayout) dialogLayout.findViewById(R.id.custom_view);

        // Set the view of the dialogBuilder to the custom created view
        dialogBuilder.setView(dialogLayout);

        // Return the created dialog
        return dialogBuilder.create();
    }

    /**
     * Is called whenever a {@link dk.aau.cs.giraf.gui.GirafDialog} is dismissed.
     * It calls the onDismiss() method of the parent activity, if that activity
     * implements {@link android.content.DialogInterface.OnDismissListener OnDismissListener}
     * @param dialog that is dismissed
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        Activity activity = getActivity();
        if (activity != null && activity instanceof DialogInterface.OnDismissListener) {
            ((DialogInterface.OnDismissListener) activity).onDismiss(dialog);
        }
    }
}
