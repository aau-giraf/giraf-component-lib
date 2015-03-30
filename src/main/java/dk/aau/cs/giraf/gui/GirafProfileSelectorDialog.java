package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.List;

import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created on 26/03/15.
 */
public class GirafProfileSelectorDialog extends GirafDialog {

    private static final String GUARDIAN_TAG = "GUARDIAN_TAG";

    /**
     * An interface to perform the confirm action for a GirafNotifyDialog
     */
    public interface Selection {
        /**
         * Change the current user
         * @param profileID the identifier of the user
         */
        public void changeCurrentUser (int profileID);
    }

    // An instance of the confirmation interface user
    Selection selection;

    public static GirafProfileSelectorDialog newInstance(int guardianID) {
        GirafProfileSelectorDialog girafProfileSelectorDialog = new GirafProfileSelectorDialog();

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putInt(GUARDIAN_TAG, guardianID);

        // Store the bundle
        girafProfileSelectorDialog.setArguments(args);
        return girafProfileSelectorDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Check if the activity using the fragment implements the needed interface
        try {
            selection = (Selection) activity;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement Selection interface");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Sets the title of the dialog
        setTitle(getActivity().getString(R.string.change_user_dialog_title));

        final Bundle args = this.getArguments();
        final int guardianID = args.getInt(GUARDIAN_TAG, -1);

        // If the user was not found throw exception
        if(guardianID == -1) {
            throw new IllegalArgumentException("The user does not exist");
        }

        ProfileController profileController = new ProfileController(GirafProfileSelectorDialog.this.getActivity());
        Profile guardianProfile = profileController.getProfileById(guardianID);
        List<Profile> childList = profileController.getChildrenByGuardian(guardianProfile);


        return dialog;
    }
}
