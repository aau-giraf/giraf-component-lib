package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Malakahh on 5/8/14.
 */
public class GLayout extends RelativeLayout {
    RelativeLayout overlay;

    //Selectable stuff
    private boolean _selectable;
    private boolean _isSelected = false;
    private GradientDrawable _selectedStyle;

    //Deletable stuff
    private boolean _deletable;
    private boolean _isDeleteShown;
    private ImageView _deleteStyle;

    //Markable
    private boolean _markable;
    private boolean _isMarked;

    public GLayout(Context context)
    {
        super(context);
        AddOverlay();
    }

    public GLayout(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        AddOverlay();
        GetAttributes(attrs);
    }

    public GLayout(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        AddOverlay();
        GetAttributes(attrs);
    }

    private void AddOverlay()
    {
        overlay = new RelativeLayout(getContext());
        this.addView(overlay);
    }

    private void GetAttributes(AttributeSet attrs)
    {
        TypedArray styledAttributes = this.getContext().obtainStyledAttributes(attrs, R.styleable.GLayout);
        this.SetSelectable(styledAttributes.getBoolean(R.styleable.GLayout_Selectable, false));
        this.SetDeletable(styledAttributes.getBoolean(R.styleable.GLayout_Deletable, false));
        this.SetMarkable(styledAttributes.getBoolean(R.styleable.GLayout_Markable, false));
    }

    public void SetSelectable(boolean selectable)
    {
        _selectable = selectable;
    }

    public boolean IsSelectable()
    {
        return _selectable;
    }

    public void SetSelected(boolean selected)
    {
        if (IsSelectable())
        {
            _isSelected = selected;
            SetSelectedStyle(_isSelected);
        }
    }

    public boolean IsSelected()
    {
        return _isSelected;
    }

    public void SetDeleteOnClickListener(OnClickListener task)
    {
        if (_deleteStyle != null)
            _deleteStyle.setOnClickListener(task);
    }

    public void SetDeletable(boolean deletable)
    {
        _deletable = deletable;

        if (IsDeletable())
            SetDeletedStlye();
        else
            SetDeleteOnClickListener(null);

        if (_deleteStyle != null)
            _deleteStyle.setVisibility(IsDeletable() ? View.VISIBLE : View.INVISIBLE);
    }

    public void SetDeletable(boolean deletable, OnClickListener task)
    {
        this.SetDeletable(deletable);

        if (IsDeletable())
           SetDeleteOnClickListener(task);
    }

    public boolean IsDeletable()
    {
        return _deletable;
    }

    public void SetMarkable(boolean markable)
    {
        _markable = markable;
    }

    public boolean IsMarkable()
    {
        return _markable;
    }

    public void SetMarked(boolean marked)
    {
        if (IsMarkable())
        {
            _isMarked = marked;
            SetMarkedStyle(_isMarked);
        }
    }

    public boolean IsMarked()
    {
        return _isMarked;
    }

    private void SetSelectedStyle(boolean setStyle)
    {
        if (setStyle)
        {
            _selectedStyle = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {0x00000000, 0x00000000});
            _selectedStyle.setStroke(3, GStyler.selectColor, 10, 10);
            overlay.setBackgroundDrawable(_selectedStyle);
        }
        else
        {
            _selectedStyle = null;
            overlay.setBackgroundDrawable(null);
        }
    }

    private void SetDeletedStlye()
    {
        if (_deleteStyle != null)
            return;

        _deleteStyle = new ImageButton(this.getContext());
        _deleteStyle.setImageResource(R.drawable.glayout_deletelayout);

        RelativeLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.width = 40;
        params.height = 40;
        _deleteStyle.setLayoutParams(params);

        SetMinimumSize(40);

        _deleteStyle.setBackgroundColor(Color.TRANSPARENT);

        _deleteStyle.setFocusable(false);
        _deleteStyle.setVisibility(View.INVISIBLE);

        overlay.addView(_deleteStyle);
    }

    private void SetMarkedStyle(boolean setStyle)
    {
        if (setStyle)
            overlay.setBackgroundResource(R.drawable.mark);
        else
            overlay.setBackgroundDrawable(null);
    }

    private void SetMinimumSize(int size)
    {
        this.setMinimumHeight(size);
        this.setMinimumWidth(size);
    }

    @Override
    protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4)
    {
        super.onLayout(arg0, arg1, arg2, arg3, arg4);

        LayoutParams params = new LayoutParams(this.getWidth(), this.getHeight());
        overlay.setLayoutParams(params);

        if (!IsMarkable() && !IsSelectable() && !IsDeletable())
            return;

        overlay.bringToFront();

        if (IsDeletable())
        {
            int padding = 2;

            _deleteStyle.setX(this.getWidth() - _deleteStyle.getWidth() - padding);
            _deleteStyle.setY(padding);
        }
    }
}
