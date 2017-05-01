package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import dk.aau.cs.giraf.models.core.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by on 22/04/15.
 */
public class GirafProfileSelectorAdapter extends BaseAdapter {

    private final Context context;
    private List<Pair<User,Boolean>> checkedProfileList;

    // private final LayoutInflater inflater;

    public GirafProfileSelectorAdapter(Context context, List<User> profileList) {

        this.checkedProfileList = new ArrayList<Pair<User, Boolean>>();

        for(User profile : profileList) {
            this.checkedProfileList.add(new Pair<User, Boolean>(profile, false));
        }

        this.context = context;
    }

    @Override
    public int getCount() {
        return checkedProfileList.size();
    }

    @Override
    public User getItem(int position) {
        return checkedProfileList.get(position).first;
    }

    @Override
    public long getItemId(int position) {
        return checkedProfileList.get(position).first.getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Pair<User,Boolean> profilePair = checkedProfileList.get(position);
        User profile = profilePair.first;
        GirafPictogramItemView itemView;

        if(convertView == null) {
            itemView = new GirafPictogramItemView(context, profile, context.getResources().getDrawable(R.drawable.no_profile_pic), profile.getUsername());
        } else {
            itemView = (GirafPictogramItemView)convertView;
            itemView.resetPictogramView();

            itemView.setImageModel(profile, context.getResources().getDrawable(R.drawable.no_profile_pic));
            itemView.setTitle(profile.getName());
        }

        if (profile.getRole().ordinal() < Profile.Roles.CHILD.ordinal()) {
            itemView.setIndicatorOverlayDrawable(
                    context.getResources().getDrawable(R.drawable.icon_guardian_shield)
            );
        }

        itemView.setChecked(profilePair.second);

        return itemView;
    }

    public void setItemChecked(int position, boolean checked) {
        Pair<User,Boolean> oldProfilePair = checkedProfileList.get(position);
        Pair<User,Boolean> newProfilePair = new Pair<User,Boolean>(oldProfilePair.first,checked);
        checkedProfileList.set(position, newProfilePair);
    }

    public boolean getItemChecked(int position) {
        return checkedProfileList.get(position).second;
    }

    public void toggleItemChecked(int position) {
        Pair<User,Boolean> oldProfilePair = checkedProfileList.get(position);
        Pair<User,Boolean> newProfilePair = new Pair<User,Boolean>(oldProfilePair.first,!oldProfilePair.second);

        checkedProfileList.remove(position);
        checkedProfileList.add(position, newProfilePair);
    }

    public List<Pair<User,Boolean>> getCheckedProfileList() {
        return checkedProfileList;
    }

    public List<User> getSelectedProfiles() {
        ArrayList<User> profiles = new ArrayList<User>();

        for (Pair<User, Boolean> pair : checkedProfileList) {
            if (pair.second) {
                profiles.add(pair.first);
            }
        }

        return profiles;
    }

}

