package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Jelly on 25-03-14.
 */
public class GToggleButton extends GButton {

    private boolean toggled = false;
    private OnClickListener task;
    final GToggleButton btn;

    public GToggleButton(Context context)
    {
        super(context);
        btn = this;
        setOnClickListener(null);
    }

    public GToggleButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        btn = this;
        Setup();
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);

        this.setToggled(context.obtainStyledAttributes(attrs, R.styleable.GToggleButton).getBoolean(R.styleable.GToggleButton_Toggled, false));
    }

    public GToggleButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        btn = this;
        Setup();
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);

        this.setToggled(context.obtainStyledAttributes(attrs, R.styleable.GToggleButton).getBoolean(R.styleable.GToggleButton_Toggled, false));
    }

    private void Setup()
    {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toggle();
            }
        });
        this.setBackgroundDrawable(null);
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

        if (isToggled())
            this.setBackgroundDrawable(penisPressed);
        else
            this.setBackgroundDrawable(penisUnPressed);


    }

    public void setToggled(boolean state)
    {
        toggled = state;

        if (isToggled())
            this.setBackgroundDrawable(penisPressed);
        else
            this.setBackgroundDrawable(penisUnPressed);
    }
}
