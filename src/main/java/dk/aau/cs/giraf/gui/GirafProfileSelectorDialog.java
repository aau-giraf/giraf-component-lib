package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.dblib.models.Category;
import dk.aau.cs.giraf.gui.GirafButton;
import dk.aau.cs.giraf.gui.GirafDialog;
import dk.aau.cs.giraf.gui.GirafPictogramItemView;
import dk.aau.cs.giraf.gui.GirafProfileSelectorAdapter;
import dk.aau.cs.giraf.dblib.Helper;
import dk.aau.cs.giraf.dblib.models.Profile;
import dk.aau.cs.giraf.gui.R;

/**
 * Created on 26/03/15.
 * GirafProfileSelectorDialog is not yet working!
 */
public class GirafProfileSelectorDialog extends GirafDialog {

    private static final String PROFILE_IDS_TAG = "PROFILE_IDS_TAG";
    private static final String IS_MULTI_SELECT_TAG = "IS_MULTI_SELECT_TAG";
    private static final String DIALOG_IDENTIFIER_TAG = "DIALOG_IDENTIFIER_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String PROFILE_CHECK_STATUS_TAG = "PROFILE_CHECK_STATUS_TAG";

    private Helper helper;

    RelativeLayout gridContainer;

    /**
     * An interface to perform the confirm action for a GirafProfileSelectorDialog
     */
    public interface OnReturnProfilesListener {
        /**
         * Gets the selected profiles
         *
         * @param dialogIdentifier the identifier of the dialog
         */
        public void onProfilesSelected(int dialogIdentifier, List<Pair<Profile, Boolean>> checkedProfileList);

    }

    // An instance of the confirmation interface user
    OnReturnProfilesListener profileListener;

    private class LoadUsers extends AsyncTask<Void, Void, List<Profile>> {
        private int[] profileIds;
        private boolean[] profilesCheckedStatus;
        private boolean isMultiselect;
        private int dialogIdentifier;

        public LoadUsers(int[] profileIds, boolean[] profilesCheckedStatus, boolean isMultiselect, int dialogIdentifier) {
            this.profileIds = profileIds;
            this.profilesCheckedStatus = profilesCheckedStatus;
            this.isMultiselect = isMultiselect;
            this.dialogIdentifier = dialogIdentifier;
        }

        @Override
        protected List<Profile> doInBackground(Void... params) {
            ArrayList<Profile> profiles = new ArrayList<Profile>();
            for (int profileId : profileIds) {
                profiles.add(helper.profilesHelper.getById(profileId));
            }
            return profiles;
        }

        @Override
        protected void onPostExecute(List<Profile> profiles) {
            super.onPostExecute(profiles);

            final GirafProfileSelectorAdapter profileAdapter = new GirafProfileSelectorAdapter(getActivity(), profiles);
            final GridView profileGrid = (GridView) gridContainer.findViewById(R.id.profile_grid);

            profileGrid.setAdapter(profileAdapter);

            for (int i = 0; i < profileAdapter.getCount(); i++) {

                if (profilesCheckedStatus[i] && isMultiselect) {
                    profileAdapter.setItemChecked(i, true);
                }
            }

            if (isMultiselect) {

                // Enable selection of profiles
                profileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        profileAdapter.toggleItemChecked(position);
                        ((GirafPictogramItemView) view).toggle();
                    }
                });

                // Add a button that ends the selection
                GirafButton selectButton = new GirafButton(getActivity(), getActivity().getResources().getDrawable(R.drawable.icon_accept), "Vælg");
                selectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (profileAdapter != null) {
                            profileListener.onProfilesSelected(dialogIdentifier, profileAdapter.getCheckedProfileList());
                        } else {
                            Toast.makeText(getActivity(), "Profilerne blev ikke hentet inden du trykkede vælg", Toast.LENGTH_SHORT).show();
                        }
                        dismiss();
                    }
                });
                addButton(selectButton);

            } else {
                // Return the first selected item
                profileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Profile selectedProfile = (Profile) profileGrid.getAdapter().getItem(position);

                        List<Pair<Profile, Boolean>> singleUserList = new ArrayList<Pair<Profile, Boolean>>();
                        singleUserList.add(new Pair<Profile, Boolean>(selectedProfile, true));
                        profileListener.onProfilesSelected(dialogIdentifier, singleUserList);
                        dismiss();

                    }
                });
            }

        }
    }

    /**
     * Gets a new instance of the GirafProfileSelector
     *
     * @param context          the context of where the dialog appears
     * @param guardianID       the identifier of the guardian of which citizens should be selectable
     * @param includeGuardian  should the guardian with the given identifier be included in the list of selectable profiles
     * @param isMultiselect    should it be possible to select multiple profiles
     * @param description      the description on the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    public static GirafProfileSelectorDialog newInstance(Context context, int guardianID, boolean includeGuardian, boolean isMultiselect, String description, int dialogIdentifier) {
        Helper helper = new Helper(context);
        Profile guardian = helper.profilesHelper.getById(guardianID);
        List<Profile> profiles = helper.profilesHelper.getChildrenByGuardian(guardian);
        List<Pair<Profile, Boolean>> profileCheckList = new ArrayList<Pair<Profile, Boolean>>();

        if (includeGuardian) {
            profiles.add(0, guardian);
        }

        for (Profile profile : profiles) {
            profileCheckList.add(new Pair<Profile, Boolean>(profile, false));
        }

        return newInstance(profileCheckList, isMultiselect, description, dialogIdentifier);
    }

    /**
     * Gets a new instance of the GirafProfileSelector
     *
     * @param profileCheckList a list of pairs of profiles and a status indicating if they should be selected
     * @param isMultiselect    should it be possible to select multiple profiles
     * @param description      the description on the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    private static GirafProfileSelectorDialog newInstance(List<Pair<Profile, Boolean>> profileCheckList, boolean isMultiselect, String description, int dialogIdentifier) {
        GirafProfileSelectorDialog girafProfileSelectorDialog = new GirafProfileSelectorDialog();

        // Store the identifier of the profiles to make it parcelable
        int[] profileIds = new int[profileCheckList.size()];
        boolean[] profilesCheckedStatus = new boolean[profileCheckList.size()];


        for (int i = 0; i < profileCheckList.size(); i++) {
            profileIds[i] = profileCheckList.get(i).first.getId();
            profilesCheckedStatus[i] = profileCheckList.get(i).second;
        }

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putIntArray(PROFILE_IDS_TAG, profileIds);
        args.putBooleanArray(PROFILE_CHECK_STATUS_TAG, profilesCheckedStatus);
        args.putBoolean(IS_MULTI_SELECT_TAG, isMultiselect);
        args.putString(DESCRIPTION_TAG, description);
        args.putInt(DIALOG_IDENTIFIER_TAG, dialogIdentifier);

        // Store the bundle
        girafProfileSelectorDialog.setArguments(args);

        return girafProfileSelectorDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Check if the activity using the fragment implements the needed interface
        try {
            profileListener = (OnReturnProfilesListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnReturnProfilesListener interface");
        }

        // Sets the helper of the activity
        helper = new Helper(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Fetch the arguments
        final Bundle args = this.getArguments();
        boolean isMultiselect = args.getBoolean(IS_MULTI_SELECT_TAG);
        String description = args.getString(DESCRIPTION_TAG);
        final int dialogIdentifier = args.getInt(DIALOG_IDENTIFIER_TAG);
        int[] profileIds = args.getIntArray(PROFILE_IDS_TAG);
        boolean[] profilesCheckedStatus = args.getBooleanArray(PROFILE_CHECK_STATUS_TAG);


        // Inflate the grid container
        gridContainer = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.giraf_profile_selector_dialog_gridview, null);
        final GridView profileGrid = (GridView) gridContainer.findViewById(R.id.profile_grid);

        // Write title according to if it is multiselect
        if (isMultiselect) {
            setTitle("Vælg profiler");
        } else {
            setTitle("Vælg profil");
        }

        setDescription(description); // Set the description
        setCustomView(gridContainer); // Set the custom view on the dialog

        profileGrid.setEmptyView(gridContainer.findViewById(R.id.loading_profiles_indicator));

        new LoadUsers(profileIds, profilesCheckedStatus, isMultiselect, dialogIdentifier).execute();

        return dialog;
    }
}
