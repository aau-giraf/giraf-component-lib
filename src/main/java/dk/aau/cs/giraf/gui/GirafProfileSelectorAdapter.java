package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import dk.aau.cs.giraf.models.core.User;
import dk.aau.cs.giraf.models.core.authentication.Role;

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


    /**
     * This is hacky method due to override, user getItemUsername instead
     * @param position the items position
     * @return -1
     */
    @Override
    public long getItemId(int position) {
        return -1;
    }


    public String getItemUsername(int position){
        return checkedProfileList.get(position).first.getUsername();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Pair<User,Boolean> profilePair = checkedProfileList.get(position);
        User profile = profilePair.first;
        GirafUserItemView itemView;

        if(convertView == null) {
            itemView = new GirafUserItemView(context, profile, context.getResources().getDrawable(R.drawable.no_profile_pic), profile.getScreenName());
        } else {
            itemView = (GirafUserItemView)convertView;
            itemView.resetPictogramView();

            itemView.setImageModel(profile, context.getResources().getDrawable(R.drawable.no_profile_pic));
            itemView.setTitle(profile.getScreenName());
        }

        //if (profile.getRole().ordinal() < Profile.Roles.CHILD.ordinal()) {
        if (!profile.isRole(Role.User)) {

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

