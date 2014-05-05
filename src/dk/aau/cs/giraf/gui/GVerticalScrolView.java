package dk.aau.cs.giraf.gui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by AndersBender on 29-04-14.
 */
public class GVerticalScrolView extends ScrollView{
        private Runnable scrollerTask;
        private float initialPosition = 0;
        GVerticalScrolView gVerticalScrolView;
        private int newCheck = 100;
        private static final String TAG = "MyScrollView";

        public void setInitialPosition(float i)
        {
            initialPosition = i;
        }

        public float getInitialPosition()
        {
            return(initialPosition);
        }
        public interface OnScrollStoppedListener{
            void onScrollStopped();
        }

        private OnScrollStoppedListener onScrollStoppedListener;

        public GVerticalScrolView(Context context, AttributeSet attrs) {
            super(context, attrs);
            gVerticalScrolView = this;
        }

        public void setOnScrollStoppedListener(GVerticalScrolView.OnScrollStoppedListener listener){
            onScrollStoppedListener = listener;
        }
        int q = 0;
        public void startScrolling(int i){
            q = i;
            Runnable runDown = new Runnable(){
                @Override
                public void run()
                {
                    //Set how much to scroll, and how fast
                    gVerticalScrolView.smoothScrollTo(0,q);
                }};

            Thread w = new Thread(runDown);
            w.start();
        }

    }