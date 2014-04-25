package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.SeekBar;

/**
 * Created by Jelly on 24-04-14.
 */
public class GSwitch extends GSeekBar {

    private GSwitch me;
    private boolean toggled = false;
    private int offSet;
    private OnClickListener task;

    public GSwitch (Context context)
    {
        super(context);
        me = this;
    }

    public GSwitch (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        me = this;
    }

    public GSwitch (Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        me = this;
    }


    @Override
    protected void CreateSeekBar(Context context)
    {
        final int padAmount = 4;
        /***
         * The reason for containing a seekbar rather than deriving from a seekbar
         * is that in order for the thumb (button) to be centered horizontally to
         * the progress bar, android:maxHeight must be set. This is not possible
         * programmatically though, so that sucks. XML it is.
         */
        LayoutInflater.from(context).inflate(R.layout.gseekbar, this);
        seeker = (SeekBar)findViewById(R.id.internalSeekbar);

        //The colors
        final int[] thumbColors = { GStyler.sliderThumbColor, GStyler.sliderThumbColor};
        final int[] progressColors = { GStyler.calculateGradientColor(GStyler.sliderProgressColor, 0.9f), GStyler.calculateGradientColor(GStyler.sliderProgressColor,0.9f) };
        final int[] unProgressColors = { GStyler.sliderUnProgressColor, GStyler.sliderUnProgressColor };

        //The thumb (button you drag)
        final ShapeDrawable thumbDrawable = new ShapeDrawable(new OvalShape());
        thumbDrawable.getPaint().setColor(thumbColors[0]);
        thumbDrawable.getPaint().setStyle(Paint.Style.FILL);


        //Progress bar to the left and right of the thumb
        GradientDrawable progressDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, progressColors);
        GradientDrawable unProgressDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, unProgressColors);

        //Both progressbars are essentially rendered, the progress ontop of the unprogress
        //This makes the progress clip-able
        InsetDrawable padding = new InsetDrawable(unProgressDrawable,0,padAmount,0,padAmount);
        ClipDrawable clip = new ClipDrawable(progressDrawable, Gravity.LEFT,ClipDrawable.HORIZONTAL);

        //combining into a single drawable
        LayerDrawable progressLayer = new LayerDrawable(new Drawable[]{padding,clip});

        seeker.setProgressDrawable(progressLayer);



        //Can't get the size for the thumb yet, so hook a preDraw listener to set the thumb size
        final ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                thumbDrawable.setIntrinsicWidth(seeker.getHeight()-padAmount*2);
                thumbDrawable.setIntrinsicHeight(seeker.getHeight()-padAmount*2);
                offSet = seeker.getHeight();
                seeker.setPadding(seeker.getHeight()/2, 0, seeker.getHeight()/2, 0);
                seeker.setThumb(thumbDrawable);

                //Drawing background
                Bitmap bmResult = Bitmap.createBitmap(me.getWidth(), me.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bmResult);
                Paint paint = new Paint();
                paint.setShader(new LinearGradient(0, 0, bmResult.getWidth()/2, 0, unProgressColors[0], unProgressColors[0], Shader.TileMode.MIRROR));
                canvas.drawPaint(paint);
                paint.setShader(new LinearGradient (0, 0, bmResult.getWidth()/2, 0, progressColors[0], progressColors[0], Shader.TileMode.CLAMP));
                canvas.drawRect(0, 0, bmResult.getWidth()/2, bmResult.getHeight(), paint);
                bmResult = GStyler.getRoundedCornerBitmap(bmResult, GStyler.calculateGradientColor(progressColors[0]), offSet/2, padAmount, getResources());
                BitmapDrawable test = new BitmapDrawable(getResources(), bmResult);

                me.setBackgroundDrawable(test);

                if (vto.isAlive())
                    vto.removeOnPreDrawListener(this);
                return true;
            }
        });

        seeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            long checkTime;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                checkTime = SystemClock.uptimeMillis();
            }

            @Override
            public void onStopTrackingTouch(final SeekBar seekBar) {
                if (SystemClock.uptimeMillis() - checkTime > 100)
                    setProgress(getProgress());
                else
                    Toggle();
            }
        });
    }

    @Override
    public void setOnSeekBarChangeListener(SeekBar.OnSeekBarChangeListener listener)
    {

    }

    @Override
    public void setOnClickListener(OnClickListener task)
    {
        this.task = task;
    }

    public void Toggle()
    {
        if (toggled)
        {
            seeker.setProgress(0);
            toggled = false;
        }
        else
        {
            seeker.setProgress(100);
            toggled = true;
        }

        if (task != null) task.onClick(this);
    }

    @Override
    public void setProgress(int progress)
    {
        boolean stateChanged = false;

        if (seeker.getProgress() > (toggled ? 80 : 20))
        {
            seeker.setProgress(100);
            if (!toggled) stateChanged = true;
            toggled = true;

        }
        else
        {
            seeker.setProgress(0);
            if (toggled) stateChanged = true;
            toggled = false;
        }

        if (stateChanged)
            if (task != null) task.onClick(this);
    }

    public boolean isToggled()
    {
        return toggled;

    }

}
