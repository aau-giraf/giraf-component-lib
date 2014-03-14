package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.PaintDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListView;

public class GList extends ListView {

	private void setStyle() {
        int baseColor = GStyler.listBaseColor;

        //this removes the blue selection background color when an item is selected
        this.setSelector(android.R.color.transparent);
        this.setDivider(null);
        //only GradientDrawable has both setCornerRadius() and setStroke() so thus it is used
        GradientDrawable listBackground = new GradientDrawable(GradientDrawable.Orientation.BL_TR, new int[] { baseColor, baseColor});
        listBackground.setCornerRadius(GStyler.dpToPixel(10, this.getContext()));
        listBackground.setStroke(GStyler.dpToPixel(4,this.getContext()), GStyler.calculateGradientColor(baseColor));

        this.setPadding(GStyler.dpToPixel(8, this.getContext()),
                GStyler.dpToPixel(5,this.getContext()),
                GStyler.dpToPixel(8,this.getContext()),
                GStyler.dpToPixel(5,this.getContext()));

        this.setBackgroundDrawable(listBackground);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
	}

    @Override
    public void draw(Canvas canvas) {
        Path path = new Path();
        RectF r = new RectF(0, 0, this.getWidth(), this.getHeight());
        path.addRoundRect(r, 10, 10, Path.Direction.CW);
        canvas.clipPath(path);
        super.draw(canvas);
    }

	public GList(Context context) {
		super(context);
		this.setStyle();
		// TODO Auto-generated constructor stub
	}

	public GList(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setStyle();

		// TODO Auto-generated constructor stub
	}

	public GList(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.setStyle();
		// TODO Auto-generated constructor stub
	}

 /*   @Override
    public void onFinishInflate() {
        if(this.getDividerHeight())
        {
            this.setText(R.string.cancelButtonString);
        }
    }*/
}
