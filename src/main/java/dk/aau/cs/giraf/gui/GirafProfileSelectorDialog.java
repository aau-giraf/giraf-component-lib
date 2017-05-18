package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import dk.aau.cs.giraf.librest.requests.GetArrayRequest;
import dk.aau.cs.giraf.librest.requests.GetRequest;
import dk.aau.cs.giraf.librest.requests.RequestQueueHandler;
import dk.aau.cs.giraf.models.core.User;
import dk.aau.cs.giraf.models.core.authentication.Role;

import java.util.ArrayList;
import java.util.List;
//import dk.aau.cs.giraf.models.core.authentication.PermissionType;


public class GirafProfileSelectorDialog extends GirafDialog {

    private static final String SELECTED_CITIZEN_ID_TAG = "SELECTED_CITIZEN_ID_TAG";
    private static final String PROFILE_IDS_TAG = "PROFILE_IDS_TAG";
    private static final String IS_MULTI_SELECT_TAG = "IS_MULTI_SELECT_TAG";
    private static final String DIALOG_IDENTIFIER_TAG = "DIALOG_IDENTIFIER_TAG";
    private static final String DESCRIPTION_TAG = "DESCRIPTION_TAG";
    private static final String WARNING_TAG = "WARNING_TAG";
    private static final String PROFILE_CHECK_STATUS_TAG = "PROFILE_CHECK_STATUS_TAG";

    private static List<User> userList;

    private RequestQueue queue;

    private RelativeLayout gridContainer; // The customView of the dialog
    private Activity callBack;

    /**
     * When multiselect is disabled this method will be called
     */
    public interface OnSingleProfileSelectedListener {
        /**
         * Gets the selected profiles
         *
         * @param dialogIdentifier the identifier of the dialog
         */
        public void onProfileSelected(int dialogIdentifier, User profile);

    }

    // An instance of the confirmation interface user
    OnSingleProfileSelectedListener profileListener;

    /**
     * When multiselect is enabled this method will be called
     */
    public interface OnMultipleProfilesSelectedListener {
        /**
         * Gets the selected profiles
         *
         * @param dialogIdentifier the identifier of the dialog
         */
        public void onProfilesSelected(int dialogIdentifier,List<Pair<User, Boolean>> checkedProfileList);

    }

    // An instance of the confirmation interface user
    OnMultipleProfilesSelectedListener profilesListener;

    /**
     * An async task that fetches profiles from the database given ids,
     * it also sets the adapter and builds listeners on the profile grid
     */
    private class LoadUsers extends AsyncTask<Void, Void, List<User>> {
        private String[] profileUsernames;
        private boolean[] profilesCheckedStatus;
        private boolean selectMultipleProfiles;
        private int dialogIdentifier;
        private long selectedCitizenId;
        private String selectedCitizenUsername;

        /**
         * Creates an async task to load users from the
         *
         * @param profileUsernames       an array of profile usernames
         * @param profilesCheckedStatus  an array of profile statuses
         * @param selectMultipleProfiles indicates if the dialog should select multiple profiles
         * @param dialogIdentifier       the identifier of the dialog
         */
        public LoadUsers(String[] profileUsernames, boolean[] profilesCheckedStatus, boolean selectMultipleProfiles, int dialogIdentifier,
                         String selectedCitizenUsername) {
            this.selectedCitizenUsername = selectedCitizenUsername;
            this.profileUsernames = profileUsernames;
            this.profilesCheckedStatus = profilesCheckedStatus;
            this.selectMultipleProfiles = selectMultipleProfiles;
            this.dialogIdentifier = dialogIdentifier;
        }

        @Override
        protected List<User> doInBackground(Void... params) {
            // A list of profiles
            final ArrayList<User> profiles = new ArrayList<User>();

            // Fill the list of profiles using the helper and the ids
            for (final String profileUsername : profileUsernames) {

                GetRequest<User> userGetRequest = new GetRequest<User>(profileUsername, User.class, new Response.Listener<User>() {
                    @Override
                    public void onResponse(User response) {
                        // Get the row and column size for the grids in the AppViewPager
                        profiles.add(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode == 404){
                            Log.e("Giraf","User with id: " + profileUsername + " not found");
                        }
                        else if(error.networkResponse.statusCode == 401){
                                Log.e("Giraf","Access denied while retrieving user " + profileUsername);
                        }

                    }
                });

                queue.add(userGetRequest);
            }
            return profiles;
        }

        @Override
        protected void onPostExecute(List<User> profiles) {
            super.onPostExecute(profiles);

            // Create the adapter using the profiles found in doInBackground
            final GirafProfileSelectorAdapter profileAdapter = new GirafProfileSelectorAdapter(getActivity(), profiles);

            // Find the profile grind in the custom view
            final GridView profileGrid = (GridView) gridContainer.findViewById(R.id.profile_grid);

            // Set the adapter of the profile grid
            profileGrid.setAdapter(profileAdapter);

            // Run throug all items in adapter
            for (int i = 0; i < profileAdapter.getCount(); i++) {

                // If the profile was checked and multiselect is enabled set the item to be checked
                if (profilesCheckedStatus[i] && selectMultipleProfiles) {
                    profileAdapter.setItemChecked(i, true);

                    // show warning if selectedCitizen is chosen
                    if (selectedCitizenUsername != "" && selectedCitizenUsername == profileAdapter.getItemUsername(i)) {
                        if (profileAdapter.getItemChecked(i)) {
                            showWarning();
                        } else {
                            hideWarning();
                        }
                    }
                }
            }

            // Check if multiselect is enabled
            if (selectMultipleProfiles) {

                // Enable selection of profiles
                profileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        profileAdapter.toggleItemChecked(position); // Toggle the item checked on the adapter
                        ((GirafPictogramItemView) view).toggle(); // Enforce an visual update on the profile grid (mark them)

                        // show warning if selectedCitizen is chosen
                        if (selectedCitizenId != -1 && selectedCitizenUsername == profileAdapter.getItemUsername(position)) {
                            if (profileAdapter.getItemChecked(position)) {
                                showWarning();
                            } else {
                                hideWarning();
                            }
                        }
                    }
                });

                // Add a button that ends the selection
                GirafButton selectButton = new GirafButton(getActivity(), getActivity().getResources().getDrawable(R.drawable.icon_accept), "Vælg");

                // When the button is clicked called the interface method to return checked profiles
                selectButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Check if the activity using the fragment implements the needed interface
                        try {
                            profilesListener = (OnMultipleProfilesSelectedListener) callBack;
                        } catch (ClassCastException e) {
                            throw new ClassCastException(callBack.toString() + " must implement OnMultipleProfilesSelectedListener interface");
                        }

                        // Call the method in the activity that returns the selected profiles
                        profilesListener.onProfilesSelected(dialogIdentifier, profileAdapter.getCheckedProfileList());

                        // Dismiss the dialog
                        dismiss();
                    }
                });

                // Add the button to the dialog
                addButton(selectButton);

            } else {
                // Return the first selected item
                profileGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        // Check if the activity using the fragment implements the needed interface
                        try {
                            profileListener = (OnSingleProfileSelectedListener) callBack;
                        } catch (ClassCastException e) {
                            throw new ClassCastException(callBack.toString() + " must implement OnSingleProfileSelectedListener interface");
                        }

                        // Find the selectedProfile
                        User selectedProfile = (User) profileGrid.getAdapter().getItem(position);

                        // Call the method in the activity that returns the selected profile
                        profileListener.onProfileSelected(dialogIdentifier, selectedProfile);
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
     * @param guardianUser       the identifier of the guardian of which citizens should be selectable
     * @param includeGuardian  should the guardian with the given identifier be included in the list of selectable profiles
     * @param selectMultipleProfiles    should it be possible to select multiple profiles
     * @param description      the description on the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    public static GirafProfileSelectorDialog newInstance(Context context, User guardianUser,
                                                         boolean includeGuardian, boolean selectMultipleProfiles,
                                                         String description, int dialogIdentifier) {
        Log.e("TestComponent1", " we are inside line 258");
        return newInstance(context, guardianUser, "", includeGuardian, selectMultipleProfiles, description, "", dialogIdentifier);
    }

    /**
     * Gets a new instance of the GirafProfileSelector
     *
     * @param context          the context of where the dialog appears
     * @param guardianUser       the identifier of the guardian of which citizens should be selectable
     * @param selectedCitizenUsername        The current selected child, if any (default "" if not set)
     * @param includeGuardian  should the guardian with the given identifier be included in the list of selectable profiles
     * @param selectMultipleProfiles    should it be possible to select multiple profiles
     * @param description      the description on the dialog
     * @param warning           the warning text in the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    //ToDO Fix non-used input
    public static GirafProfileSelectorDialog newInstance(Context context, final User guardianUser, String selectedCitizenUsername,
                                                         final boolean includeGuardian, boolean selectMultipleProfiles,
                                                         String description, String warning, int dialogIdentifier)
    {
        //RequestQueue queue = RequestQueueHandler.getInstance(context.getApplicationContext()).getRequestQueue();
        // Find the guardian
        List<Pair<User, Boolean>> profileCheckList = new ArrayList<Pair<User, Boolean>>();

        Log.e("TestComponent2", "We are about to addAll on line 285");
        if (guardianUser.isRole(Role.Guardian)){
            userList.addAll(guardianUser.getGuardianOf());

            for (User profile : userList) {
                profileCheckList.add(new Pair<User, Boolean>(profile, false));
            }
        }

        /*User guardian = helper.profilesHelper.getById(guardianID);
        // Find the department of the guardian
        Department guardianDepartment = helper.departmentsHelper.getDepartmentsByProfile(guardian);

        // Find the profiles of the citizens of a department and sub departments
        List<User> profiles = helper.profilesHelper.getChildrenByDepartmentAndSubDepartments(guardianDepartment);*/

        return newInstance(profileCheckList, selectMultipleProfiles, description, warning, dialogIdentifier, selectedCitizenUsername);
    }

    private void setQueue(RequestQueue requestQueue){
        this.queue = requestQueue;
    }

    /**
     * Gets a new instance of the GirafProfileSelector
     *
     * @param profileCheckList a list of pairs of profiles and a status indicating if they should be selected
     * @param selectMultipleProfiles    should it be possible to select multiple profiles
     * @param description      the description on the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    public static GirafProfileSelectorDialog newInstance(List<Pair<User, Boolean>> profileCheckList,
                                                         boolean selectMultipleProfiles, String description, String warning,
                                                         int dialogIdentifier, String selectedCitizenUsername) {
        GirafProfileSelectorDialog girafProfileSelectorDialog = new GirafProfileSelectorDialog();
        girafProfileSelectorDialog.setQueue(
            RequestQueueHandler.getInstance(
                girafProfileSelectorDialog.getActivity().getApplication().getApplicationContext()).getRequestQueue());

        // Store the identifier of the profiles to make it parcelable
        String[] profileUsernames = new String[profileCheckList.size()];
        boolean[] profilesCheckedStatus = new boolean[profileCheckList.size()];


        for (int i = 0; i < profileCheckList.size(); i++) {
            profileUsernames[i] = profileCheckList.get(i).first.getId();
            profilesCheckedStatus[i] = profileCheckList.get(i).second;
        }

        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putString(SELECTED_CITIZEN_ID_TAG, selectedCitizenUsername);
        args.putStringArray(PROFILE_IDS_TAG, profileUsernames);
        args.putBooleanArray(PROFILE_CHECK_STATUS_TAG, profilesCheckedStatus);
        args.putBoolean(IS_MULTI_SELECT_TAG, selectMultipleProfiles);
        args.putString(DESCRIPTION_TAG, description);
        args.putString(WARNING_TAG, warning);
        args.putInt(DIALOG_IDENTIFIER_TAG, dialogIdentifier);

        // Store the bundle
        girafProfileSelectorDialog.setArguments(args);

        return girafProfileSelectorDialog;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        callBack = activity;

        // Sets the helper of the activity
        //helper = new Helper(callBack);

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // Fetch the arguments
        final Bundle args = this.getArguments();
        final String selectedCitizenUsername = args.getString(SELECTED_CITIZEN_ID_TAG);
        boolean selectMultipleProfiles = args.getBoolean(IS_MULTI_SELECT_TAG);
        String description = args.getString(DESCRIPTION_TAG);
        String warning = args.getString(WARNING_TAG);
        final int dialogIdentifier = args.getInt(DIALOG_IDENTIFIER_TAG);
        String[] profileUsernames = args.getStringArray(PROFILE_IDS_TAG);
        boolean[] profilesCheckedStatus = args.getBooleanArray(PROFILE_CHECK_STATUS_TAG);


        // Inflate the grid container
        gridContainer = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.giraf_profile_selector_dialog_gridview, null);
        final GridView profileGrid = (GridView) gridContainer.findViewById(R.id.profile_grid);

        // Write title according to if it is multiselect
        if (selectMultipleProfiles) {
            setTitle("Vælg profiler");
        } else {
            setTitle("Vælg profil");
        }

        setDescription(description); // Set the description
        setWarningText(warning); // Set the description
        setCustomView(gridContainer); // Set the custom view on the dialog

        profileGrid.setEmptyView(gridContainer.findViewById(R.id.loading_profiles_indicator));

        // Create the cancel button
        GirafButton cancelButton = new GirafButton(getActivity(),getActivity().getResources().getDrawable(R.drawable.icon_cancel),"Luk");

        // Make the dialog dissmiss when clicking on the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        addButton(cancelButton);

        new LoadUsers(profileUsernames, profilesCheckedStatus, selectMultipleProfiles, dialogIdentifier, selectedCitizenUsername).execute();

        return dialog;
    }
}
