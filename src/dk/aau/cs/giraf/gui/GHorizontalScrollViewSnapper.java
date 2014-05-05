package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AndersBender on 01-05-14.
 */
public class GHorizontalScrollViewSnapper extends HorizontalScrollView{

    float scrollRecordPosition = 0;
    float initialPosition = 0;
    int nmbOfItems = 0;
    Thread w;
    private GHorizontalScrollViewSnapper gHorizontalScrollViewSnapper;
    boolean touched = false;
    int scrollToDistance = 0;

    //Constructors
    public GHorizontalScrollViewSnapper(Context context, AttributeSet attrs) {
        super(context, attrs);
        //Self reference - This will be used later when Threading
        gHorizontalScrollViewSnapper = this;
        setListeners();
    }

    public GHorizontalScrollViewSnapper(Context context) {
        super(context);
        //Self reference - This will be used later when Threading
        gHorizontalScrollViewSnapper = this;
        setListeners();
    }

    public GHorizontalScrollViewSnapper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //Self reference - This will be used later when Threading
        gHorizontalScrollViewSnapper = this;
        setListeners();
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        //Check whether the scrollview has stopped scrolling and if it is at it desired distance
        if(scrollRecordPosition == this.getScrollX() && scrollToDistance != this.getScrollX())
        {
            //Check if the user is touching the screen
            if(!touched)
            {
                //Check if the scrollview has moved from the initialPosition
                if (this.getInitialPosition() != this.getScrollX())
                {
                    //Fetch the LinearLayout inside the ScrollView
                    LinearLayout linearLayout = (LinearLayout) this.getChildAt(0);
                    //Count number of views inside this layout
                    nmbOfItems = linearLayout.getChildCount();
                    //Count number of views inside this layout
                    int pixelCount = 0;
                    //This for-loops checks which of the views is at beginning of the visible part of the scrollview.
                    //Further more it check if the should snap to the beginning of this item or the end
                    for(int i = 0; i < nmbOfItems; i++)
                    {
                        //Get i'th view test it size, if the Scrollview's getScrollX() is inside the view find a snap point
                        //Otherwise add the view to the pixelCount
                        View w = linearLayout.getChildAt(i);
                        if(pixelCount <= this.getScrollX() && this.getScrollX() <= pixelCount + w.getMeasuredWidth())
                        {
                            //Sroll Right - if the view is less than half showed
                            if(pixelCount + w.getMeasuredWidth()/2 <= this.getScrollX())
                            {
                                this.startScrollingTo(pixelCount + w.getMeasuredWidth());
                                break;
                            }
                            //Scroll Left
                            else if(pixelCount + w.getMeasuredWidth()/2 >= this.getScrollX())
                            {
                                this.startScrollingTo(pixelCount);
                                break;
                            }

                        }
                        //Adding the view size to the pixel count
                        else{pixelCount += w.getMeasuredWidth();}
                    }
                }
            }
        }
        scrollRecordPosition = this.getScrollX();
    }

    //used to record what position the user started to press down and scroll the scrollView
    public void setInitialPosition(float initialPosition) {
        this.initialPosition = initialPosition;
    }

    public float getInitialPosition()
    {
        return initialPosition;
    }

    //Preforms the snapping
    public void startScrollingTo(int i){
        scrollToDistance = i;
        //Create a runnable to run a new thread
        Runnable run = new Runnable(){
            @Override
            public void run()
            {
                gHorizontalScrollViewSnapper.smoothScrollTo(scrollToDistance, 0);
            }};

        if(w == null) //If no thread is instantiated yet instantiate
        {
            w = new Thread(run);
            w.start();
        }
        //if a thread is already running, interrupt it and create a new
        //(this is done becacuse two threads must not handle this at the same time, causes it to freeze)
        else if(w.getState() == Thread.State.RUNNABLE)
        {
            w.interrupt();
            if(w.isInterrupted()) //ensure that it is in fact interrupted
            {
                w = new Thread(run);
                w.start();
            }
        }
        else //if a thread is instantiated but not doing anything create a new and run that
        {
            w = new Thread(run);
            w.start();
        }
    }

    public void setListeners()
    {
         //Use onTouchListener to Detect when the user is pressing down and releasing the scrollview
        this.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    GHorizontalScrollViewSnapper scroller = (GHorizontalScrollViewSnapper) v;
                    scroller.setInitialPosition(scroller.getScrollX());
                    touched = true;
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    touched = false;
                    gHorizontalScrollViewSnapper.invalidate();
                }
                return false;
            }
        });
    }
}
