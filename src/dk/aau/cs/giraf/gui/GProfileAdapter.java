package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

import dk.aau.cs.giraf.oasis.lib.models.Profile;

public class GProfileAdapter extends BaseAdapter {
	
	private Activity activity;
    private List<Profile> data;
    private static LayoutInflater inflater=null;
    protected GradientDrawable stylePressed;
    protected GradientDrawable styleUnPressed;
    protected GradientDrawable picBackground;
    
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
        return data.get(position).getId();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.gprofile_row, null);

        ImageView profilePicture = (ImageView)vi.findViewById(R.id.profile_pic);

            if(data.get(position).getImage() != null)
            {
                profilePicture.setImageBitmap(data.get(position).getImage());
            }

        StateListDrawable stateListDrawable = new StateListDrawable();

        //default colors
        int[] colors = GStyler.getColors(GStyler.buttonBaseColor);

        //colors when pressed
        int[] colorsPressed = new int[2];
        colorsPressed[0] = colors[1];
        colorsPressed[1] = GStyler.calculateGradientColor(colorsPressed[0]);

        GTextView name = (GTextView)vi.findViewById(R.id.profile_name); // title
//        ImageView thumb_image=(ImageView)vi.findViewById(R.id.list_image); // thumb image

        Profile profile = data.get(position);


        View profileBackground = vi.findViewById(R.id.profile_pic);
        profileBackground.setPadding(GStyler.dpToPixel(3, vi.getContext())
                , GStyler.dpToPixel(3, vi.getContext())
                , GStyler.dpToPixel(3, vi.getContext())
                , GStyler.dpToPixel(3, vi.getContext()));

        picBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {colorsPressed[1],colorsPressed[1]});
        picBackground.setCornerRadius(5);

        profileBackground.setBackgroundDrawable(picBackground);
        // Setting all values in listview
        name.setText(profile.getName());
        //make the two gradients
        styleUnPressed = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colors);
        stylePressed = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, colorsPressed);

        //round corners and give edges
        styleUnPressed.setCornerRadius(10);
        styleUnPressed.setStroke(3, GStyler.calculateGradientColor(colors[0], 0.75f));
        stylePressed.setCornerRadius(10);
        stylePressed.setStroke(3, GStyler.calculateGradientColor(colorsPressed[0], 0.75f));

        //set state_pressed to gdPressed and all others to gd
        stateListDrawable.addState(new int[] {android.R.attr.state_selected, android.R.attr.state_pressed}, stylePressed);
        stateListDrawable.addState(new int[] {android.R.attr.state_selected}, stylePressed);
        stateListDrawable.addState(new int[] {android.R.attr.state_pressed}, stylePressed);
        stateListDrawable.addState(new int[] {-android.R.attr.state_pressed}, styleUnPressed);

        vi.setPadding(GStyler.dpToPixel(10, vi.getContext())
                , GStyler.dpToPixel(5, vi.getContext())
                , GStyler.dpToPixel(10, vi.getContext())
                , GStyler.dpToPixel(5, vi.getContext()));

        vi.setBackgroundDrawable(stateListDrawable);
        return vi;
    }

}
