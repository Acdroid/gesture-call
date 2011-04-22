package ac.gestureCall.ui.widget;

import ac.gestureCall.R;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetMain  extends AppWidgetProvider{
    
    //Componente que identifica esta clase
    static final ComponentName THIS_APPWIDGET =
        new ComponentName("ac.gestureCall.ui.widget","ac.gestureCall.ui.widget.WidgetMain");
         
    //instancia de la clase
    private static WidgetMain sInstance;
    
    /**metodo que devuelve una instancia de la clase para que luego se puedan hacer
     * llamadas sobre ella, lo utiliza el servicio
     * @return
     */    
    static synchronized WidgetMain getInstance() {
        if (sInstance == null) {
            sInstance = new WidgetMain();
        }
        return sInstance;
    }
    
    /**
     * Método llamado en la subida del widget y que contendrá la llamada a todas
     * las inicializaciones necesarias
     */
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
            int[] appWidgetIds) {
        
        //llamada al método de inicializaciones
        defaultAppWidget(context, appWidgetIds);    
        
        //llamada al registro del servicio (antes se ha tenido que hacer todo el
        //defaultAppWidget
        Intent updateIntent = new Intent(context, WidgetService.class);
        updateIntent.addFlags(Intent.FLAG_RECEIVER_REGISTERED_ONLY);
        context.sendBroadcast(updateIntent);
    }

    /**
     * Método en el que hay que inicializar las vistas, exceptuando los botones 
     * que se le llamará desde dentro a otro método
     * 
     * @param context
     * @param appWidgetIds
     */
    private void defaultAppWidget(Context context, int[] appWidgetIds) {
        final Resources res = context.getResources();
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);
        
        //en los Widgets se trabaja con RemoteViews y no Views directamente por lo que
        //recogeremos las RemoteViews siempre e inicializaremos los valores de nuestras
        //vistas, por ejemplo:
        //views.setTextViewText(R.id.text, "Inicializando campo de texto");

        linkButtons(context, views);
        pushUpdate(context, appWidgetIds, views);
    }

    /**
     * Método que actualiza la parte gráfica. Todo el tiempo se juega con RemoteViews y
     * se van pasando de método en método hasta llegar aquí que es donde se cierran y se
     * muestran. No se mete en otros métodos ya que siempre hay que acabar llamando a este método
     * y a veces algunos métodos son llamados al principio y otros cada cierto tiempo por lo
     * que tendriamos que repetir código 
     * 
     * @param context
     * @param appWidgetIds
     * @param views
     */
    private void pushUpdate(Context context, int[] appWidgetIds, RemoteViews views) {
        final AppWidgetManager gm = AppWidgetManager.getInstance(context);
        if (appWidgetIds != null) {
            gm.updateAppWidget(appWidgetIds, views);
        } else {
            gm.updateAppWidget(THIS_APPWIDGET, views);
        }
    }
    
    /**
     * Método llamado cuando se ha realizado una acción, para ello nos podemos valer
     * de valores recibidos desde el servicio o más fácil si hemos creado métodos
     * en el Servicio para recoger las variables podremos recogerlas directamente
     * 
     * @param service
     * @param appWidgetIds
     */
    void performUpdate(WidgetService service, int[] appWidgetIds) {
        final Resources res = service.getResources();
        final RemoteViews views = new RemoteViews(service.getPackageName(), R.layout.main);
        
        //ejemplo de lo que haríamos, cambiaríamos el texto de un TextView y añadiríamos
        //el nuevo valor evaluado por el servicio tras el pulsamiento de un botón
        //views.setTextViewText("El nuevo valor es:" + service.getValue());
        
       Log.d("DEBUG","pulsación o algo?");
        
        //llama al método que actualiza las vistas
        pushUpdate(service, appWidgetIds, views);
    }

    /**
     * Links a todos los botones que va a tener el widget, es bueno tenerlo
     * separado ya que se hará sólo la primera vez
     * 
     * @param context
     * @param views
     */
    private void linkButtons(Context context, RemoteViews views) {
        //intents y el servico que necesitaremos para crear la interconexión botón-servicio
        Intent intent;
        PendingIntent pendingIntent;

        final ComponentName serviceName = new ComponentName(context, WidgetService.class);
        
        //Ejemplo de como se puede linkar un botón y como ves hay que darle una acción
        //que luego en el servicio recogeremos y cambiaremos el desarrollo en función 
        //de eso, para ello necesitamos utilizar pending intent, no hay OnClickListener
        intent = new Intent(WidgetService.UNA_ACCION);
        intent.setComponent(serviceName);
        pendingIntent = PendingIntent.getService(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.widget_imagebutton, pendingIntent);
                
    }
        
    /**
     * Método de notificacion del Widget, es el método que comunica con el otro método
     * de notificación pero del servicio, de esta manera tenemmos todo más ordenado.
     * Lo primero que hace es comprobar la instancia del servicio y después lo pasa a
     * producción
     * 
     * @param service
     * @param what
     */
    void notifyChange(WidgetService service, String what) {
        if (hasInstances(service)) {
            performUpdate(service, null);
        }
    }
    
    /**
     * Comprueba que esté instanciado correctamente la variable que se recibe como 
     * servicio
     * @param context
     * @return
     */
    private boolean hasInstances(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(THIS_APPWIDGET);
        return (appWidgetIds.length > 0);
    }
   
}