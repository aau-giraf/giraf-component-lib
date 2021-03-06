package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GWidgetConnectivity extends ImageView implements IGWidget {

	private final String mPreString;
	private String mPostString = "";
	private final Context mContext;
	private final String mOnlineString;
	private final String mSyncingString;
	private final String mOfflineString;
	private TextView mTooltipTextview;
	private _GTooltip GTip;
	
	private void setStyle(int d) {
		this.setBackgroundDrawable(getResources().getDrawable(d));
	}
	
	private void setStyleOnline() {
		this.setStyle(R.drawable.ggiraficon_online);
		mPostString = mOnlineString;
	}
	
	private void setStyleSyncing() {
		this.setStyle(R.drawable.ggiraficon_syncing);
		mPostString = mSyncingString;
	}
	
	private void setStyleOffline() {
		this.setStyle(R.drawable.ggiraficon_offline);
		mPostString = mOfflineString;
	}
	
	private void setInitialStyle() {
		this.setStyleOffline();
		mPostString = mOfflineString;
	}

	public GWidgetConnectivity(Context context) {
		super(context);
		this.setInitialStyle();
		
		mContext = context;
		mPreString = mContext.getResources().getString(R.string.mPreString);
		mOnlineString = mContext.getResources().getString(R.string.mOnlineString);
		mOfflineString = mContext.getResources().getString(R.string.mOfflineString);
		mSyncingString = mContext.getResources().getString(R.string.mSyncingString);
	}

	public GWidgetConnectivity(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setInitialStyle();

		mContext = context;
		mPreString = mContext.getResources().getString(R.string.mPreString);
		mOnlineString = mContext.getResources().getString(R.string.mOnlineString);
		mOfflineString = mContext.getResources().getString(R.string.mOfflineString);
		mSyncingString = mContext.getResources().getString(R.string.mSyncingString);
	}

	public GWidgetConnectivity(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setInitialStyle();

		mContext = context;
		mPreString = mContext.getResources().getString(R.string.mPreString);
		mOnlineString = mContext.getResources().getString(R.string.mOnlineString);
		mOfflineString = mContext.getResources().getString(R.string.mOfflineString);
		mSyncingString = mContext.getResources().getString(R.string.mSyncingString);
	}

	@Override
	public void updateDisplay() {
		/*switch (helper.serverHelper.getStatus()) {
		case 0:
			setStyleOffline();
			break;
		case 1:
			setStyleOnline();
			break;
		case 2:
			setStyleSyncing();
			break;
		}*/

        setStyleOffline();
		
		if (mTooltipTextview != null) {
			mTooltipTextview.setText(generateTooltipString());
			GTip.setView(mTooltipTextview);
		}
	}
	
	private void showTooltip(){
		_GTooltip g = new _GTooltip(mContext, generateTooltipString(), Color.WHITE);
		g.setRightOf(this);
		g.show();
		
		GTip = g;
		
	}

	private String generateTooltipString() {
		return mPreString +" "+ mPostString;
	}


	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		this.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showTooltip();
				
			}
		});
	}

}
