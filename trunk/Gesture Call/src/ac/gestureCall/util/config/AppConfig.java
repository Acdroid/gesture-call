
package ac.gestureCall.util.config;

import ac.gestureCall.R;
import ac.gestureCall.util.mToast.mToast;
import android.content.Context;

/**
 * AppConfig.java 30/03/2011
 * 
 * Esta clase, que extiende de la clase MSharedPreferences,
 * se encarga de interceder entre esta y la aplicacion
 * facilitando su uso. Contiene una serie de Static con
 * todas las posibles preferencias que se pueden guardar.
 * Durante el primer uso guarda la configuración inicial.
 * @author mtrujillo & cdiaz
 */
public class AppConfig extends MSharedPreferences{
	public static final String FIRST_TIME = "first_time";



	public AppConfig(Context mContext, String name){
		super(mContext,name);

		//Comprobamos si es la primera vez que se llama alconstructor, en tal caso
		//se guardan los valores iniciales
		if(!pref.contains(FIRST_TIME)){
			mToast.Make(mContext,mContext.getResources().getString(R.string.first_time_toast), 1);

			//Procedemos a cargar los valores por primera vez en las preferencias.
			//Estos valores son por defecto

			//Valores por defecto
			put(false,FIRST_TIME); //Flag para indicar que no es la primera vez que se usa

		}

	}
}
