package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created on 26/03/15.
 * GirafProfileSelectorDialog is not yet working!
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

        setTitle("GirafProfileSelectorDialog is not yet working!");

        /* TODO Undone work
        // Sets the title of the dialog
        setTitle(getActivity().getString(R.string.change_user_dialog_title));

        final Bundle args = this.getArguments();
        final int guardianID = args.getInt(GUARDIAN_TAG, -1);

        // If the user was not found throw exception
        if(guardianID == -1) {
            throw new IllegalArgumentException("The user does not exist");
        }



        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.giraf_profile_selection_custom_view,null);

        setCustomView(ll);

        GirafSpinner spinner = (GirafSpinner) ll.findViewById(R.id.department_spinner);
        ArrayList<String> strings = new ArrayList<String>();
        strings.add("Rød");
        strings.add("Grøn");
        strings.add("Blå");
        GirafSpinnerAdapter<String> adapter = new GirafSpinnerAdapter<String>(getActivity(),strings);
        spinner.setAdapter(adapter);

        ProfileController profileController = new ProfileController(GirafProfileSelectorDialog.this.getActivity());
        Profile guardianProfile = profileController.getProfileById(guardianID);
        List<Profile> childList = profileController.getChildrenByGuardian(guardianProfile);


        */
        return dialog;
    }
}
