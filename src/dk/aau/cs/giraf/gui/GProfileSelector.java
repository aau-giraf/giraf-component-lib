package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
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

    public GProfileSelector(Context context, Profile guardianProfile)
    {
        super(context);
            //Inflate Views
        View completeView = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_selector, null);
        View currentProfileView = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_row, null);

        //Find the different components
        theList = (GList) completeView.findViewById(R.id.profileSelectorList);
        LinearLayout currentProfileLayer =(LinearLayout) completeView.findViewById(R.id.currentProfileLayer);
        LinearLayout profilePicture = (LinearLayout) currentProfileView.findViewById(R.id.profile_pic);
        GTextView currentPersonTextView = (GTextView) currentProfileView.findViewById(R.id.profile_name);
        currentPersonTextView.setText(guardianProfile.getName());

        //Create a back dynamic background to the profile picture accordingly to GStyler
        picBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                         new int[] {GStyler.calculateGradientColor(GStyler.calculateGradientColor(GStyler.buttonBaseColor)),GStyler.calculateGradientColor(GStyler.calculateGradientColor(GStyler.buttonBaseColor))});
        picBackground.setCornerRadius(5);
        profilePicture.setPadding(GStyler.dpToPixel(3, completeView.getContext())
                , GStyler.dpToPixel(3, completeView.getContext())
                , GStyler.dpToPixel(3, completeView.getContext())
                , GStyler.dpToPixel(3, completeView.getContext()));
        profilePicture.setBackgroundDrawable(picBackground);

        //Add the inflated currentProfileView to the currentProfileLayer
        currentProfileLayer.addView(currentProfileView);

        ProfileController profileController = new ProfileController(getContext());
       //create and set the adapter to the list
        GProfileAdapter profileAdapter = new GProfileAdapter((Activity) context, profileController.getChildrenByGuardian(guardianProfile));
        theList.setAdapter(profileAdapter);

        //Set the completeview to the Dialog
        this.SetView(completeView);
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
