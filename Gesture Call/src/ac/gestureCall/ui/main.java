package ac.gestureCall.ui;


import java.util.HashMap;

import ac.gestureCall.R;
import ac.gestureCall.ui.contactos.ListContact;
import ac.gestureCall.ui.gestos.GestureBuilderActivity;
import ac.gestureCall.util.config.AppConfig;
import ac.gestureCall.util.gestures.GesturesRecognizer;
import ac.gestureCall.util.mToast.mToast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

public class main extends Activity {
    
	public static final String NO_PREDICCION = "Sin_Resultado";
	public static final int RESULT_OK = 0;
	public static final int RESULT_ERROR = 1;
	public static final int RESULT_SALIR = 2;
	public static final int RESULT_REALOAD_GESTURES = 3;
	public static final int RESULT_GESTO_ADD_OK = 4;
	public static final int ID = 0;
	public static final int DIALOG_SALIR = 0;
	public static final String MY_AD_UNIT_ID = "a14daeadcc3acb6";
	
	public GestureOverlayView overlay;
	public static GesturesRecognizer gr;
	
	public AdView adView;
	
	private final String dir = Environment.getExternalStorageDirectory() + "/GestureCall";
	private final String fich = "gestures";
	//HAndler encargado de recibir las predicciones del
	public Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			call((String) msg.obj);
		}

	};
	
	
	public Context mContext;
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
    }
    
    
    private void init(){
    	
    	mContext = this;
    	
        // Look up the AdView as a resource and load a request.
        adView = (AdView)this.findViewById(R.id.Publicidad);
        adView.loadAd(new AdRequest());
    	
    	AppConfig ap = new AppConfig(this, AppConfig.NAME);
    	overlay = (GestureOverlayView)findViewById(R.id.gestures);
    	
    	try {
    		//gr = new GesturesRecognizer(mStoreFile, this, overlay,handler,GesturesRecognizer.RECONOCEDOR_BASICO);
    		gr = new GesturesRecognizer(dir,fich, overlay, handler, GesturesRecognizer.RECONOCEDOR_BASICO);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage() + "\nNo esta habilitado el reconocedor de gestos.",Toast.LENGTH_SHORT).show();
		} //Reconocedor, lo cargamos con la base de datos de accesos directos
		
    	
    }
    
    
	@Override
	protected Dialog onCreateDialog(int id) {
		@SuppressWarnings("unused")
		Dialog dialog;
		switch(id) {
		case DIALOG_SALIR:
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setMessage(mContext.getResources().getString(R.string.di_salir))
			.setCancelable(false)
			.setPositiveButton(mContext.getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					//Comunicador.this.finish();
					getStore().save();
					((Activity) mContext).setResult(RESULT_OK);
					((Activity) mContext).finish();
				}
			})
			.setNegativeButton(mContext.getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					dialog.cancel();
				}
			});
			AlertDialog alert = builder.create();
			return alert;
		default:
			return dialog = null;
		}
	}
    
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_escritorio, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.me_salir:
			showDialog(DIALOG_SALIR);
			return true;
		case R.id.me_gestures:
			clickEdit(null);
			return true;
		case R.id.me_opciones:
			mToast.Make(this, "Pulsado opciones en general", 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ID){
			switch (resultCode){
			case RESULT_OK:
				break;
			case RESULT_ERROR:
				break;
			case RESULT_SALIR:
				main.this.finish();
			case RESULT_REALOAD_GESTURES:
				getStore().load();
				break;
			default:
				
			}
			
			getStore().load();
		}
	}

	
	
    public void call(String prediccion){
    	
    	if (prediccion.equals("")){
    		mToast.Make(this, getResources().getString(R.string.no_gesto), 0);
    		return;
    	}
    	
    	String url = "tel:" + prediccion;
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivityForResult(i,ID);
    	
    	mToast.Make(this, "Quieres llamar a : " + prediccion, 0);

    }
    
    public void clickAdd(View v){
    	Intent i = new Intent(main.this,ListContact.class);
    	startActivityForResult(i, ID);
    }
    
    public void clickEdit(View v){
		Intent i = new Intent(main.this,GestureBuilderActivity.class);
		startActivityForResult(i,ID);
    }
    
    public void clickOpciones(View v){
    	
    }
    
    public static GestureLibrary getStore(){
    	return gr.getStore();    	
    }
    
}