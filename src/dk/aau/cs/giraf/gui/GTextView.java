package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Typeface;
import android.text.method.ScrollingMovementMethod;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

/**
 * Created by AndersBender on 27-03-14.
 */
public class GTextView extends TextView{

    private OnScrollListener mOnScrollListener;

    public GTextView(Context context) {
        super(context);
        this.setStyle();
    }

    public GTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setStyle();

    }

    public GTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.setStyle();
    }

    private void setStyle()
    {
        this.setTypeface(Typeface.SANS_SERIF);
        this.setMovementMethod(new ScrollingMovementMethod());
    }

    public void SetOnScrollListener(OnScrollListener l)
    {
        mOnScrollListener = l;
    }

    @Override
    protected void onScrollChanged(int horiz, int vert, int oldHoriz, int oldVert)
    {
        super.onScrollChanged(horiz, vert, oldHoriz, oldVert);
        if (mOnScrollListener != null)
            mOnScrollListener.onScroll(this);
    }

    public interface OnScrollListener
    {
        void onScroll(View v);
    }
}
