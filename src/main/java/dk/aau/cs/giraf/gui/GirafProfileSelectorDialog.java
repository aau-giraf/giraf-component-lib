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

    private RelativeLayout gridContainer; // The customView of the dialog
    private Activity callBack;
    private User currentUser;

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
     * Gets a new instance of the GirafProfileSelector
     *
     * @param context          the context of where the dialog appears
     * @param guardianUser       the identifier of the guardian of which citizens should be selectable
     * @param includeGuardian  should the guardian with the given identifier be included in the list of selectable profiles
     * @param description      the description on the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    public static GirafProfileSelectorDialog newInstance(Context context, User guardianUser,
                                                         boolean includeGuardian, String description, int dialogIdentifier) {

        return newInstance(context, guardianUser, "", includeGuardian, description, "", dialogIdentifier);
    }

    /**
     * Gets a new instance of the GirafProfileSelector
     *
     * @param context          the context of where the dialog appears
     * @param guardianUser       the identifier of the guardian of which citizens should be selectable
     * @param selectedCitizenUsername        The current selected child, if any (default "" if not set)
     * @param includeGuardian  should the guardian with the given identifier be included in the list of selectable profiles
     * @param description      the description on the dialog
     * @param warning           the warning text in the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    //ToDO Fix non-used input
    public static GirafProfileSelectorDialog newInstance(Context context, final User guardianUser, String selectedCitizenUsername,
                                                         final boolean includeGuardian,
                                                         String description, String warning, int dialogIdentifier)
    {

        return newInstance(guardianUser, description, warning, dialogIdentifier, selectedCitizenUsername);
    }

    /**
     * Gets a new instance of the GirafProfileSelector
     *
     * @param guardianUser the guardianUser;
     * @param description      the description on the dialog
     * @param dialogIdentifier a unique identifier of the dialog
     * @return a GirafProfileSelector
     */
    public static GirafProfileSelectorDialog newInstance(User guardianUser, String description, String warning,
                                                         int dialogIdentifier, String selectedCitizenUsername) {
        GirafProfileSelectorDialog girafProfileSelectorDialog = new GirafProfileSelectorDialog();
        girafProfileSelectorDialog.setCurrentUser(guardianUser);


        // Create the argument bundle
        Bundle args = new Bundle();

        // Store the arguments into the bundle
        args.putString(SELECTED_CITIZEN_ID_TAG, selectedCitizenUsername);
        args.putString(DESCRIPTION_TAG, description);
        args.putString(WARNING_TAG, warning);
        args.putInt(DIALOG_IDENTIFIER_TAG, dialogIdentifier);

        // Store the bundle
        girafProfileSelectorDialog.setArguments(args);

        return girafProfileSelectorDialog;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
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
        String description = args.getString(DESCRIPTION_TAG);
        String warning = args.getString(WARNING_TAG);
        final int dialogIdentifier = args.getInt(DIALOG_IDENTIFIER_TAG);

        // Inflate the grid container
        gridContainer = (RelativeLayout) getActivity().getLayoutInflater().inflate(R.layout.giraf_profile_selector_dialog_gridview, null);
        final GridView profileGrid = (GridView) gridContainer.findViewById(R.id.profile_grid);
        setTitle("Vælg profil");

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



        // Create the adapter using the profiles found in doInBackground
        final GirafProfileSelectorAdapter profileAdapter;
            if(currentUser.isRole(Role.Guardian) && currentUser.getGuardianOf() != null) {
                profileAdapter = new GirafProfileSelectorAdapter(getActivity(), currentUser.getGuardianOf());
            } else{
              profileAdapter = new GirafProfileSelectorAdapter(getActivity(), new ArrayList<User>());
            }

        // Set the adapter of the profile grid
        profileGrid.setAdapter(profileAdapter);



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
        return dialog;
    }
}
