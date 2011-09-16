package ac.gestureCall.util.adMobListener;

import android.util.Log;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.InterstitialAd;

public class AdMobListenerInterstitial implements AdListener {
	private InterstitialAd interstitial;
	
	
	public AdMobListenerInterstitial (InterstitialAd inter){
		interstitial = inter;
	}

	@Override
	public void onDismissScreen(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
		AdRequest ar = new AdRequest();
		ar.addTestDevice(AdRequest.TEST_EMULATOR);
		interstitial.loadAd(ar);
		
	}

	@Override
	public void onLeaveApplication(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPresentScreen(Ad arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReceiveAd(Ad arg0) {
		Log.d("Ads", "onReceiveAd");
		interstitial.show();
		
	}

}
