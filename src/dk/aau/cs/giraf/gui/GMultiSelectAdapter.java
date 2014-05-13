package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import dk.aau.cs.giraf.oasis.lib.controllers.ProfileController;

/**
 * Created by AndersBender on 12-05-14.
 */
public class GMultiSelectAdapter extends BaseAdapter {

    //Fields
    private Activity activity;
    private static LayoutInflater inflater=null;
    private ProfileController profileController;

    public List<MultiSelectDataWrapper> data = new ArrayList<MultiSelectDataWrapper>();

    //Constructor
    public GMultiSelectAdapter(Activity a, List<Integer> indexes, List<Integer> toggledIndexes) {
        //Store the activity and Create the controller handling of database
        activity = a;
        profileController = new ProfileController(a.getApplicationContext());

        //Fetch the id's from the indexes List and transform them to MultiSelectDataWrapper
        //(containing index number in list, id for a profile and toggle status)
        for(int i = 0; i < indexes.size(); i++)
        {
            data.add(new MultiSelectDataWrapper(i, indexes.get(i)));
            //If there is some ids in the toggledIndexes find which corrospond to the one's in the data list and toggle.
            if(toggledIndexes != null)
            {
                for(int x = 0; x < toggledIndexes.size(); x++)
                {

                    if(toggledIndexes.get(x).intValue() == indexes.get(i).intValue())
                    {
                        data.get(i).setToggled(true);
                    }
                }
            }
        }

        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return data.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        //Inflate our view (gshowrow) if it's null
        if(convertView==null)
        {
            //if the view is null instantiate a GToggleButton and attach a listener
            vi = new GToggleButton(activity.getApplicationContext());
            ((GToggleButton) vi).setToggled(true);
             vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((GToggleButton) v).isToggled())
                    {
                        //Get position of the toggled toggleButton and store in data
                        int i = ((GList)v.getParent()).getPositionForView(v);
                       data.get(i).setToggled(true);
                    }
                    else
                    {
                        //Get position of the detoggled toggleButton and store in data
                        int i = ((GList)v.getParent()).getPositionForView(v);
                        data.get(i).setToggled(false);
                    }
                }
            });
        }

        //Set the Text of the ToggleButtons to the Profiles name
        ((GToggleButton) vi).setText(profileController.getProfileById(data.get(position).getId()).getName());
        //Toggle the ToggleButton if the data says is should be toggled.
        if(data.get(position).getToggled())
        {
            ((GToggleButton)vi).setToggled(true);
        }
        else
        {
            ((GToggleButton)vi).setToggled(false);
        }


        return vi;
    }

//Wrapper class for the data
    public class MultiSelectDataWrapper
    {
        //Contains a index in the list, id of the profile, and if the given is toggled.
        int index;
        int id;
        boolean toggled = false;

        public MultiSelectDataWrapper(int index, int id)
        {
            this.index = index;
            this.id =  id;
        }

        public int getIndex()
        {
            return index;
        }

        public int getId()
        {
            return id;
        }

        public boolean getToggled()
        {
            return toggled;
        }

        public void setToggled(boolean setState)
        {
            toggled = setState;
        }
    }
}