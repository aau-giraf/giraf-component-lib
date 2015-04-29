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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.dblib.controllers.ProfileController;
import dk.aau.cs.giraf.dblib.models.Profile;

/**
 * <p></p>
 * @deprecated Old selector use {@link dk.aau.cs.giraf.gui.GirafProfileSelectorDialog} instead.
 */
public class GMultiProfileSelector extends GDialog{

    //Fields
    protected GradientDrawable picBackground;
    protected GList theList;
    private GVerifyButton verifyButton;
    private GCancelButton cancelButton;
    List<Long> allProfileindexes = new ArrayList<Long>();
    List<Long> selectedProfileindexes = new ArrayList<Long>();
    ProfileController profileController;
    private onCloseListener myOnCloseListener;

    //Custom onClose Listener
    public interface onCloseListener {
        void onClose(List<Profile> selectedProfiles);
    }

    //Constructor
    public GMultiProfileSelector(Context context, List<Profile> allProfiles,  List<Profile> selectedProfiles)
    {
        super(context);

        //Disable the Dialogs ability to close when pressed on background
        try{
        this.backgroundCancelsDialog(false);
        }
        catch(Exception e)
        {}

        //Instantiate the ProfileController  for the Database usage
        profileController = new ProfileController((Activity) context);

        //Inflate Views
        View completeView = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_multiselector, null);

        //Find the different components
        theList = (GList) completeView.findViewById(R.id.profileSelectorList);
        verifyButton = (GVerifyButton) completeView.findViewById(R.id.verifyButton);
        cancelButton = (GCancelButton) completeView.findViewById(R.id.cancelButton);

        //Set the OnClickListener for the verifyButton
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Fetch the Adapter from the List.
                GMultiSelectAdapter multiSelectAdapter = (GMultiSelectAdapter) theList.getAdapter();
                //Clear the selected List and fill with the newfound selected indexes
                selectedProfileindexes.clear();
                for(int i = 0; i < multiSelectAdapter.data.size(); i++)
                {
                    if(multiSelectAdapter.data.get(i).getToggled())
                    {
                       selectedProfileindexes.add(multiSelectAdapter.data.get(i).getId());
                    }
                }
                //Call the function notifying the Listeners
                closing();
                //Close the Dialog
                cancel();
            }
        });
        //Attach the onClick event for the cancel button
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        //Get the ID's from all the Profiles
        for(int i = 0; i < allProfiles.size() ; i++)
        {
            allProfileindexes.add(allProfiles.get(i).getId());
        }

        //Get the ID's from all the Profiles which should be toggled in the dialog (if any)
        if(selectedProfiles != null)
        {
            for(int i = 0; i < selectedProfiles.size() ; i++)
            {
                selectedProfileindexes.add(selectedProfiles.get(i).getId());
            }
        }

        //Create the adapter and attach
        GMultiSelectAdapter selectorAdapter = new GMultiSelectAdapter((Activity) context, allProfileindexes, selectedProfileindexes);
        theList.setAdapter(selectorAdapter);
        this.SetView(completeView);
    }

    public void closing()
    {
        //Transform the id's to profiles
        List<Profile> selectedProfiles = new ArrayList<Profile>();
        for (int i = 0; i < selectedProfileindexes.size(); i++)
        {
            selectedProfiles.add(profileController.getProfileById(selectedProfileindexes.get(i)));
        }
        //Notify listeners
        myOnCloseListener.onClose(selectedProfiles);
    }

    public void setMyOnCloseListener(onCloseListener myOnCloseListener) {
        this.myOnCloseListener = myOnCloseListener;
    }
}
