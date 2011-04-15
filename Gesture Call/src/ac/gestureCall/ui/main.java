package ac.gestureCall.ui;

import ac.gestureCall.R;
import ac.gestureCall.ui.contactos.ListContact;
import ac.gestureCall.util.config.AppConfig;
import ac.gestureCall.util.gestures.GesturesRecognizer;
import ac.gestureCall.util.mToast.mToast;
import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

public class main extends Activity {
    
	public static final String NO_PREDICCION = "Sin_Resultado";
	public static final int RESULT_OK = 0;
	public static final int ID = 0;
	
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
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        init();
    }
    
    
    private void init(){
    	
    	AppConfig ap = new AppConfig(this, AppConfig.NAME);
    	overlay = (GestureOverlayView)findViewById(R.id.gestures);
    	
    	try {
    		//gr = new GesturesRecognizer(mStoreFile, this, overlay,handler,GesturesRecognizer.RECONOCEDOR_BASICO);
    		gr = new GesturesRecognizer(dir,fich, overlay, handler, GesturesRecognizer.RECONOCEDOR_BASICO);
		} catch (Exception e) {
			Toast.makeText(this, e.getMessage() + "\nNo esta habilitado el reconocedor de gestos.",Toast.LENGTH_SHORT).show();
		} //Reconocedor, lo cargamos con la base de datos de accesos directos
		
    	
    }
    
    
    public void call(String prediccion){
    	
    	if (prediccion.equals("")){
    		mToast.Make(this, "No se ha reconocido el gesto", 0);
    		return;
    	}
    	
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