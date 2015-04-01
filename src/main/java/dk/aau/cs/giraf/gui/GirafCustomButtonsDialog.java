package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;

/**
 * Created on 01/04/15.
 */
public class GirafCustomButtonsDialog extends GirafDialog {

    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String DIALOG_ID = "DIALOG_ID";

    CustomButtons customButtons;

    public interface CustomButtons {

        /**
         * Method for adding buttons to a buttoncontainer of a specific dialog
         * @param dialogIdentifier
         * @param buttonContainer
         */
        public void fillButtonContainer(int dialogIdentifier, ButtonContainer buttonContainer);
    }

    /**
     * Inner class to allow for adding buttons
     */
    public class ButtonContainer {
        public void addGirafButton(GirafButton girafButton) {
            GirafCustomButtonsDialog.this.addButton(girafButton);
        }
    }

    /**
     * Creates a new instance of GirafCustomButtonsDialog
     * @param title title of the dialog
     * @param description description of the dialog
     * @return a GirafCustomButtonsDialog
     */
    public static GirafCustomButtonsDialog newInstance(String title, String description, int dialogIdentifier) {
        GirafCustomButtonsDialog girafCustomButtonsDialog = new GirafCustomButtonsDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(DIALOG_ID, dialogIdentifier);

        // Store the bundle
        girafCustomButtonsDialog.setArguments(args);
        return girafCustomButtonsDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Check if the activity using the fragment implements the needed interface
        try {
            customButtons = (CustomButtons) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement CustomButtons interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Fetch the dialog
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Fetch the arguments
        final Bundle args = this.getArguments();

        // Set the arguments
        setTitle(args.getString(TITLE_TAG, ""));
        setDescription(args.getString(DESCRIPTION_TAG, ""));

        customButtons.fillButtonContainer(args.getInt(DIALOG_ID), new ButtonContainer());

        return dialog;
    }
}
