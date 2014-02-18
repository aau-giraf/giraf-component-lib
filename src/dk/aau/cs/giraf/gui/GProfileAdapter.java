package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class GProfileAdapter extends BaseAdapter {
	
	private Activity activity;
    private List<Profile> data;
    private static LayoutInflater inflater=null;
    
    public GProfileAdapter(Activity a, List<Profile> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
	
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return data.get(position);
    }
 
    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.gprofile_row, null);
 
        TextView name = (TextView)vi.findViewById(R.id.profile_name); // title
//        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image
        
        Profile profile = data.get(position);
 
        // Setting all values in listview
        name.setText(profile.getFirstname()+" "+profile.getSurname());
        //imageLoader.DisplayImage(song.get(CustomizedListView.KEY_THUMB_URL), thumb_image);
        return vi;
    }
    

}
