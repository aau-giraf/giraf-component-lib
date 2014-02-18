package dk.aau.cs.giraf.gui;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GDialog extends Dialog {
	
	private final GDialog mDialog;

	public void setStyle(int thumb, String headline, String description) {
		this.setContentView(R.layout.gdialog_layout);
		
		ImageView thumb_view = (ImageView) this.findViewById(R.id.content_thumb);
		thumb_view.setBackgroundResource(thumb);
		
		TextView headline_txt = (TextView) this.findViewById(R.id.dialog_headline);
		headline_txt.setText(headline);
		
		TextView description_txt = (TextView) this.findViewById(R.id.dialog_description);
		description_txt.setText(description);
	}
	
	public GDialog(Context context, int drawable, String headline, String text, android.view.View.OnClickListener task) {
		super(context, android.R.style.Theme_Translucent_NoTitleBar);
		this.setStyle(drawable, headline, text);
		
		mDialog = this;
		
		this.findViewById(R.id.dialog_cancel).setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				mDialog.cancel();	
			}
		});
		
		this.findViewById(R.id.dialog_ok).setOnClickListener(task);
	}


}
