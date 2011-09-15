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
import com.google.ads.InterstitialAd;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdView;

public class AdMobListenerInterstitial implements AdListener {
	
	private InterstitialAd interstitial;
	
	public AdMobListenerInterstitial (InterstitialAd i){
		interstitial = i;
	}

	@Override
	public void onDismissScreen(Ad adView) {
		Log.d("Ads","On dismissScreen");
		
	}

	@Override
	public void onFailedToReceiveAd(Ad ad, ErrorCode error) {
		Log.d("Ads","Interstitial On failed to receivead");

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
		interstitial.show();
	}

}
