package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import java.util.List;

import dk.aau.cs.giraf.oasis.lib.models.Pictogram;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created by on 22/04/15.
 */
public class GriafProfileSelectorAdapter extends BaseAdapter {

    private final Context context;
    private List<Profile> profileList;
    // private final LayoutInflater inflater;

    public GriafProfileSelectorAdapter(Context context, List<Profile> profileList) {
        this.profileList = profileList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return profileList.size();
    }

    @Override
    public Object getItem(int position) {
        return profileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return profileList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Profile profile = profileList.get(position);
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
        return itemView;
    }
}

