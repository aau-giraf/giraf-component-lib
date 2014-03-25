package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;

public class GGridView extends GridView {

	private void setStyle() {

        int baseColor = GStyler.gridBaseColor;

        //this removes the blue selection background color when an item is selected
        this.setSelector(android.R.color.transparent);

        //only GradientDrawable has both setCornerRadius() and setStroke() so thus it is used
        GradientDrawable gridBackground = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[] { baseColor, baseColor});
        gridBackground.setCornerRadius(GStyler.dpToPixel(10, this.getContext()));
        gridBackground.setStroke(GStyler.dpToPixel(4, this.getContext()), GStyler.calculateGradientColor(baseColor));
        this.setPadding(GStyler.dpToPixel(7, this.getContext()),
                GStyler.dpToPixel(7,this.getContext()),
                GStyler.dpToPixel(7,this.getContext()),
                GStyler.dpToPixel(7,this.getContext()));


        this.setBackgroundDrawable(gridBackground);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
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
