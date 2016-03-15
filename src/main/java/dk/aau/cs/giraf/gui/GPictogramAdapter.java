package dk.aau.cs.giraf.gui;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;

import dk.aau.cs.giraf.dblib.controllers.BaseImageControllerHelper;
import dk.aau.cs.giraf.dblib.models.Pictogram;

/**
 * Created by AndersBender on 28-03-14.
 */
public class GPictogramAdapter extends BaseAdapter {

private Activity activity;
private List<Pictogram> data;
private static LayoutInflater inflater=null;
    private BaseImageControllerHelper helper;

        public GPictogramAdapter(Activity a, List<Pictogram> d) {
            activity = a;
            data=d;
            helper = new BaseImageControllerHelper(a);
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

            //Attach listener to the pictogramlayout to handle dragging of the pictogram
            pictogramlayout.setOnTouchListener(new ImageDragger(pictogram));
            //THis should set the picture of the ImageView to the Pictogram picture
            //Function is not implemented yet, so a placeholder is used instead
            if(helper.getImage(pictogram) != null)
            {
                  thumb_image.setImageBitmap(helper.getImage(pictogram));
            }

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
