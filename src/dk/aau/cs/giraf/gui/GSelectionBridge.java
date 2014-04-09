package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.RelativeLayout;


/**
 * Created by Jelly on 07-04-14.
 */
public class GSelectionBridge extends RelativeLayout {

    GList targetList;
    View targetView;
    boolean initialized = false;
    TypedArray attributes;
    int selectedId = -1;
    View selectedView;
    int dbg = 0;

    public GSelectionBridge(Context context) throws Exception
    {
        super(context);
        throw new Exception("GSelectionBridge instantiated without attributes. Please instantiate with required attributes.");
    }

    public GSelectionBridge(Context context, AttributeSet attrs)
    {
        super(context,attrs);
        attributes = context.obtainStyledAttributes(attrs, R.styleable.GSelectionBridge, 0, 0);
        this.setBackgroundColor(Color.GRAY);
    }

    public GSelectionBridge(Context context, AttributeSet attrs, int defStyle)
    {
        super(context,attrs,defStyle);
        attributes = context.obtainStyledAttributes(attrs, R.styleable.GSelectionBridge, 0, 0);
        this.setBackgroundColor(Color.GRAY);
    }

    @Override
    public void onDraw(Canvas c)
    {
        super.onDraw(c);

        if (!initialized)
        {
            Setup();
            initialized = true;
        }

    }

    private void PositionBridge()
    {
        if (targetList != null && targetView != null)
        {
            int listItemLocation[] = new int[2];
            int targetViewLocation[] = new int[2];
            selectedView.getLocationOnScreen(listItemLocation);
            targetView.getLocationOnScreen(targetViewLocation);

            float myHeight = selectedView.getHeight()*0.8f;
            float myWidth;
            if (listItemLocation[0] < targetViewLocation[0])
                myWidth = targetViewLocation[0] - listItemLocation[0] - selectedView.getWidth();
            else
                myWidth = listItemLocation[0] - targetViewLocation[0] - selectedView.getWidth();

            this.setLayoutParams(new LayoutParams((int) (myWidth + .5 + 3), (int) (myHeight + .8)));

            this.setX(listItemLocation[0] + selectedView.getWidth());
            this.setY(selectedView.getY() + targetList.getY() + myHeight*0.1f);
        }
    }

    private void ScrollBridge()
    {
        if (selectedView == null) return;
        if (selectedId < targetList.getFirstVisiblePosition() || selectedId > targetList.getLastVisiblePosition())
        {
            this.setLayoutParams(new LayoutParams(this.getWidth(), 0));
            return;
        }
        int svHeight = (int)(selectedView.getHeight() * 0.8f);
        float svY = selectedView.getY() + svHeight/8;
        float tY = targetList.getY();
        int tBot = targetList.getBottom();

        float vPos = svY + tY;
        float diff;
        if (vPos < tY)
        {
            diff = vPos - tY;
            if (svHeight + diff > 0)
            {
                this.setLayoutParams(new LayoutParams(this.getWidth(), (int)(svHeight + diff)));
                this.setY(vPos - diff);
            }
        }
        else if (vPos + svHeight > tBot)
        {
            diff = vPos + svHeight - tBot;
            this.setLayoutParams(new LayoutParams(this.getWidth(), (int)(svHeight - diff)));
            this.setY(vPos);
        }
        else
        {
            this.setLayoutParams(new LayoutParams(this.getWidth(), (int)(svHeight)));
            this.setY(vPos);
        }


    }

    private void Setup()
    {
        View parent = (View)this.getParent();
        targetList = (GList)parent.findViewById(attributes.getResourceId(R.styleable.GSelectionBridge_List, -1));
        targetView = parent.findViewById(attributes.getResourceId(R.styleable.GSelectionBridge_TargetView, -1));

        final AdapterView.OnItemClickListener prevListener = targetList.getOnItemClickListener();
        targetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (prevListener != null)
                    prevListener.onItemClick(parent, view, position, id);
                selectedId = position;
                selectedView = view;
                PositionBridge();
            }
        });

        ViewTreeObserver vto = targetList.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                ScrollBridge();
                return true;
            }
        });


        this.setPadding(0,3,0,3);
        RelativeLayout rl = new RelativeLayout(this.getContext());
        rl.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        rl.setBackgroundColor(Color.WHITE);
        this.addView(rl);
        this.setLayoutParams(new LayoutParams(0,0));
    }
}
