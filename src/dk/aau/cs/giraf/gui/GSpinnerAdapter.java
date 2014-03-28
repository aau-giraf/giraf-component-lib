package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.lang.reflect.GenericArrayType;
import java.util.List;

/**
 * Created by Jelly on 28-03-14.
 */
public class GSpinnerAdapter extends ArrayAdapter {

    public GSpinnerAdapter(Context context, List objects)
    {
        super(context, R.layout.gspinner_item, objects);
    }

    public GSpinnerAdapter(Context context, String[] objects)
    {
        super(context, R.layout.gspinner_item, objects);
    }

    public static GSpinnerAdapter createFromResource(Context context, int textArrayResId)
    {
        return (GSpinnerAdapter)ArrayAdapter.createFromResource(context, textArrayResId, R.layout.gspinner_item);
    }

}
