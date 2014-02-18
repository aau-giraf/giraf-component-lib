package dk.aau.cs.giraf.gui;

import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;


public class GColorAdapter extends BaseAdapter {

	private Integer[] mColors;
	private Context mContext;
	private static LayoutInflater mInflater = null;

	public GColorAdapter(Context context) {
		super();

		mContext = context;
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		Resources appResources = context.getResources();
		int[] colorsRaw = appResources.getIntArray(R.array.appcolors);
		mColors = new Integer[colorsRaw.length];

		for (int i = 0; i < colorsRaw.length; i++) {
			mColors[i] = colorsRaw[i];
		}
	}

	@Override
	public int getCount() {
		return mColors.length;
	}

	@Override
	public Object getItem(int arg0) {
		return mColors[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int color = (Integer) this.getItem(position);
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.colorpicker, null);
		}

		ImageView colorView = (ImageView) convertView.findViewById(R.id.color_bg);

		GradientDrawable colorGradient = (GradientDrawable) mContext.getResources().getDrawable(R.drawable.colorborder);
		colorGradient.setColor(color);

		colorView.setBackgroundDrawable(colorGradient);

		convertView.setOnTouchListener(new ColorDragger(color));
		return convertView;
	}

	private final class ColorDragger implements OnTouchListener {
		private int mColor;

		public ColorDragger(int c){
			this.mColor = c;
		}

		@Override
		public boolean onTouch(View v, MotionEvent e) {
			boolean result;
			
			switch(e.getActionMasked()) {
			case MotionEvent.ACTION_DOWN:
				ClipData data = ClipData.newPlainText("color", Integer.toString(mColor));
				DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
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
