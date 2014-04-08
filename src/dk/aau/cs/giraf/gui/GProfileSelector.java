package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created by AndersBender on 03-04-14.
 */
public class GProfileSelector extends GDialog {

    protected GradientDrawable picBackground;

    public GProfileSelector(Context context, List<Profile> profileList, Profile currentProfile)
    {
        super(context);

        View w = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_selector, null);
        View o = LayoutInflater.from(this.getContext()).inflate(R.layout.gprofile_row, null);
        GList theList = (GList) w.findViewById(R.id.profileSelectorList);
        LinearLayout i =(LinearLayout) w.findViewById(R.id.profileSelectorButton);
        LinearLayout s = (LinearLayout) o.findViewById(R.id.profile_pic);
        picBackground = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
                         new int[] {GStyler.calculateGradientColor(GStyler.calculateGradientColor(GStyler.buttonBaseColor)),GStyler.calculateGradientColor(GStyler.calculateGradientColor(GStyler.buttonBaseColor))});
        picBackground.setCornerRadius(5);
        s.setPadding(GStyler.dpToPixel(3, w.getContext())
                , GStyler.dpToPixel(3, w.getContext())
                , GStyler.dpToPixel(3, w.getContext())
                , GStyler.dpToPixel(3, w.getContext()));

        s.setBackgroundDrawable(picBackground);
        i.addView(o);

        GProfileAdapter profileAdapter = new GProfileAdapter((Activity) context, profileList);

        theList.setAdapter(profileAdapter);


        this.SetView(w);
    }
}
