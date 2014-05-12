package dk.aau.cs.giraf.gui;

import android.content.Context;

import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jelly on 25-03-14.
 */
public class GToggleButton extends GButton {

    private boolean toggled = false;
    private boolean isSetup = false;
    private OnClickListener task;
    final GToggleButton btn;

    public GToggleButton(Context context)
    {
        super(context);
        btn = this;
        setOnClickListener(null);
        Setup();
    }

    public GToggleButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        btn = this;
        Setup();
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);

        toggled = context.obtainStyledAttributes(attrs, R.styleable.GToggleButton).getBoolean(R.styleable.GToggleButton_Toggled, false);
    }

    public GToggleButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        btn = this;
        Setup();
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);

        toggled = context.obtainStyledAttributes(attrs, R.styleable.GToggleButton).getBoolean(R.styleable.GToggleButton_Toggled, false);
    }

    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w,h,oldw,oldh);
        SetBackground();
    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);
        if (!isSetup)
        {
            SetBackground();

            isSetup = true;
        }
    }

    private void Setup()
    {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toggle();
            }
        });
    }

    @Override
    public void setOnClickListener(OnClickListener task)
    {
        this.task = task;
    }

    public boolean isToggled()
    {
        return toggled;

    }

    public void Toggle()
    {
        toggled = !toggled;
        if (task != null) task.onClick(this);

        SetBackground();
    }

    public void setToggled(boolean state)
    {
        toggled = state;

        SetBackground();
    }

    private void SetBackground()
    {
        if (isToggled())
            this.setBackgroundDrawable(stylePressed);
        else
            this.setBackgroundDrawable(styleUnPressed);
    }
}
