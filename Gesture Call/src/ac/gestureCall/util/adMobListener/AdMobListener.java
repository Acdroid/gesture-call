package ac.gestureCall.util.adMobListener;

import java.util.Timer;
import java.util.TimerTask;

import ac.gestureCall.ui.autopublicidad.Autopublicidad;
import android.os.Looper;
import android.util.Log;
import android.view.View;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;

public class AdMobListener implements AdListener {
	
	private AdView adView;
	private Autopublicidad autopublicidad;
	boolean flag = false; //Se estam mostrando autopublicidad?
	Timer tiempo;
	
	public AdMobListener (AdView ad,Autopublicidad au){
		adView = ad;
		autopublicidad = au;
	}

	@Override
	public void onDismissScreen(Ad adView) {
		Log.d("Ads","On dismissScreen");
		
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode error) {
		Log.d("Ads","On dismissScreen");
		Log.i("Gesture Call","Error obteniendo la publicidad. ErrorCode: " + error );
		adView.setVisibility(View.GONE);
		autopublicidad.setVisibility(View.VISIBLE);
		flag=true;
		tiempo = new Timer();
		tiempo.schedule(new timerTask(), 17000, 17000);
		
		
		
	}

	@Override
	public void onLeaveApplication(Ad ad) {
		Log.d("Ads","On leaveApplication");
		
	}

	@Override
	public void onPresentScreen(Ad ad) {
		Log.d("Ads","On PresentScreen");
		
	}

	@Override
	public void onReceiveAd(Ad ad) {
		Log.d("Ads","On receiveAd");
		if (flag){
			autopublicidad.setVisibility(View.GONE);
			adView.setVisibility(View.VISIBLE);
			flag=false;
			tiempo.purge();
			
		}
	}
	
	
	public class timerTask extends TimerTask {

		@Override
		public void run() {
			Looper.prepare();
			adView.loadAd(new AdRequest());
			Looper.loop();
			Looper.myLooper().quit();
			tiempo.cancel();
			
		}
		
	}

}
