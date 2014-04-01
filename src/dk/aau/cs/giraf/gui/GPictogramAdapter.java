package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.shapes.Shape;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import java.io.InputStream;
import java.util.List;

import dk.aau.cs.giraf.oasis.lib.models.Pictogram;
import dk.aau.cs.giraf.oasis.lib.models.Profile;

/**
 * Created by AndersBender on 28-03-14.
 */
public class GPictogramAdapter extends BaseAdapter {

private Activity activity;
private List<Pictogram> data;
private static LayoutInflater inflater=null;

        public GPictogramAdapter(Activity a, List<Pictogram> d) {
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
                    vi = inflater.inflate(R.layout.gpictogram_box, null);

            //Find TextView and ImageView
            TextView name = (TextView)vi.findViewById(R.id.pictogram_name); // title
            ImageView thumb_image=(ImageView)vi.findViewById(R.id.pictogram_picture); // thumb image

            //Find layout to change background color
            LinearLayout pictogramlayout = (LinearLayout)vi.findViewById(R.id.pictogram_layout);
            GradientDrawable bgShape = (GradientDrawable) pictogramlayout.getBackground();
            bgShape.setColor(GStyler.calculateGradientColor(GStyler.gridBaseColor));

            //Get Data from the pictogram in the list
            Pictogram pictogram = data.get(position);

            // Setting all values
            name.setText(pictogram.getName());

            //Attach the Listener to the pictogramlayout
            pictogramlayout.setOnTouchListener(new ImageDragger(pictogram));
            return vi;
        }

    private final class ImageDragger implements View.OnTouchListener {
        private Pictogram pictogram;

        public ImageDragger(Pictogram p){
            this.pictogram = p;
        }

        @Override
        public boolean onTouch(View v, MotionEvent e) {
            boolean result;

            switch(e.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    ClipData data = ClipData.newPlainText("PictogramID", String.valueOf(pictogram.getId()));
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                    v.startDrag(data, shadowBuilder, v, 0);
                    result = true;
                    break;
                default:
                    result = false;
            }

            return result;
        }

    }

}
