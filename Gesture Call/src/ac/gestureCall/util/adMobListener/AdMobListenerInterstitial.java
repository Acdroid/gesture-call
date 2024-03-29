package ac.gestureCall.util.adMobListener;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.Looper;
import android.util.Log;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;
import com.mobclix.android.sdk.MobclixFullScreenAdView;


public class AdMobListenerInterstitial implements AdListener {

	private InterstitialAd adView;
	private boolean flag = false; //Se estam mostrando autopublicidad?
	private Timer tiempo;
	private Activity activity = null;

	public AdMobListenerInterstitial(InterstitialAd ad){
		adView = ad;
	}

	public AdMobListenerInterstitial(InterstitialAd ad, Activity act){
		adView = ad;
		this.activity = act;
	}

	@Override
	public void onDismissScreen(Ad adView) {
		Log.d("Ads","On dismissScreen");

	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode error) {
		Log.d("Ads"," dismissScreen");
		//		if (error != ErrorCode.NETWORK_ERROR){
		//				
		//			Log.i("Gesture Call","Error obteniendo la publicidad. ErrorCode: " + error );
		//			adView.setVisibility(View.GONE);
		//			autopublicidad.setVisibility(View.VISIBLE);
		//			flag=true;
		//			tiempo = new Timer();
		//			tiempo.schedule(new timerTask(), 17000, 17000);
		//		}

		if (activity != null){
			MobclixFullScreenAdView mobClixFSAdView = new MobclixFullScreenAdView(activity);
			mobClixFSAdView.requestAndDisplayAd();
		}


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

		//adView.show();

		//		if (flag){
		//			autopublicidad.setVisibility(View.GONE);
		//			adView.setVisibility(View.VISIBLE);
		//			flag=false;
		//			tiempo.purge();
		//			
		//		}
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
