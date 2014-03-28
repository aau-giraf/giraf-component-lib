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

    public GSpinnerAdapter(Context context, CharSequence[] objects)
    {
        super(context, R.layout.gspinner_item, objects);
    }

    public GSpinnerAdapter(Context context, int textArrayResId)
    {
        this(context, context.getResources().getTextArray(textArrayResId));
    }

}
