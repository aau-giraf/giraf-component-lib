package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class _GTooltip extends Dialog {
	
	private _GTooltip mTip;
	private Context mContext;
	private final int padding = 5;
	private ViewGroup mViewGroup;
    private String text;
    private int color;
    private TextView tv;

	public _GTooltip(Context context, String text, int color) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		mContext = context;
		this.setStyle();
		mTip = this;

        this.text = text;
        this.color = color;

        this.tv = new TextView(context);
        tv.setText(text);
        tv.setTextColor(color);

        mTip.setView(tv);

		this.findViewById(R.id.tooltip_hitarea).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mTip.cancel();
			}
		});

	}

	private _GTooltip(Context context, int theme) {
		super(context, theme);
		mContext = context;
		this.setStyle();
	}

	private _GTooltip(Context context, boolean cancelable,
                      OnCancelListener cancelListener, View v) {
		super(context, cancelable, cancelListener);
		mContext = context;
		this.setStyle();
	}
	
	public void setStyle() {
		this.setContentView(R.layout._gtooltip_layout);
		mViewGroup = (ViewGroup) this.findViewById(R.id.tooltip_content);
	}


	public void setRightOf(View v) {
		View target = this.findViewById(R.id.gtooltip_arrow_view);

        int[] pos = new int[2];
        v.getLocationOnScreen(pos);

        pos[0] += v.getMeasuredWidth() +0.5f;
        pos[1] += v.getMeasuredHeight()/2 + mViewGroup.getMeasuredHeight()/2;

        tv.setText(String.valueOf(target.getBottom()));

        target.setX(pos[0]);
        target.setY(pos[1]);

        //this.tv.setText(String.valueOf(v.getHeight()) + "-" + String.valueOf(pos[1]));

        mViewGroup.setX(pos[0]);
        mViewGroup.setY(pos[1]);
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
