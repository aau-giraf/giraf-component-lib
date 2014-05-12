package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created by AndersBender on 03-04-14.
 */
public class GProfileSelector extends GDialog {

    protected GradientDrawable picBackground;
    protected GList theList;
    private Profile guardianProfile;
    private Profile currentProfile;
    private boolean placeGuardianInList = true;
    private Context context;

    public GProfileSelector(Context context, Profile guardianProfile, Profile currentProfile)
    {
        super(context);
        this.guardianProfile = guardianProfile;
        this.currentProfile = currentProfile;
        this.context = context;
        setup();
    }

    public GProfileSelector(Context context, Profile guardianProfile, Profile currentProfile, boolean placeGuardianInList)
    {
        super(context);
        this.guardianProfile = guardianProfile;
        this.currentProfile = currentProfile;
        this.placeGuardianInList = placeGuardianInList;
        this.context = context;
        setup();
    }

    //Setup
    private void setup()
    {

        //Inflate Views
        View completeView = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_selector, null);
        View currentProfileView = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_row, null);

        //Find the different components
        theList = (GList) completeView.findViewById(R.id.profileSelectorList);
        LinearLayout currentProfileLayer =(LinearLayout) completeView.findViewById(R.id.currentProfileLayer);
        LinearLayout profileLayout = (LinearLayout) currentProfileView.findViewById(R.id.profile_pic);
        ImageView profilePicture = (ImageView) profileLayout.findViewById(R.id.imageview_profilepic);
        GTextView currentPersonTextView = (GTextView) currentProfileView.findViewById(R.id.profile_name);

        //Checks to see whether the profile is guardian or a child (if the current profile == null it's a guardian)
        if(currentProfile == null)
        {
            currentPersonTextView.setText(guardianProfile.getName());
            if(guardianProfile.getImage() != null)
            {
                profilePicture.setImageBitmap(guardianProfile.getImage());
            }
        }
        else
        {
            currentPersonTextView.setText(currentProfile.getName());
            if(currentProfile.getImage() != null)
            {
                profilePicture.setImageBitmap(currentProfile.getImage());
            }
        }


        //Create a back dynamic background to the profile picture accordingly to GStyler
        picBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                new int[] {GStyler.calculateGradientColor(GStyler.calculateGradientColor(GStyler.buttonBaseColor)),GStyler.calculateGradientColor(GStyler.calculateGradientColor(GStyler.buttonBaseColor))});
        picBackground.setCornerRadius(5);
        profileLayout.setPadding(GStyler.dpToPixel(3, completeView.getContext())
                , GStyler.dpToPixel(3, completeView.getContext())
                , GStyler.dpToPixel(3, completeView.getContext())
                , GStyler.dpToPixel(3, completeView.getContext()));
        profileLayout.setBackgroundDrawable(picBackground);

        //Add the inflated currentProfileView to the currentProfileLayer
        currentProfileLayer.addView(currentProfileView);

        ProfileController profileController = new ProfileController(getContext());
        //Create and set the adapter to the list
        if(guardianProfile.getRole() == Profile.Roles.GUARDIAN && currentProfile != null)
        {

            List<Profile> profileList = new ArrayList<Profile>();
            if(placeGuardianInList == true)
            {
                profileList.add(guardianProfile);
            }
            profileList.addAll(profileController.getChildrenByGuardian(guardianProfile));
            GProfileAdapter profileAdapter = new GProfileAdapter((Activity) context, profileList);
            theList.setAdapter(profileAdapter);
        }
        else if(guardianProfile.getRole() == Profile.Roles.GUARDIAN && currentProfile == null)
        {
            List<Profile> profileList = profileController.getChildrenByGuardian(guardianProfile);
            GProfileAdapter profileAdapter = new GProfileAdapter((Activity) context, profileList);
            theList.setAdapter(profileAdapter);
        }
        else
        {
            Log.e("Error", "You must select a guardian profile!");
        }
        //Set the completeview to the Dialog
        this.SetView(completeView);

        //Set the ability to close the dialog by clicking next to it
        try
        {
            this.backgroundCancelsDialog(true);
        }
        catch(Exception e){}

    }
    /**
     * Used to set the OnItemClickListener to the List from outside the Dialog
     * @param listener The Listener desired for the List's OnItemClickListener
     */
    public void setOnListItemClick(AdapterView.OnItemClickListener listener)
    {
        theList.setOnItemClickListener(listener);
    }
}
