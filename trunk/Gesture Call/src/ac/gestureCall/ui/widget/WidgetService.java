/**
 * Acdroid Apps for Android
 * 
 * @author Carlos Diaz Canovas
 * @author Marcos Trujillo Seoane
 * 
 * Project Gesture Call
 * 
 */
package ac.gestureCall.ui.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

public class WidgetService extends Service{

    //accion creada para la comprensión entre llamadas y para identificar
    //lo que debe hacer
    public static final String UNA_ACCION = "ac.gestureCall.ui.main";
    
    //clase del Widget principal con la que se hará la comunicación
    //ya que esto es un servicio y necesita contactar con algo
    private WidgetMain mAppWidgetProvider = WidgetMain.getInstance();
    
    //variable para el uso del programa, pudes usar lo que quieras, eso depende de ti
    //esto es un ejemplo
    private int value = 5;
    
    /**
     * Recibe mensajes externos y los evalua llamando a la función de actualización
     * del widget || En principio no es necesario, pero lo ponemos porque puede
     * ser útil para llamadas externas
     */
    private BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            
            String action = intent.getAction();
            
            int[] appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            mAppWidgetProvider.performUpdate(WidgetService.this, appWidgetIds);
        }
    };
    
    /**
     * Método llamado en la creación con el que registraremos el servicio 
     * y las posibles acciones que pueda tener
     */
    @Override
     public void onCreate() {
            super.onCreate();
            
            IntentFilter commandFilter = new IntentFilter();
            //añadimos la accion, si ponemos más que es lo normal las añadiremos
            //aquí seguidas una detrás de otra
            commandFilter.addAction(UNA_ACCION);            
            registerReceiver(mIntentReceiver, commandFilter);
     }
    
    /**
     * simplemente se pone 
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    /**
     * Hay que acordarse de que los servicios hay que cerrarlos y cuando se cierran
     * hay que desregistrarlos, con este método lo hacemos
     */
    @Override
    public void onDestroy(){
         unregisterReceiver(mIntentReceiver);
         super.onDestroy();
    }
     
    /**
     * Método que es llamado siempre que se recibe un aviso, por lo que es donde
     * evaluamos las acciones
     */
    @Override
    public void onStart(Intent intent, int startId) {
    	Log.d("DEBUG","una acción y tal pascual1");
        //recogemos la accion
        String action = intent.getAction();
        Log.d("DEBUG",action + " una acción y tal pascual");
        
        if(UNA_ACCION.equals(action)){
            //hacemos lo que tengamos que hacer, por ejemplo poner una variable a 0
        	Log.d("DEBUG","una acción y tal pascual2");
            value = 0;
        }
        Log.d("DEBUG","una acción y tal pascual3");
        //llamamos al método que hace la notificación (se podría hacer desde aquí
        //directamente pero es bueno trabajar por capas, además este método lo podremos
        //utilizar si decidimos también utilizar el BroadcastReceiver
        notifyChange(action);        
    }
    
    /**
     * Método de notificación. Contacta con el widget y le envia la notificación 
     * según el campo what que le llega, que también se puede cambiar, eso depende
     * de como quieras gestionar la información entre los métodos
     * 
     * @param what
     */
     private void notifyChange(String what) {
        Intent i = new Intent(what);
        sendBroadcast(i);
        Log.d("DEBUG","una acción y tal pascual55");
        mAppWidgetProvider.notifyChange(this, what);
     }

     /**
      * Método que sirve para devolver el valor de value, no es necesario, pero si 
      * guardamos variables en el servicio con métodos podemos devolver los datos,
      * o enviarlos dentro del intent desde el notifyChange
      * 
      * @return
      */
     public int getValue(){
         return value;
     }
}