package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created on 27/03/15.
 * Customize a GirafDialog. NOTICE! You can only add a title, description and a custom view to this class.
 * Also, you can only add custom view created in xml using ressources.
 */
public class GirafInflateableDialog extends GirafDialog {

    private static final String TITLE_TAG = "TITLE_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String CUSTOM_VEIW_RESSOURCE_TAG = "CUSTOM_VEIW_RESSOURCE_TAG";

    public static GirafInflateableDialog newInstance(String title, String description, int customViewRessource) {
        GirafInflateableDialog girafInflateableDialog = new GirafInflateableDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putString(TITLE_TAG, title);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(CUSTOM_VEIW_RESSOURCE_TAG, customViewRessource);

        // Store the bundle
        girafInflateableDialog.setArguments(args);
        return girafInflateableDialog;
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

        // Finds the customView in ressources and inflates it then sets it on the dialog
        setCustomView((ViewGroup) inflater.inflate(args.getInt(CUSTOM_VEIW_RESSOURCE_TAG), null));

        return dialog; // Return the customize dialog
    }
}
