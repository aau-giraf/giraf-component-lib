package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class GToast
{
    private Toast _toast;

    public GToast(Context context, CharSequence text, int duration)
    {
        _toast = Toast.makeText(context, text, duration);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.gtoast_layout, null);

        //Set background color
        ColorDrawable cd = new ColorDrawable(GStyler.baseAlphaBackground);
        layout.setBackgroundDrawable(cd);

        //Set text
        TextView textView = (TextView) layout.findViewById(R.id.GToast_text);
        textView.setText(text);

        _toast.setView(layout);
    }

    public void cancel()
    {
        _toast.cancel();
    }

    public int getDuration()
    {
        return _toast.getDuration();
    }

    public int getGravity()
    {
        return _toast.getGravity();
    }

    public float getHorizontalMargin()
    {
        return _toast.getHorizontalMargin();
    }

    public float getVerticalMargin()
    {
        return _toast.getVerticalMargin();
    }

    public View getView()
    {
        return _toast.getView();
    }

    public int getXOffset()
    {
        return _toast.getXOffset();
    }

    public int getYOffset()
    {
        return _toast.getYOffset();
    }

    private void setDuration(int duration)
    {
        _toast.setDuration(duration);
    }

    public void setGravity(int gravity, int xOffset, int yOffset)
    {
        _toast.setGravity(gravity, xOffset, yOffset);
    }

    public void setMargin(float horizontalMargin, float verticalMargin)
    {
        _toast.setMargin(horizontalMargin, verticalMargin);
    }

    public void show()
    {
        _toast.show();
    }

    public static GToast makeText(Context context, CharSequence text, int duration)
    {
        return new GToast(context, text, duration);
    }

    public static GToast makeText(Context context, int resID, int duration)
    {
        return new GToast(context, context.getString(resID), duration);
    }
}
