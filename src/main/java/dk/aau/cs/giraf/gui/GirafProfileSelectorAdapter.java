package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.dblib.models.Pictogram;
import dk.aau.cs.giraf.dblib.models.Profile;

/**
 * Created by on 22/04/15.
 */
public class GirafProfileSelectorAdapter extends BaseAdapter {

    private final Context context;
    private List<Pair<Profile,Boolean>> checkedProfileList;

    // private final LayoutInflater inflater;

    public GirafProfileSelectorAdapter(Context context, List<Profile> profileList) {

        this.checkedProfileList = new ArrayList<Pair<Profile, Boolean>>();

        for(Profile profile : profileList) {
            this.checkedProfileList.add(new Pair<Profile, Boolean>(profile, false));
        }

        this.context = context;
    }

    @Override
    public int getCount() {
        return checkedProfileList.size();
    }

    @Override
    public Profile getItem(int position) {
        return checkedProfileList.get(position).first;
    }

    @Override
    public long getItemId(int position) {
        return checkedProfileList.get(position).first.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Pair<Profile,Boolean> profilePair = checkedProfileList.get(position);
        Profile profile = profilePair.first;
        GirafPictogramItemView itemView;

        if(convertView == null) {
            if (profile.getImage() != null) {
                itemView = new GirafPictogramItemView(context, profile, profile.getName());
            }
            else {
                Pictogram pictogram = new Pictogram(profile.getName(),
                        0,
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.no_profile_pic));
                itemView = new GirafPictogramItemView(context, pictogram, profile.getName());
            }
        } else {
            itemView = (GirafPictogramItemView)convertView;
            itemView.resetPictogramView();

            if(profile.getImage() == null) {
                Pictogram pictogram = new Pictogram(profile.getName(),
                        0,
                        BitmapFactory.decodeResource(context.getResources(), R.drawable.no_profile_pic));
                itemView.setImageModel(pictogram);

            } else {
                itemView.setImageModel(profile);
            }
            itemView.setTitle(profile.getName());
        }

        itemView.setChecked(profilePair.second);

        return itemView;
    }

    public void setItemChecked(int position, boolean checked) {
        Pair<Profile,Boolean> oldProfilePair = checkedProfileList.get(position);
        Pair<Profile,Boolean> newProfilePair = new Pair<Profile,Boolean>(oldProfilePair.first,checked);
        checkedProfileList.set(position, newProfilePair);
    }

    public void toggleItemChecked(int position) {
        Pair<Profile,Boolean> oldProfilePair = checkedProfileList.get(position);
        Pair<Profile,Boolean> newProfilePair = new Pair<Profile,Boolean>(oldProfilePair.first,!oldProfilePair.second);

        checkedProfileList.remove(position);
        checkedProfileList.add(position,newProfilePair);
    }

    public List<Pair<Profile,Boolean>> getCheckedProfileList() {
        return checkedProfileList;
    }

}

