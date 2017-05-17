package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * <p></p>
 * @deprecated Old adapter {@link dk.aau.cs.giraf.gui.GirafSpinnerAdapter} instead.
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
