package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * <p></p>
 * @deprecated Replaced by {@link dk.aau.cs.giraf.gui.GirafButton}. Remember to set the type to LOG_OUT
 */
@Deprecated
public class GWidgetLogout extends ImageView implements IGWidget {
	
	private void setStyle() {
		this.setBackgroundDrawable(getResources().getDrawable(R.drawable.glogout_icon));
	}

	public GWidgetLogout(Context context) {
		super(context);
		setStyle();
	}

	public GWidgetLogout(Context context, AttributeSet attrs) {
		super(context, attrs);
		setStyle();
	}

	public GWidgetLogout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setStyle();
	}

	@Override
	public void updateDisplay() {
		// none needed
	}

}
