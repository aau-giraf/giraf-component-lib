package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;

public class GVerifyButton extends GIconButton {

	
	public GVerifyButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.setIcon(this.getResources().getDrawable(R.drawable.ok_icon));
	}

	public GVerifyButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.setIcon(this.getResources().getDrawable(R.drawable.ok_icon));
		
	}

	public GVerifyButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.setIcon(this.getResources().getDrawable(R.drawable.ok_icon));
	}

}
