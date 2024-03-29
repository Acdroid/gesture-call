/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCall.ui.donate;

import com.flurry.android.FlurryAgent;

import ac.gestureCall.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

/**
 * @author marcos trujillo
 * @author Carlos
 *
 */
public class Donate extends Activity {

	public static final int ID = 2345;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.donate);

		init();
	}

	@Override
	protected void onStart() {
		super.onStart();
		FlurryAgent.logEvent("Donate", true);	
	}

	public void init(){


	}

	public void clickGestureCall(View v){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		FlurryAgent.logEvent("Donate click! Gesture call");
		intent.setData(Uri.parse("market://details?id=ac.gestureCallPro"));
		startActivityForResult(intent,ID);

	}
//	
//	
// PARA APLICACION EN AMAZON 
//
//	public void clickGestureCall(View v){
//		Intent intent = new Intent(Intent.ACTION_VIEW);
//		intent.setData(Uri.parse("http://www.amazon.com/gp/mas/dl/android?p=ac.GestureCallPro"));//enlace web
//		intent.setData(Uri.parse("amzn://apps/android?p=ac.gestureCallPro")); //enlace con la app de amazon	
//		startActivity(intent);
//
//	}
	
	
	public void clickAcdroid(View v){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		FlurryAgent.logEvent("Donate click! Acdroid");
		intent.setData(Uri.parse("market://search?q=pub:Acdroid"));
		startActivityForResult(intent,ID);	
	}
	
	


	/* (non-Javadoc)
	 * @see android.app.Activity#onActivityResult(int, int, android.content.Intent)
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			switch (resultCode){
			case RESULT_OK:
				Log.i("GestureCall","result_Ok");
				break;
			case RESULT_CANCELED:
				Log.i("GestureCall","result_canceled");
				break;
			case RESULT_FIRST_USER:
				Log.i("GestureCall","result_firstuser");				
				break;
			default:

			}
			
			setResult(RESULT_OK);
			Donate.this.finish();
		}
	}
}
