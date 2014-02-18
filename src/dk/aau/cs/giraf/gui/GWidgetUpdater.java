package dk.aau.cs.giraf.gui;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Handler;
import android.os.Message;

public class GWidgetUpdater extends Handler
{
	public final static int MSG_START = 0;
	public final static int MSG_STOP = 1;
	public final static int MSG_UPDATE = 2;
	public final static int REFRESH_PERIOD = 1000; // in ms
	public final static int SPIN_PERIOD = 100; // in ms 

	// pointer to the user interface adapter
	private ArrayList<IGWidget> widgets;
	// remember the last time the UI was updated
	private long mLastTime;

	public GWidgetUpdater()
	{
		super();
		widgets = new ArrayList<IGWidget>();
		mLastTime = 0;
	}

	public void addWidget(IGWidget widget) {
		if (!widgets.contains(widget)) {
			widgets.add(widget);
		}	
	}

	// handle messages to implement the screen refresh timer
	@Override
	public void handleMessage(Message msg)
	{
		super.handleMessage(msg);

		switch (msg.what)
		{
		case MSG_START:
			// start the timer by sticking an update message
			// into the message queue
			this.sendEmptyMessage(MSG_UPDATE);
			break;

		case MSG_UPDATE:
			// the timer loops by sticking delayed messages into the
			// queue at the spin period.  Each spin period, use
			// checkTime() to see if a UI update is required
			this.checkTime();
			this.sendEmptyMessageDelayed(MSG_UPDATE, SPIN_PERIOD);
			break;                                 

		case MSG_STOP:
			// stop the timer by removing any and all delayed update
			// messages that have not been processed
			this.removeMessages(MSG_UPDATE);
			break;

		default:
			break;
		}
	}

	// check how much time has passed and update the UI
	// if required.
	private void checkTime()
	{
		// how much time has passed since the last update?
		long currTime = Calendar.getInstance().getTimeInMillis();
		long dt = currTime - mLastTime;
		// if it is more than the refresh period, update the UI
		if (dt > REFRESH_PERIOD) {
			for (IGWidget widget : widgets) {
				widget.updateDisplay();
			}
			mLastTime = currTime;
		}
	}
}