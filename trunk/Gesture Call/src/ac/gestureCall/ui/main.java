package ac.gestureCall.ui;


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

public class main extends Activity {
    
	public static final String NO_PREDICCION = "Sin_Resultado";
	public static final int RESULT_OK = 0;
	public static final int ID = 0;
	public static final int DIALOG_SALIR = 0;
	
	public GestureOverlayView overlay;
	public static GesturesRecognizer gr;
	
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
			Intent i = new Intent(main.this,GestureBuilderActivity.class);
			mContext.startActivity(i);
			return true;
		case R.id.me_opciones:
			mToast.Make(this, "Pulsado opciones en general", 0);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	
	
    public void call(String prediccion){
    	
    	if (prediccion.equals("")){
    		mToast.Make(this, "No se ha reconocido el gesto", 0);
    		return;
    	}
    	
    	String url = "tel:" + prediccion;
        Intent i = new Intent(Intent.ACTION_CALL, Uri.parse(url));
        startActivityForResult(i,ID);
    	
    	mToast.Make(this, "Quieres llamar a : " + prediccion, 0);

    }
    
    public void clickAdd(View v){
    	mToast.Make(this, "Añadir gesto",0);
    	Intent i = new Intent(main.this,ListContact.class);
    	startActivityForResult(i, ID);
    }
    
    public static GestureLibrary getStore(){
    	return gr.getStore();    	
    }
}