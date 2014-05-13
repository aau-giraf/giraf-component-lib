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

import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created by AndersBender on 12-05-14.
 */
public class GMultiProfileSelector extends GDialog{

    protected GradientDrawable picBackground;
    protected GList theList;
    private GVerifyButton verifyButton;
    private GCancelButton cancelButton;
    List<Integer> allProfileindexes = new ArrayList<Integer>();
    List<Integer> selectedProfileindexes = new ArrayList<Integer>();
    ProfileController profileController;
    private onCloseListener myOnCloseListener;

    public interface onCloseListener {
        void onClose(List<Profile> selectedProfiles);
    }

    public GMultiProfileSelector(Context context, List<Profile> allProfiles,  List<Profile> selectedProfiles)
    {
        super(context);

        profileController = new ProfileController((Activity) context);

        //Inflate Views
        View completeView = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_multiselector, null);

        //Find the different components
        theList = (GList) completeView.findViewById(R.id.profileSelectorList);
        verifyButton = (GVerifyButton) completeView.findViewById(R.id.verifyButton);
        cancelButton = (GCancelButton) completeView.findViewById(R.id.cancelButton);

        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               GMultiSelectAdapter multiSelectAdapter = (GMultiSelectAdapter) theList.getAdapter();
                selectedProfileindexes.clear();
                for(int i = 0; i < multiSelectAdapter.data.size(); i++)
                {
                    if(multiSelectAdapter.data.get(i).getToggled())
                    {
                    selectedProfileindexes.add(multiSelectAdapter.data.get(i).getId());
                    }
                }
                closing();
                cancel();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        for(int i = 0; i < allProfiles.size() ; i++)
        {
            allProfileindexes.add(allProfiles.get(i).getId());
        }

        if(selectedProfiles != null)
        {
            for(int i = 0; i < selectedProfiles.size() ; i++)
            {
                selectedProfileindexes.add(selectedProfiles.get(i).getId());
            }

        }


        GMultiSelectAdapter selectorAdapter = new GMultiSelectAdapter((Activity) context, allProfileindexes, selectedProfileindexes);
        theList.setAdapter(selectorAdapter);
        this.SetView(completeView);

        //Set the ability to close the dialog by clicking next to it
        try
        {
            this.backgroundCancelsDialog(true);
        }
        catch(Exception e){}
    }

    public void closing()
    {
        List<Profile> selectedProfiles = new ArrayList<Profile>();
        for (int i = 0; i < selectedProfileindexes.size(); i++)
        {
            selectedProfiles.add(profileController.getProfileById(selectedProfileindexes.get(i)));
        }
        myOnCloseListener.onClose(selectedProfiles);
    }

    public void setMyOnCloseListener(onCloseListener myOnCloseListener) {
        this.myOnCloseListener = myOnCloseListener;
    }
}
