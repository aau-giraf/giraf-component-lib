package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

/**
 * Created by Jelly on 25-03-14.
 */
public class GToggleButton extends GButton {

    private boolean toggled = false;
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
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);
    }

    public GToggleButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        btn = this;
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);
    }

    @Override
    public void setOnClickListener(final OnClickListener task)
    {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setToggled(!btn.isToggled());
                if (task != null)
                    task.onClick(v);
            }
        });
    }

    public boolean isToggled()
    {
        return toggled;

    }


    public void setToggled(boolean state)
    {
        toggled = state;

        if (isToggled())
            this.setBackgroundDrawable(stylePressed);
        else
            this.setBackgroundDrawable(styleUnPressed);
    }
}
