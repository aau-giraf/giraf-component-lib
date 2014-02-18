package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class GTooltip extends Dialog {
	
	private GTooltip mTip;
	private Context mContext;
	private final int padding = 5;
	private ViewGroup mViewGroup;

	public GTooltip(Context context) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		this.setStyle();
		mTip = this;
		this.findViewById(R.id.tooltip_hitarea).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mTip.cancel();
			}
		});

	}

	private GTooltip(Context context, int theme) {
		super(context, theme);
		mContext = context;
		this.setStyle();
	}

	private GTooltip(Context context, boolean cancelable,
			OnCancelListener cancelListener, View v) {
		super(context, cancelable, cancelListener);
		mContext = context;
		this.setStyle();
	}
	
	public void setStyle() {
		this.setContentView(R.layout.gtooltip_layout);
		mViewGroup = (ViewGroup) this.findViewById(R.id.tooltip_content);
	}
	
	public void setRightOf(View v) {
		View target = this.findViewById(R.id.gtooltip_arrow_view);
		
		int parentMarginLeft = ((RelativeLayout.LayoutParams) ((ViewGroup) v.getParent()).getLayoutParams()).leftMargin;
		int newLeft = intToDP(v.getLeft() + v.getWidth() + padding) + parentMarginLeft;
		int newTop = intToDP(v.getTop() + (v.getHeight()/2) - 12); 
	
		android.widget.RelativeLayout.LayoutParams p = (android.widget.RelativeLayout.LayoutParams) target.getLayoutParams();
		p.setMargins(newLeft, newTop, 0, 0);
		target.setLayoutParams(p);
		

	}
	
	private int intToDP(int i) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, i, mContext.getResources().getDisplayMetrics());
	}

	public void setView(View v){
		
		mViewGroup.removeAllViewsInLayout();
		v.measure(0, 0);
		RelativeLayout.LayoutParams lp = (LayoutParams) mViewGroup.getLayoutParams();
		int height = mViewGroup.getPaddingBottom()+mViewGroup.getPaddingTop()+v.getMeasuredHeight();
		
		int y = -((height/2) -12);
		lp.setMargins(0, y, 0, 0);
		
		mViewGroup.addView(v);		
	}
}
