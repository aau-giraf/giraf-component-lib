package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

public class GListSnapper extends ListView{
    int paddingSize = 10;

	public GListSnapper(Context context) {
		super(context);
        this.setStyle();
        this.setListeners();
		// TODO Auto-generated constructor stub
	}
	public GListSnapper(Context context, AttributeSet attrs) {
		super(context, attrs);
        this.setStyle();
        this.setListeners();
		// TODO Auto-generated constructor stub
	}

	public GListSnapper(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
        this.setStyle();
        this.setListeners();
		// TODO Auto-generated constructor stub
	}

    private void setListeners()
    {
        //Add listener to handle right positioning of the Lists item after a scroll
        this.setOnScrollListener(new OnScrollListener() {
            private float y = 0; //the offset of the y-axis, from the top of the list to the top of the first item which is shown in the list.
            private int currentItem = 0;
            private int itemHeight = 0;
            private Boolean lastItemShown = false;
            private Thread u;

            @Override
            public void onScrollStateChanged(final AbsListView view, int scrollState) {
                //Test if the scrolling stopped
                if(scrollState == 0)
                {
                    //Ensure that the lists first visable item is not null (otherwise the rest will crash)
                    View item = view.getChildAt(0);
                    if(item != null)
                    {
                        int l = - itemHeight/2;
                        //Test if more than half of the first item is showed
                        if(y > l)
                        {
                            //If more than  50% is shown push down to show the first item again
                            //Runnable to be able to use Threading and be able to use the smoothScrollBy function
                            Runnable runDown = new Runnable(){
                                @Override
                                public void run()
                                {
                                    //Set how much to scroll, and how fast
                                    view.smoothScrollBy((int)y- paddingSize, 300);
                                }};

                            if(u == null) //If no thread is instantiated yet instantiate
                            {
                                u = new Thread(runDown);
                                u.start();
                            }
                            //if a thread is already running, interrupt it and create a new
                            //(this is done becacuse two threads must not handle this at the same time, causes it to freeze)
                            else if(u.getState() == Thread.State.RUNNABLE)
                            {
                              u.interrupt();
                              if(u.isInterrupted() == true) //ensure that it is in fact interrupted
                              {
                                  u = new Thread(runDown);
                                  u.start();
                              }
                            }
                            else //if a thread is instantiated but not doing anything create a new and run that
                            {
                                u = new Thread(runDown);
                                u.start();
                            }
                        }
                        //Also tests if it can see the last item
                        //This is done because if you scroll all the way done it causes the list to try to runUp an item that it cant.
                        //Making it jump and going into an infinite loop
                        else if(y < l && lastItemShown == false)
                        {
                            //If more than  50% is shown push up and display the "second item" shown as the first instead
                            //Runnable to be able to use Threading and be able to use the smoothScrollBy function
                            Runnable runUp = new Runnable(){
                                @Override
                                public void run() {
                                    //Set how much to scroll, and how fast
                                int j = itemHeight +(int) y;
                                view.smoothScrollBy(j, 300);
                            }};

                            //Same as before
                            if(u == null)
                            {
                                u = new Thread(runUp);
                                u.start();
                            }
                            else if(u.getState() == Thread.State.RUNNABLE)
                            {
                                u.interrupt();
                                if(u.isInterrupted() == true)
                                {
                                    u = new Thread(runUp);
                                    u.start();
                                }
                            }
                            else
                            {
                                u = new Thread(runUp);
                                u.start();
                            }
                        }
                    }

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                //Test if view is not null otherwise rest of the code would crash the application
                if(view != null)
                {
                    View w  = view.getChildAt(0);
                    if(w != null)
                    {
                        currentItem = firstVisibleItem;
                        y = w.getTop();
                        itemHeight = w.getHeight();
                        if(firstVisibleItem == totalItemCount - visibleItemCount)
                        {
                            lastItemShown = true;
                        }
                        else
                        {
                            lastItemShown = false;
                        }
                    }
                }
            }
        });
    }

    protected void setStyle() {
        int dHeight = this.getDividerHeight();

        //this removes the blue selection background color when an item is selected
        this.setSelector(android.R.color.transparent);
        ColorDrawable cd = new ColorDrawable(android.R.color.transparent);
        this.setDivider(cd);
        this.setDividerHeight(dHeight);

        //Create fading effect
        this.setPadding(0, paddingSize, 0, paddingSize);
        this.setVerticalFadingEdgeEnabled(true);
        this.setFadingEdgeLength(5);
    }
}
