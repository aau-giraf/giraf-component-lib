package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Largely based on http://code.google.com/p/android-color-picker/
 */
public class GDialogColorSelect extends GDialog {
    public interface OnOkListener
    {
        void OnOkClick(GDialogColorSelect diag, int color);
    }
    private final OnOkListener _task;

    private final GDialogColorSelect _diag;
    private final View layout;
    private final View _hue; //viewHue - color shit to the right
    private final View _prevColor; //viewOldColor
    private final View _newColor; //viewNewColor
    private final ImageView _cursor; //viewCursor
    private final ImageView _target; //viewTarget
    private final ViewGroup _colorFrame; //viewContainer
    private final RelativeLayout _targetFrameContainer;
    private final RelativeLayout _hueContainer;
    private final RelativeLayout _prevColorContainer;
    private final RelativeLayout _newColorContainer;
    private GDialogColorSelectTargetFrame _targetFrame; //viewSatVal

    private final float[] currentColorHsv = new float[3];
    private int colorCurr;
    private boolean colorCurrIsSet = false;
    private boolean isInit = false;

    public GDialogColorSelect(Context context, OnOkListener task)
    {
        super(context);
        _task = task;
        _diag = this;

        View v = LayoutInflater.from(context).inflate(R.layout.gdialogcolorselect_layout, null);
        layout = v;
        _hue = v.findViewById(R.id.GDialogColorSelect_Hue);
        _prevColor = v.findViewById(R.id.GDialogColorSelect_PrevColor);
        _newColor = v.findViewById(R.id.GDialogColorSelect_NewColor);
        _cursor = (ImageView)v.findViewById(R.id.GDialogColorSelect_Cursor);
        _target = (ImageView)v.findViewById(R.id.GDialogColorSelect_Target);
        _colorFrame = (ViewGroup)v.findViewById(R.id.GDialogColorSelect_ColorFrame);
        _targetFrameContainer = (RelativeLayout)v.findViewById(R.id.GDialogColorSelect_TargetFrameContainer);
        _hueContainer = (RelativeLayout)v.findViewById(R.id.GDialogColorSelect_HueContainer);
        _prevColorContainer = (RelativeLayout) v.findViewById(R.id.GDialogColorSelect_PrevColorContainer);
        _newColorContainer = (RelativeLayout) v.findViewById(R.id.GDialogColorSelect_NewColorContainer);

        _targetFrame = new GDialogColorSelectTargetFrame(getContext());
        _targetFrameContainer.addView(_targetFrame);

        int color = GStyler.calculateGradientColor(GStyler.dialogBackgroundColor);
        GradientDrawable d = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[] {color, color});
        d.setStroke(2, color);
        Drawable d2 = d.mutate().getConstantState().newDrawable();
        Drawable d3 = d.mutate().getConstantState().newDrawable();
        Drawable d4 = d.mutate().getConstantState().newDrawable();
        _targetFrameContainer.setBackgroundDrawable(d);
        _hueContainer.setBackgroundDrawable(d2);
        _prevColorContainer.setBackgroundDrawable(d3);
        _newColorContainer.setBackgroundDrawable(d4);

        SetView(v);

        SetListeners();
    }

    public GDialogColorSelect(Context context, int oldColor, OnOkListener task)
    {
        this(context, task);
        SetCurrColor(oldColor);
    }

    public void SetCurrColor(int color)
    {
        colorCurr = color;
        colorCurrIsSet = true;
        isInit = false;
        Color.colorToHSV(colorCurr, currentColorHsv);
    }

    private void SetListeners()
    {
        this.findViewById(R.id.GDialogColorSelect_cancelBtn).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _diag.dismiss();
            }
        });

        this.findViewById(R.id.GDialogColorSelect_okBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_task != null)
                    _task.OnOkClick(_diag, GetColor());

                _diag.dismiss();
            }
        });
        _hue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP)
                {
                    float y = event.getY();
                    if (y < 0.0f)
                        y = 0.0f;
                    if (y > _hue.getMeasuredHeight())
                        y = _hue.getMeasuredHeight() - 0.001f; // to avoid looping from end to start.

                    float hue = 360.0f - 360.0f / _hue.getMeasuredHeight() * y;
                    if (hue == 360.0f)
                        hue = 0.0f;

                    SetHue(hue);

                    // Update view
                    _targetFrame.SetHue(GetHue());
                    MoveCursor();
                    _newColor.setBackgroundColor(GetColor());

                    return true;
                }
                return false;
            }
        });
        _targetFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_MOVE
                        || event.getAction() == MotionEvent.ACTION_DOWN
                        || event.getAction() == MotionEvent.ACTION_UP)
                {
                    float x = event.getX();
                    float y = event.getY();

                    if (x < 0.0f)
                        x = 0.0f;
                    if (x > _targetFrame.getMeasuredWidth())
                        x = _targetFrame.getMeasuredWidth();
                    if (y < 0.0f)
                        y = 0.0f;
                    if (y > _targetFrame.getMeasuredHeight())
                        y = _targetFrame.getMeasuredHeight();

                    SetSat(1.0f / _targetFrame.getMeasuredWidth() * x);
                    SetVal(1.0f - (1.0f / _targetFrame.getMeasuredHeight() * y));

                    // Update view
                    MoveTarget();
                    _newColor.setBackgroundColor(GetColor());

                    return true;
                }
                return false;
            }
        });
        // move cursor & target on first draw
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout()
            {
                MoveCursor();
                MoveTarget();
                layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });
    }

    @Override
    public void show()
    {
        if (!Initialize())
        {
            Log.e("GDialogColorSelect", "Cannot show, current color not set. Call SetCurrColor(int) to set current color.");
            return;
        }

        super.show();
    }

    private boolean Initialize()
    {
        if (isInit)
            return true;
        if (!colorCurrIsSet)
            return false;

        _targetFrame.SetHue(GetHue());
        _prevColor.setBackgroundColor(colorCurr);
        _newColor.setBackgroundColor(colorCurr);

        return true;
    }

    private void MoveCursor()
    {
        float y = _hue.getMeasuredHeight() - (GetHue() * _hue.getMeasuredHeight() / 360.0f);
        if (y == _hue.getMeasuredHeight())
            y = 0.0f;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) _cursor.getLayoutParams();
        layoutParams.leftMargin = (int) (_hueContainer.getRight()-2 - Math.floor(_cursor.getMeasuredWidth() / 2) - _colorFrame.getPaddingLeft());
        layoutParams.topMargin = (int) (_hueContainer.getTop()+2 + y - Math.floor(_cursor.getMeasuredHeight() / 2) - _colorFrame.getPaddingTop());
        _cursor.setLayoutParams(layoutParams);
    }

    private void MoveTarget()
    {
        float x = GetSat() * _targetFrame.getMeasuredWidth();
        float y = (1.0f - GetVal()) * _targetFrame.getMeasuredHeight();

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) _target.getLayoutParams();
        params.leftMargin = (int) (_targetFrameContainer.getLeft()+2 + x - (_target.getMeasuredWidth() / 2 + 0.5) - _colorFrame.getPaddingLeft());
        params.topMargin = (int) (_targetFrameContainer.getTop()+2 + y - (_target.getMeasuredHeight() / 2 + 0.5) - _colorFrame.getPaddingTop());
        _target.setLayoutParams(params);
    }

    private float GetHue()
    {
        return currentColorHsv[0];
    }

    private void SetHue(float hue)
    {
        currentColorHsv[0] = hue;
    }

    private float GetSat()
    {
        return currentColorHsv[1];
    }

    private void SetSat(float sat)
    {
        currentColorHsv[1] = sat;
    }

    private float GetVal()
    {
        return currentColorHsv[2];
    }

    private void SetVal(float val)
    {
        currentColorHsv[2] = val;
    }

    private int GetColor()
    {
        return Color.HSVToColor(currentColorHsv);
    }

    public class GDialogColorSelectTargetFrame extends View {
        final float[] color = { 1.0f, 1.0f, 1.0f };
        Paint paint;
        Shader verticalGradient;

        public GDialogColorSelectTargetFrame(Context context)
        {
            this(context, null);
        }

        public GDialogColorSelectTargetFrame(Context context, AttributeSet attrs)
        {
            this(context, attrs, 0);
        }

        public GDialogColorSelectTargetFrame(Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        @Override
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
            if (paint == null)
            {
                paint = new Paint();
                verticalGradient = new LinearGradient(0.0f, 0.0f, 0.0f, this.getMeasuredHeight(), 0xffffffff, 0xff000000, Shader.TileMode.CLAMP);
            }
            int rgb = Color.HSVToColor(color);
            Shader horizontalGradient = new LinearGradient(0.0f, 0.0f, this.getMeasuredWidth(), 0.0f, 0xffffffff, rgb, Shader.TileMode.CLAMP);
            ComposeShader shader = new ComposeShader(verticalGradient, horizontalGradient, PorterDuff.Mode.MULTIPLY);
            paint.setShader(shader);
            canvas.drawRect(0.0f, 0.0f, this.getMeasuredWidth(), this.getMeasuredHeight(), paint);
        }

        void SetHue(float hue)
        {
            color[0] = hue;
            invalidate();
        }
    }
}
