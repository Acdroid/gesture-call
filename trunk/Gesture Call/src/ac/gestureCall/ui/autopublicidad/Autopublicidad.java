/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCall.ui.autopublicidad;

import com.flurry.android.FlurryAgent;

import ac.gestureCall.R;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Autopublicidad extends LinearLayout {

	public ImageView imagen;
	public LinearLayout layout;
	private Context context;

	public Autopublicidad(Context c) {
		super(c);
		context = c;
		init();
	}

	public Autopublicidad(Context c, AttributeSet attrs) {
		super(c, attrs);
		context = c;
		init();
	}

	private void init(){
		String infService = Context.LAYOUT_INFLATER_SERVICE;
		LayoutInflater li = (LayoutInflater)getContext().getSystemService(infService);
		li.inflate(R.layout.auto_publicidad, this, true);

		imagen = (ImageView)findViewById(R.id.auto_publicidad_ima);
		layout = (LinearLayout)findViewById(R.id.auto_publicidad);

		layout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse("market://details?id=ac.gestureCallPro"));
				
				FlurryAgent.logEvent("Click Autopublicidad", true);
				
				context.startActivity(intent);				
			}
		});
	}

	@Override
	public void setVisibility(int visibility) {
		super.setVisibility(visibility);
		if (visibility == View.VISIBLE)
			FlurryAgent.logEvent("Show Autopublicidad", true);
	}
	
	
}
