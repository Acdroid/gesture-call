/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCall.ui.creadorGestos;

import java.io.File;

import ac.gestureCall.R;
import ac.gestureCall.exceptions.NoPreferenceException;
import ac.gestureCall.ui.main;
import ac.gestureCall.ui.cabecera.Cabecera;
import ac.gestureCall.ui.contactos.ListContact;
import ac.gestureCall.util.config.AppConfig;
import ac.gestureCall.util.config.AppConfig.Themes;
import ac.gestureCall.util.mToast.mToast;
import ac.gestureCall.util.smaatoListener.PrepareBaner;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.InterstitialAd;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
import com.smaato.SOMA.SOMABanner;


public class CreadorGestos extends Activity {
	private static final float LENGTH_THRESHOLD = 120.0f;
	private static final int ID = 2;

	private Gesture mGesture;
	private Button mDoneButton;
	public Button ButtonCancel;
	public GestureLibrary store;

	public Context mContext;
	public Gesture gestureActual=null;
	public TextView texto;
	public String nombreContacto="";
	public String phoneContacto="";
	public LinearLayout lay_main;
	public AppConfig ap;
	public Cabecera cabecera;

	public GestureOverlayView overlay;

	public MobclixMMABannerXLAdView adView;
	public InterstitialAd interstitial;
	
	public SOMABanner somaBaner;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.create_gesture);

        //Escondemos la publicidad hasta que se cargue
		adView = (MobclixMMABannerXLAdView)findViewById(R.id.mobclix_publicidad);
		adView.setVisibility(View.GONE);
			
		
		//Publicidad de smaato!
		somaBaner = (SOMABanner)findViewById(R.id.smaato_baner);
		PrepareBaner.prepareAndCall(somaBaner,adView);

		mDoneButton = (Button)findViewById(R.id.done);
		ButtonCancel = (Button) findViewById(R.id.cg_button_cancel);
		//Asignamos al boton el texto volver
		ButtonCancel.setText(getResources().getString(R.string.cancelGesture2));
		texto = (TextView)findViewById(R.id.create_gesture_text);
		mContext = this;
		overlay = (GestureOverlayView)findViewById(R.id.cg_overlay);
		lay_main = (LinearLayout)findViewById(R.id.create_gesture_lay_main);
		ap = new AppConfig(mContext, AppConfig.NAME);
		setTheme();

		//Cabecera
		cabecera = (Cabecera)findViewById(R.id.create_cabecera);
		cabecera.setOnOptionClickWitReturn(ID);

		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			nombreContacto = bundle.getString(ListContact.KEY_NAME);
			phoneContacto = bundle.getString(ListContact.KEY_PHONE);
			texto.setText(nombreContacto);
		}

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
			overlay.post(new Runnable() {
				public void run() {
					overlay.setGesture(mGesture);
				}
			});

			mDoneButton.setEnabled(true);
			ButtonCancel.setText(getResources().getString(R.string.cancelGesture));
		}
		ButtonCancel.setText(getResources().getString(R.string.cancelGesture2));
		mDoneButton.setEnabled(false);
	}

	/**
	 * 
	 * Metodo de apoyo para doneGesture
	 * 
	 * @param v boton que activa el metodo
	 */
	public void addGesture(View v) {
		if (mGesture != null) {

			if(!obtenLibreria())
				return;

			store.removeEntry(phoneContacto);
			store.addGesture(phoneContacto, mGesture);
			store.save();

			mToast.Make(this, getResources().getString(R.string.gesto_anadido),0);
			setResult(main.RESULT_GESTO_ADD_OK);
			CreadorGestos.this.finish();
		}

	}


	/**
	 * Obtiene la lista de gestos de la base de datos
	 * de gestos.
	 * @return false si ha ocurrido algun error al cargar
	 * true si todo ha salido bien.
	 */
	private boolean obtenLibreria(){
		File mStoreFile = new File(Environment.getExternalStorageDirectory() + "/GestureCall", "gestures");
		store = GestureLibraries.fromFile(mStoreFile);
		if (!store.load()){
			Log.d("DEBUG","Store.load == null");
			if ( store.getGestureEntries() == null){
				mToast.Make(this, "Error while obtain the Gestures Library. Try to close and open Gesture Call", 0);
				return false;
			}
		}

		return true;
	}

	/**
	 * 
	 * Metodo al que se llama cuadno se pulsa el boton 
	 * cancelar. Si no hay gesto en la pantalla se vuelve
	 * para atr�s.
	 * Si hay un gesto en la pantalla se cancela.
	 * 
	 * @param v boton que activa el metodo
	 */
	public void cancelGesture(View v) {

		if (overlay.getGesture() == null){
			setResult(main.RESULT_OK);
			finish();
		}
		else{
			overlay.clear(true);
			mDoneButton.setEnabled(false);
			ButtonCancel.setText(getResources().getString(R.string.cancelGesture2));
		}
	}


	/**
	 * 
	 * Metodo al que se llama cuadno se pulsa el a�adir. Guarda y asigna
	 * el gesto al contacto seleccionado.
	 * 
	 * @param v boton que activa el metodo
	 */
	public void doneGesture(View v) {		
		gestureActual = mGesture;
		addGesture(v);

	}

	/**
	 * Carga el tema segun las preferencias del usuarios
	 * 
	 */
	public void setTheme(){
		int  theme;
		try {
			theme = ap.getInt(AppConfig.THEME);
		} catch (NoPreferenceException e) {
			Log.i("Gesture Call","No Theme preference. Apply Default GREY");
			theme = Themes.GREY;
			ap.put(Themes.GREY,AppConfig.THEME);
		}

		Log.d("DEBUG","puto theme " + theme);

		switch (theme) {
		case Themes.GREY:
			lay_main.setBackgroundResource(R.drawable.background_grey);
			//			overlay.setGestureColor(getResources().getColor(R.color.overlay_grey));
			//			overlay.setGestureColor(Color.WHITE);
			//			overlay.setUncertainGestureColor(getResources().getColor(R.color.overlay_grey_uncertain));
			texto.setTextColor(getResources().getColor(R.color.white));

			break;
		case Themes.BLUE:
			lay_main.setBackgroundResource(R.drawable.background_blue_gradient);
			//			overlay.setGestureColor(getResources().getColor(R.color.overlay_blue));
			//			overlay.setUncertainGestureColor(getResources().getColor(R.color.overlay_blue_uncertain));
			texto.setTextColor(getResources().getColor(R.color.black));
			break;
		case Themes.GREEN:
			lay_main.setBackgroundResource(R.drawable.background_green_gradient);
			//			overlay.setGestureColor(getResources().getColor(R.color.overlay_green));
			//			overlay.setUncertainGestureColor(getResources().getColor(R.color.overlay_green_uncertain));
			texto.setTextColor(getResources().getColor(R.color.black));
			break;
		case Themes.BLACK:
			lay_main.setBackgroundResource(R.drawable.background_black_gradient);
			texto.setTextColor(getResources().getColor(R.color.white));
			break;
		case Themes.WHITE:
			lay_main.setBackgroundResource(R.drawable.background_white_gradient);
			texto.setTextColor(getResources().getColor(R.color.black));
			break;

		default:
			lay_main.setBackgroundResource(R.drawable.background_grey);
			texto.setTextColor(getResources().getColor(R.color.white));
			break;
		}
	}




	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			setTheme();
		}

	}




	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		somaBaner.setAutoRefresh(false);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		somaBaner.setAutoRefresh(true);
	}




	private class GesturesProcessor implements GestureOverlayView.OnGestureListener {
		public void onGestureStarted(GestureOverlayView overlay, MotionEvent event) {
			mDoneButton.setEnabled(false);
			mGesture = null;
			ButtonCancel.setText(getResources().getString(R.string.cancelGesture));
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
