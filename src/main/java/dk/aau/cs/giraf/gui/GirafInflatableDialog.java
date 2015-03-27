package dk.aau.cs.giraf.gui;

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

    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String CUSTOM_VIEW_RESOURCE_TAG = "CUSTOM_VIEW_RESOURCE_TAG";
    private ViewGroup customView;

    public static GirafInflatableDialog newInstance(String title, String description, int customViewResource) {
        GirafInflatableDialog girafInflatableDialog = new GirafInflatableDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(CUSTOM_VIEW_RESOURCE_TAG, customViewResource);

        // Store the bundle
        girafInflatableDialog.setArguments(args);
        return girafInflatableDialog;
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

        // Set the customView
        setCustomView(customView);

        return dialog; // Return the customized dialog
    }

    /**
     * Gets the customView of the GirafDialog
     * @return the customView
     */
    public ViewGroup getCustomView() {
        return customView;
    }

}
