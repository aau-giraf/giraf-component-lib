package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
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
    private boolean drawableSetup = false;
    private String onText = "";
    private String offText = "";

    public GSwitch (Context context)
    {
        super(context);
        me = this;
    }

    public GSwitch (Context context, AttributeSet attrs)
    {
        super(context, attrs);
        me = this;
        onText = context.obtainStyledAttributes(attrs, R.styleable.GSwitch).getString(R.styleable.GSwitch_OnText);
        offText = context.obtainStyledAttributes(attrs, R.styleable.GSwitch).getString(R.styleable.GSwitch_OffText);
        if (onText == null) onText = "";
        if (offText == null) offText = "";
    }

    public GSwitch (Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        me = this;
        onText = context.obtainStyledAttributes(attrs, R.styleable.GSwitch).getString(R.styleable.GSwitch_OnText);
        offText = context.obtainStyledAttributes(attrs, R.styleable.GSwitch).getString(R.styleable.GSwitch_OffText);
        if (onText == null) onText = "";
        if (offText == null) offText = "";
    }

    @Override
    protected void CreateSeekBar(Context context)
    {
        final int padAmount = GStyler.dpToPixel(4, getContext());
        /***
         * The reason for containing a seekbar rather than deriving from a seekbar
         * is that in order for the thumb (button) to be centered horizontally to
         * the progress bar, android:maxHeight must be set. This is not possible
         * programmatically though, so that sucks. XML it is.
         */
        LayoutInflater.from(context).inflate(R.layout.gseekbar, this);
        seeker = (SeekBar)findViewById(R.id.internalSeekbar);

        //The thumb (button you drag)
        final ShapeDrawable thumbDrawable = new ShapeDrawable(new OvalShape());
        thumbDrawable.getPaint().setColor(GStyler.sliderThumbColor);
        thumbDrawable.getPaint().setStyle(Paint.Style.FILL);

        //Can't get the size for the thumb yet, so hook a preDraw listener to set the thumb size
        final ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ViewGroup.LayoutParams lp = me.getLayoutParams();

                if (me.getWidth() < me.getHeight()*2)
                    lp.width = seeker.getHeight()*2;

                if (me.getHeight() - padAmount * 2 < 15)
                    lp.height = 15 + padAmount * 2;

                me.setLayoutParams(lp);
                thumbDrawable.setIntrinsicWidth(lp.height - padAmount);
                thumbDrawable.setIntrinsicHeight(lp.height - padAmount);
                offSet = lp.height;
                seeker.setPadding(lp.height/2, 0, lp.height/2, 0);

                Bitmap thumbBmp = Bitmap.createBitmap(thumbDrawable.getIntrinsicWidth(), thumbDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(thumbBmp);
                thumbDrawable.setBounds(canvas.getClipBounds());
                thumbDrawable.draw(canvas);
                thumbBmp = GStyler.getRoundedCornerBitmap(thumbBmp, Color.WHITE, thumbBmp.getHeight()/2, GStyler.dpToPixel(1, getContext()), getResources());
                BitmapDrawable thumbBmpDrawable = new BitmapDrawable(getResources(), thumbBmp);
                seeker.setThumb(thumbBmpDrawable);
                //seeker.setThumb(thumbDrawable);

                //Drawing background
                Bitmap bmResult = Bitmap.createBitmap(lp.width, lp.height, Bitmap.Config.ARGB_8888);
                canvas = new Canvas(bmResult);
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.FILL);
                paint.setColor(GStyler.sliderUnProgressColor);
                canvas.drawRect(new Rect(0, 0, lp.width, lp.height), paint);
                paint.setColor(GStyler.sliderProgressColor);
                canvas.drawRect(new Rect(0,0, lp.height/2, lp.height), paint);

                bmResult = GStyler.getRoundedCornerBitmap(bmResult, GStyler.calculateGradientColor(GStyler.sliderProgressColor), offSet/2, padAmount/2, getResources());

                BitmapDrawable test = new BitmapDrawable(getResources(), bmResult);

                me.setBackgroundDrawable(test);

                if (me.getViewTreeObserver().isAlive())
                    me.getViewTreeObserver().removeOnPreDrawListener(this);

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
    protected void onDraw(Canvas c)
    {
        super.onDraw(c);
        if (!drawableSetup)
        {
            CreateProgressDrawables();
            drawableSetup = true;
        }
    }

    private void Resize()
    {
        CreateProgressDrawables();
        CreateBackground();
    }

    private void CreateBackground()
    {

    }

    private void CreateProgressDrawables()
    {
        int padd = GStyler.dpToPixel(4, getContext());
        int[] progressColors = { GStyler.sliderProgressColor, GStyler.sliderProgressColor};
        int[] unProgressColors = { GStyler.sliderUnProgressColor, GStyler.sliderUnProgressColor };

        //Progress bar to the left and right of the thumb
        Drawable progressDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, progressColors);
        Drawable unProgressDrawable = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, unProgressColors);

        Bitmap progressBmp = Bitmap.createBitmap(seeker.getWidth(), seeker.getHeight(), Bitmap.Config.ARGB_8888);
        Bitmap unProgressBmp = Bitmap.createBitmap(seeker.getWidth(), seeker.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(progressBmp);

        progressDrawable.setBounds(canvas.getClipBounds());
        unProgressDrawable.setBounds(canvas.getClipBounds());

        progressDrawable.draw(canvas);
        progressBmp = drawTextToBitmap(getContext(), progressBmp, onText, Paint.Align.LEFT);
        progressDrawable = new BitmapDrawable(getResources(), progressBmp);

        canvas = new Canvas(unProgressBmp);
        unProgressDrawable.draw(canvas);
        unProgressBmp = drawTextToBitmap(getContext(), unProgressBmp, offText, Paint.Align.RIGHT);
        unProgressDrawable = new BitmapDrawable(getResources(), unProgressBmp);

        //Both progressbars are essentially rendered, the progress ontop of the unprogress
        //This makes the progress clip-able
        final InsetDrawable padding = new InsetDrawable(unProgressDrawable,0,padd/2,0,padd/2);
        final ClipDrawable clip = new ClipDrawable(progressDrawable, Gravity.LEFT,ClipDrawable.HORIZONTAL);

        //combining into a single drawable
        //LayerDrawable progressLayer = new LayerDrawable(new Drawable[]{padding,clip});
        LayerDrawable progressLayer = new LayerDrawable(new Drawable[]{padding,clip});
        seeker.setProgressDrawable(progressLayer);
    }

    private Bitmap drawTextToBitmap(Context mContext,  Bitmap bitmap,  String mText, Paint.Align align)
    {
        android.graphics.Bitmap.Config bitmapConfig =   bitmap.getConfig();
        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        paint.setTextScaleX(2);
        // text color - #3D3D3D
        paint.setColor(Color.rgb(0,0, 0));
        // text size in pixels
        paint.setTextSize(GStyler.dpToPixel(20, mContext));

        paint.setTextAlign(align);


        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(mText, 0, mText.length(), bounds);

        int x = align == Paint.Align.LEFT ? 0 : bitmap.getWidth();
        int y = bounds.height() + bitmap.getHeight()/2 - (int)paint.getTextSize()/2;//bitmap.getHeight()/2 - (int)paint.getTextSize()/2;

        canvas.drawText(mText, x, y, paint);

        return bitmap;
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
        setToggled(!toggled);
    }

    @Override
    public void setProgress(int progress)
    {
        boolean stateChanged = false;

        if (progress > (toggled ? 80 : 20))
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

    public void setToggled(boolean state)
    {
        if (!state)
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

    public void setTextOn(CharSequence text)
    {
        onText = text.toString();
        CreateProgressDrawables();
        refresh();
    }

    public void setTextOff(CharSequence text)
    {
        offText = text.toString();
        CreateProgressDrawables();
        refresh();
    }

    public void refresh()
    {
        CreateProgressDrawables();
        int prog = seeker.getProgress();
        seeker.setProgress(50);
        seeker.setProgress(prog);
    }

}
