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

    private Activity activity;
    public List<MultiSelectDataWrapper> data = new ArrayList<MultiSelectDataWrapper>();
    private static LayoutInflater inflater=null;
    private int currentPosition = 0;
    private ProfileController profileController;

    public GMultiSelectAdapter(Activity a, List<Integer> indexes, List<Integer> toggledIndexes) {
        activity = a;
        profileController = new ProfileController(a.getApplicationContext());
        for(int i = 0; i < indexes.size(); i++)
        {
            data.add(new MultiSelectDataWrapper(i, indexes.get(i)));
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
        currentPosition = ((GList) parent).getFirstVisiblePosition() +position;
        //Inflate our view (gshowrow) if it's null
        if(convertView==null)
        {
            vi = new GToggleButton(activity.getApplicationContext());
            ((GToggleButton) vi).setToggled(true);
             vi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((GToggleButton) v).isToggled())
                    {
                        int i = ((GList)v.getParent()).getPositionForView(v);
                       data.get(i).setToggled(true);
                    }
                    else
                    {
                        int i = ((GList)v.getParent()).getPositionForView(v);
                        data.get(i).setToggled(false);
                    }
                }
            });
        }
     /*   BitmapDrawable profilePicture = new BitmapDrawable(profileController.getProfileById(data.get(position).getId()).getImage());
        if(profilePicture != null)
        {
        ((GToggleButton) vi).setCompoundDrawablesWithIntrinsicBounds(profilePicture, null, null, null);
        }*/
        ((GToggleButton) vi).setText(profileController.getProfileById(data.get(position).getId()).getName());
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



    public class MultiSelectDataWrapper
    {
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