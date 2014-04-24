package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.NinePatch;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.StateSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

/**
 * Created by Jelly on 22-04-14.
 */
public class GSeekBar extends RelativeLayout {

    SeekBar seeker;

    public GSeekBar (Context context)
    {
        super(context);
        CreateSeekBar(context);
    }

    public GSeekBar (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        CreateSeekBar(context);
    }

    public GSeekBar (Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        CreateSeekBar(context);
    }

    private void CreateSeekBar(Context context)
    {
        /***
         * The reason for containing a seekbar rather than deriving from a seekbar
         * is that in order for the thumb (button) to be centered horizontally to
         * the progress bar, android:maxHeight must be set. This is not possible
         * programmatically though, so that sucks. XML it is.
         */
        LayoutInflater.from(context).inflate(R.layout.gseekbar, this);
        seeker = (SeekBar)findViewById(R.id.internalSeekbar);

        //Le colors
        int[] thumbColors = { GStyler.sliderThumbColor, GStyler.sliderThumbColor};
        int[] progressColors = { GStyler.sliderProgressColor, GStyler.sliderProgressColor };
        int[] unProgressColors = { GStyler.sliderUnProgressColor, GStyler.sliderUnProgressColor };

        //The thumb (button you drag)
        final GradientDrawable thumbDrawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, thumbColors);
        thumbDrawable.setStroke(2, GStyler.calculateGradientColor(GStyler.sliderThumbColor));

        //Progress bar to the left and right of the thumb
        GradientDrawable progressDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, progressColors);
        GradientDrawable unProgressDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, unProgressColors);
        progressDrawable.setStroke(2, GStyler.calculateGradientColor(GStyler.sliderProgressColor));
        unProgressDrawable.setStroke(2, GStyler.calculateGradientColor(GStyler.sliderProgressColor));
        progressDrawable.setCornerRadius(4);
        unProgressDrawable.setCornerRadius(4);

        //The seeker
        ClipDrawable clip = new ClipDrawable(progressDrawable, Gravity.LEFT,ClipDrawable.HORIZONTAL);

        InsetDrawable padding = new InsetDrawable(unProgressDrawable,0,3,0,3);

        LayerDrawable progressLayer = new LayerDrawable(new Drawable[]{padding,clip});

        seeker.setProgressDrawable(progressLayer);

        //Can't get the size for the thumb yet, so hook a preDraw listener to set the thumb size
        final ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                thumbDrawable.setSize(GStyler.dpToPixel(10, seeker.getContext()), seeker.getHeight());
                seeker.setThumb(thumbDrawable);

                if (vto.isAlive())
                    vto.removeOnPreDrawListener(this);
                return true;
            }
        });
    }

    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener)
    {
        seeker.setOnSeekBarChangeListener(listener);
    }
}
