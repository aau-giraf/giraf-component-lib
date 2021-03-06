package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created on 27/03/15.
 * Customize a GirafDialog. NOTICE! You can only add a title, description and a custom view to this class.
 * Also, you can only add custom view created in xml using resources.
 */
public class GirafInflatableDialog extends GirafDialog {

    // Constant tags for storage in bundles
    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String CUSTOM_VIEW_RESOURCE_TAG = "CUSTOM_VIEW_RESOURCE_TAG";
    private static final String DIALOG_ID_TAG = "DIALOG_ID_TAG";

    // Default value for dialogIdentifier
    private static final int DIALOG_ID_DEFAULT = -1;

    // Private members
    private ViewGroup customView;
    private Integer dialogIdentifier;
    private Activity caller;

    /**
     * An interface to perform the confirm action for a GirafInflatable
     */
    public interface OnCustomViewCreatedListener {
        public void editCustomView(ViewGroup customView, int dialogIdentifier);
    }

    // An instance of the OnCustomViewCreatedListener interface user
    OnCustomViewCreatedListener onCustomViewCreatedCallback;

    public static GirafInflatableDialog newInstance(String title, String description, int customViewResource) {
        return GirafInflatableDialog.newInstance(title, description, customViewResource, null);
    }

    public static GirafInflatableDialog newInstance(String title, String description, int customViewResource, Integer dialogIdentifier) {
        GirafInflatableDialog girafInflatableDialog = new GirafInflatableDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(CUSTOM_VIEW_RESOURCE_TAG, customViewResource);

        if (dialogIdentifier == null) {
            args.putInt(DIALOG_ID_TAG, DIALOG_ID_DEFAULT);
        } else if (dialogIdentifier == DIALOG_ID_DEFAULT) {
            throw new IllegalArgumentException("-1 is reserved for default identifier in GirafInflatableDialog.newInstance");
        } else {
            args.putInt(DIALOG_ID_TAG, dialogIdentifier);
        }

        // Store the bundle
        girafInflatableDialog.setArguments(args);
        return girafInflatableDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        caller = activity; // Save for later use (onStart)

        // Cast callback to an activity that implements OnCustomViewCreatedListener
        if (caller instanceof OnCustomViewCreatedListener) {
            onCustomViewCreatedCallback = (OnCustomViewCreatedListener) activity;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Creates the dialog
        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Get the arguments from the bundle
        final Bundle args = this.getArguments();

        setTitle(args.getString(TITLE_TAG, "")); // Set the title
        setDescription(args.getString(DESCRIPTION_TAG, "")); // Set the description

        // Finds the customView
        customView = (ViewGroup) inflater.inflate(args.getInt(CUSTOM_VIEW_RESOURCE_TAG), null);
        dialogIdentifier = args.getInt(DIALOG_ID_TAG);

        // Set the customView
        setCustomView(customView);

        return dialog; // Return the customized dialog
    }

    @Override
    public void onStart() {
        super.onStart();

        // Check if the activity implements the interface
        if (onCustomViewCreatedCallback != null) {

            onCustomViewCreatedCallback.editCustomView(customView, dialogIdentifier);

            // Check if an identifier was given but the activity do not implement OnCustomViewCreatedListener
        } else if (onCustomViewCreatedCallback == null && dialogIdentifier != null && dialogIdentifier != DIALOG_ID_DEFAULT) {
            throw new ClassCastException(caller.toString() + " must implement OnCustomViewCreatedListener interface. If you create a GirafInflatableDialog using an dialog identifier.");
        }
    }

}
