package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class GGridView extends GridView {

	private void setStyle() {
		this.setBackgroundResource(R.drawable.grow);
	}

	public GGridView(Context context) {
		super(context);
		this.setStyle();
		// TODO Auto-generated constructor stub
	}

	public GGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStyle();
		// TODO Auto-generated constructor stub
	}

	public GGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStyle();
		// TODO Auto-generated constructor stub
	}

}
