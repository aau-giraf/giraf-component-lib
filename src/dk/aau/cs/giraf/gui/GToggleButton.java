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
        Setup(null);
    }

    public GToggleButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        Setup(null);
    }

    public GToggleButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        Setup(null);
    }

    private void Setup(final OnClickListener task)
    {
        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                state = !state;
                ((GButton)v).setText(String.valueOf(state));
                if (task != null) task.onClick(v);
                if (state)
                    v.setBackgroundDrawable(((GButton)v).stylePressed);
                else
                    v.setBackgroundDrawable(((GButton)v).styleUnPressed);
            }
        });

    }

    public void SetOnClickListener(OnClickListener task)
    {
        Setup(task);
    }

    public boolean IsPressed()
    {
        return state;
    }

}
