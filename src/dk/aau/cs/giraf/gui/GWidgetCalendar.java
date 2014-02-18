package dk.aau.cs.giraf.gui;

import java.util.Calendar;
import java.util.Locale;
import java.util.Timer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class GWidgetCalendar extends TextView implements IGWidget {

	private Timer timer;
	private String day_of_week;
	private String week_num;
	private String day_of_month;
	private String month;
	private final Context mContext;
	private TextView mTooltipTextview;
	private GTooltip GTip;
	
	private void setStyle(){
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.gcal_icon));
		this.setGravity(android.view.Gravity.CENTER_HORIZONTAL);
		this.setTextSize(35);
		this.setTypeface(null, Typeface.BOLD);
		this.setTextColor(Color.rgb(102, 102, 102));
		this.setPadding(0, 16, 0, 0);
		
	}
	
	public GWidgetCalendar(Context context) {
		super(context);
		mContext = context;
		this.setStyle();
	}

	public GWidgetCalendar(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		this.setStyle();
	}

	public GWidgetCalendar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		this.setStyle();
	}
	
	public final void showTooltip(){
		GTip = new GTooltip(mContext);
		mTooltipTextview = new TextView(mContext);
		mTooltipTextview.setText(generateTooltipString());
		mTooltipTextview.setTextColor(Color.WHITE);
		
		GTip.setView(mTooltipTextview);
		GTip.setRightOf(this);
		GTip.show();
	}
	
	private String generateTooltipString(){
		return day_of_week + " den " + day_of_month + ". " + month + ", Uge " + week_num;
	}
	
	public void updateDisplay(){
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		day_of_month = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		
		int weekday = cal.get(Calendar.DAY_OF_WEEK);
		
		String[] weekdays_strings = mContext.getResources().getStringArray(R.array.week_days);
		day_of_week = weekdays_strings[weekday-1];
		week_num = Integer.toString(cal.get(Calendar.WEEK_OF_YEAR));
		
		String[] month_strings = mContext.getResources().getStringArray(R.array.months);
		int cur_month = cal.get(Calendar.MONTH);
		month = month_strings[cur_month];
		
		day_of_month = Integer.toString(cal.get(Calendar.DAY_OF_MONTH));
		this.setText(day_of_month);
		
		if(mTooltipTextview != null){
			mTooltipTextview.setText(generateTooltipString());
			GTip.setView(mTooltipTextview);
		}
	}
	
	public void onWindowFocusChanged(boolean hasFocus) {
		this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTooltip();
				
			}
		});
	}

}
