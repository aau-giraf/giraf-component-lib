package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by Jelly on 25-03-14.
 */
public class GToggleButton extends GButton {

    private boolean state = false;

    public GToggleButton(Context context)
    {
        super(context);
        setOnClickListener(null);
    }

    public GToggleButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);
    }

    public GToggleButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "onClick") == null)
            setOnClickListener(null);
    }

    @Override
    public void setOnClickListener(final OnClickListener task)
    {
        super.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                state = !state;
                if (task != null) task.onClick(v);
                if (state)
                    v.setBackgroundDrawable(((GButton) v).stylePressed);
                else
                    v.setBackgroundDrawable(((GButton) v).styleUnPressed);
            }
        });
    }

    @Override
    public boolean isPressed()
    {
        return state;
    }

}
