package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;

public class GCancelButton extends GIconButton {

	
	public GCancelButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setIcon(this.getResources().getDrawable(R.drawable.cancel_icon));
	}

	public GCancelButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setIcon(this.getResources().getDrawable(R.drawable.cancel_icon));
	}

	public GCancelButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.setIcon(this.getResources().getDrawable(R.drawable.cancel_icon));
	}

}
