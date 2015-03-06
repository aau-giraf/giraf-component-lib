package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * <p></p>
 * @deprecated Old button use {@link dk.aau.cs.giraf.gui.GirafButton} instead. Remember to set the logout icon
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
