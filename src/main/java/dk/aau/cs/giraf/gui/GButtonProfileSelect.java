package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import dk.aau.cs.giraf.dblib.controllers.ProfileController;
import dk.aau.cs.giraf.dblib.models.Profile;

/**
 * Created by AndersBender on 08-05-14.
 */
public class GButtonProfileSelect extends GButton {
    //Default Icon
    private Drawable myIcon = getResources().getDrawable(R.drawable.no_profile_pic);
    //Fields
    private GProfileSelector profileSelector;
    private Profile currentProfile;
    private Profile guardianProfile;
    private boolean profilesLoaded = false;
    private GButtonProfileSelect gButtonProfileSelect;
    private onCloseListener myOnCloseListener;
    private boolean addGuardianToList = true;

    public interface onCloseListener {
        void onClose(Profile guardianProfile, Profile currentProfile);
    }

    //Getters
    public Profile getCurrentProfile()
    {
        return currentProfile;
    }

    public Profile getGuardianProfile()
    {
        return guardianProfile;
    }

    //Constructors (Setting Standard image and a self reference)
    public GButtonProfileSelect(Context context) {
        super(context);
        this.SetImage(myIcon);
        gButtonProfileSelect = this;
    }

    public GButtonProfileSelect(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.SetImage(myIcon);
        gButtonProfileSelect = this;
    }

    public GButtonProfileSelect(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gButtonProfileSelect = this;
        this.SetImage(myIcon);
    }

    //Update the image on the button
    private void updateImage()
    {
        if(currentProfile != null && currentProfile.getImage() != null)
        {
            BitmapDrawable profilePicture = new BitmapDrawable(currentProfile.getImage());
            this.SetImage(profilePicture);
        }
        else if(currentProfile == null && guardianProfile.getImage() != null)
        {
            BitmapDrawable profilePicture = new BitmapDrawable(guardianProfile.getImage());
            this.SetImage(profilePicture);
        }

    }

    //Setup the button with the custom listener from the user, the guardian profile and the current profile
    public void setup(Profile incGuardianProfile, Profile incCurrentProfile, onCloseListener myListener)
    {
        //Ensures the argument inGuardianProfile is actually a guardian
        if(incGuardianProfile != null)
        {
            if(incGuardianProfile.getRole() == Profile.Roles.GUARDIAN)
            {
                this.guardianProfile = incGuardianProfile;
                profilesLoaded = true;
            }
            else
            {
                Log.e("Error", "You must select a guardian profile!");
            }
            if(incCurrentProfile != null)
            {
                this.currentProfile = incCurrentProfile;
            }
            if(profilesLoaded == true)
            {

                updateImage();
                myOnCloseListener = myListener;

                //Set the onClickListener for the Button
                this.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        //Set the onListItemClickListener for the List in the profileSelector
                        profileSelector = new GProfileSelector(getContext(), guardianProfile, currentProfile, addGuardianToList);
                        profileSelector.setOnListItemClick(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                //Create controller to find Profile by the id
                                ProfileController profileController = new ProfileController(getContext());
                                //see if the pressed user is a guardian, and if so set currentProfile to null and update guardian
                                if(profileController.getProfileById((int)id).getRole() == Profile.Roles.GUARDIAN)
                                {
                                    currentProfile = null;
                                    guardianProfile = profileController.getProfileById((int)id);
                                }
                                else
                                {
                                    currentProfile = profileController.getProfileById((int)id);
                                }
                                gButtonProfileSelect.updateImage();
                                //Closing the profileselector
                                profileSelector.dismiss();
                            }
                        });
                        //set the OnDismissListener to call the users onCloseListener
                        profileSelector.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                closing();
                            }
                        });
                        profileSelector.show();
                    }
                });
            }
        }
        else{Log.e("Error", "Your selected guardian is null");}

    }

    public void GuardianSelectableInList(boolean selectable)
    {
        addGuardianToList = selectable;
    }

    //Method used to call the users onCloseListener
    public void closing()
    {
        myOnCloseListener.onClose(guardianProfile, currentProfile);
    }
}
