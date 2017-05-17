package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created on 26/03/15.
 */
public class GirafSpinnerAdapter<T> extends ArrayAdapter<T> {

    public GirafSpinnerAdapter(final Context context, final List<T> items) {
        super(context, R.layout.giraf_spinner_item, items);
    }

    @Override
    public final View getView(final int position, final View convertView, final ViewGroup parent) {
        final TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItemName(getItem(position)));
        view.setGravity(Gravity.CENTER);
        return view;
    }

    @Override
    public View getDropDownView(final int position, final View convertView, final ViewGroup parent) {
        return super.getView(position, convertView, parent);
    }

    /**
     * This method should be overridden, to return the name of the item, if item.toString is not the desired name of the item.
     *
     * @param item item in the list
     * @return the name of this item
     */
    public String getItemName(final T item) {
        return item.toString();
    }

}
