package ac.gestureCall.util.location;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;


/**
 * This class has the missionto get the current location, save it and manage it.
 * 
 * @author marcos
 *
 */
public class GetCurrentLocation implements Runnable {

	private Context mContext;
	private static GetCurrentLocation gcl = null;
	public static Location currentLocation = null;	
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	private static Thread thread;
	private Handler handler = null;




	/**
	 * @param Context de la aplicacion
	 * 
	 * Metodo que se encarga de crear la instancia de la clase de geolocalizacion
	 */
	private synchronized static void createInstance(Context c) {
		if (gcl == null) gcl = new GetCurrentLocation(c);
	}//end method
	

	/**
	 * @param Context de la aplicacion
	 * @param han Handler with the handler that we want to call when the location
	 * will found.
	 * 
	 * Metodo que se encarga de crear la instancia de la clase de geolocalizacion
	 */
	private synchronized static void createInstance(Context c, Handler han) {
		if (gcl == null) gcl = new GetCurrentLocation(c, han);
	}//end method


	/**
	 * @param Context context de la aplicacion
	 */
	public static GetCurrentLocation getInstance(Context c) {
		if (gcl == null) createInstance(c);

		//if (currentLocation == null) getCurrentLocation();

		return gcl;
	}//end methodd
	
	/**
	 * @param Context context de la aplicacion
	 * @param han Handler with the handler that we want to call when the location
	 * will found.
	 */
	public static GetCurrentLocation getInstance(Context c, Handler han) {
		if (gcl == null) createInstance(c,han);

		//if (currentLocation == null) getCurrentLocation();

		return gcl;
	}//end methodd


	protected GetCurrentLocation(Context c){
		mContext = c;
		thread = new Thread(this,"Get Location");
		thread.start();

	}
	
	protected GetCurrentLocation(Context c, Handler han){
		mContext = c;
		handler = han;
		
		thread = new Thread(this,"Get Location");
		thread.start();

	}

	/**
	 * Use this method to put a handler for the listener
	 * this handler is activated when location is found.
	 * @param han handle that you want to activate
	 * when location is found.
	 */
	public synchronized void setHandler(Handler han){
		handler = han;
	}
	
	//Location

	public static void setCurrentLocation(Location loc) {
		currentLocation = loc;
	}


	@Override
	public void run() {

		try{

			mLocationManager = (LocationManager)mContext.getSystemService(Context.LOCATION_SERVICE);

			if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				Looper.prepare();
				Log.i("GestureCall","Location obtain with GPS.");
				mLocationListener = new MyLocationListener();
				mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocationListener);
				Looper.loop(); 
				Looper.myLooper().quit();  

			} else if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				Looper.prepare();
				Log.i("GestureCall","Location obtain with network.");
				mLocationListener = new MyLocationListener();
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
				Looper.loop();
				Looper.myLooper().quit();
			}else{
				Looper.prepare();;
				if (handler != null) handler.sendEmptyMessage(0);
								
				Log.i("GestureCall","Location cant be find.");
				Looper.loop();
				Looper.myLooper().quit();
			}	
		}
		catch(SecurityException e)
		{
			Log.i("GestureCall", e.getMessage() + " Continue with network_provider");
			
			if (mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
				Looper.prepare();
				Log.i("GestureCall","Location obtain with network.");
				mLocationListener = new MyLocationListener();
				mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, mLocationListener);
				Looper.loop();
				Looper.myLooper().quit();
			}else{
				Looper.prepare();;
				if (handler != null) handler.sendEmptyMessage(0);
								
				Log.i("GestureCall","Location cant be find.");
				Looper.loop();
				Looper.myLooper().quit();
			}	
		}
	}


	/**
	 * @author marcos
	 * 
	 * Clase de apoyo que extiende de LocationListener
	 * que se utiliza para gestionar los cambios
	 * en la recepción GPS tales como una nueva localizacion
	 * o un cambio de Status
	 */
	private class MyLocationListener implements LocationListener 
	{
		@Override
		public void onLocationChanged(Location loc) {
			if (loc != null) {
				setCurrentLocation(loc);
				//           handler.sendEmptyMessage(0);
				Log.i("GestureCall","Location: " + loc.toString());
				
				//Stop looking for locations
				mLocationManager.removeUpdates(mLocationListener);
				
				//If handler isnt null, call it 
				if (handler != null){
					Message msg = new Message();
					msg.obj = loc;
					handler.sendMessage(msg);
				}
			}

			
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onStatusChanged(String provider, int status, 
				Bundle extras) {
		}
	}  

}