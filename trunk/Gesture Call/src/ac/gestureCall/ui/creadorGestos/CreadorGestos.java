package ac.gestureCall.ui.creadorGestos;

import java.util.ArrayList;
import java.util.Iterator;

import ac.gestureCall.R;
import ac.gestureCall.ui.main;
import ac.gestureCall.util.mToast.mToast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.Prediction;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CreadorGestos extends Activity {
	private static final float LENGTH_THRESHOLD = 120.0f;

	private Gesture mGesture;
	private View mDoneButton;
	public GestureLibrary store;

	public Context mContext;
	public Gesture gestureActual=null;
	public TextView texto;
	public String nombreContacto="";

	private static final int LETRA_NUEVA = 1;
	private static final int OK_ERROR= 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_gesture);

		mDoneButton = (Button)findViewById(R.id.done);
		texto = (TextView)findViewById(R.id.create_gesture_text);
		mContext = this;

		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			nombreContacto = bundle.getString("NOMBRE");
			texto.setText(nombreContacto);
		}

		GestureOverlayView overlay = (GestureOverlayView) findViewById(R.id.gestures_overlay);
		overlay.addOnGestureListener(new GesturesProcessor());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		if (mGesture != null) {
			outState.putParcelable("gesture", mGesture);
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		mGesture = savedInstanceState.getParcelable("gesture");
		if (mGesture != null) {
			final GestureOverlayView overlay =
				(GestureOverlayView) findViewById(R.id.gestures_overlay);
			overlay.post(new Runnable() {
				public void run() {
					overlay.setGesture(mGesture);
				}
			});

			Log.d("Entrenador","onRestoreInstanceEstate");

			mDoneButton.setEnabled(true);
		}
	}

	public void addGesture(View v) {
		if (mGesture != null) {

			store = main.getStore();
			
			store.removeEntry(nombreContacto);
			store.addGesture(nombreContacto, mGesture);
			store.save();

			mToast.Make(this, getResources().getString(R.string.gesto_anadido),0);
			setResult(main.RESULT_OK);
			CreadorGestos.this.finish();
		}

	}

	public void cancelGesture(View v) {
		setResult(main.RESULT_OK);
		finish();


	}

	public void doneGesture(View v) {		
		gestureActual = mGesture;
		addGesture(v);
		
	}

	

	private class GesturesProcessor implements GestureOverlayView.OnGestureListener {
		public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
			mDoneButton.setEnabled(false);
			mGesture = null;
		}

		public void onGesture(GestureOverlayView overlay, MotionEvent event) {
		}

		public void onGestureEnded(GestureOverlayView overlay, MotionEvent event) {
			mGesture = overlay.getGesture();
			if (mGesture.getLength() < LENGTH_THRESHOLD) {
				overlay.clear(false);
			}
			mDoneButton.setEnabled(true);

		}

		public void onGestureCancelled(GestureOverlayView overlay, MotionEvent event) {
		}
	}
	
}
