package ac.gestureCall.util.smaatoListener;

import ac.gestureCall.util.location.GetCurrentLocation;
import ac.gestureCall.util.mobclixListener.MobclixListener;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
import com.smaato.SOMA.AdDownloader;
import com.smaato.SOMA.AdListener;
import com.smaato.SOMA.ErrorCode;
import com.smaato.SOMA.SOMABanner;
import com.smaato.SOMA.SOMAReceivedBanner;

public class SmaatoListener implements AdListener{
	private SOMABanner somaBaner;
	private MobclixMMABannerXLAdView mobclixView = null;
	private AdView adView = null;
	private int count = 0;
	private Context mContext;


	public SmaatoListener (SOMABanner sb){
		somaBaner = sb;
	}

	public SmaatoListener (SOMABanner sb,MobclixMMABannerXLAdView mv ){
		somaBaner = sb;
		mobclixView = mv;
	}

	public SmaatoListener (SOMABanner sb,AdView ad ){
		somaBaner = sb;
		adView = ad;
	}
	
	public SmaatoListener (SOMABanner sb,AdView ad , Context context){
		somaBaner = sb;
		adView = ad;
		mContext = context;
	}


	@SuppressWarnings("static-access")
	@Override
	public void onFailedToReceiveAd(AdDownloader adDownloader, ErrorCode errCode) {
		if (errCode == ErrorCode.NO_AD_AVAILABLE){
			Log.d("Ads","Error al recibir ads " + count);
			if (count > 0){

				if (mobclixView != null){
					mobclixView.addMobclixAdViewListener(new MobclixListener());
					somaBaner.setVisibility(View.GONE);
					somaBaner.setAutoRefresh(false);
				}
				else if (adView != null){

					AdRequest request = new AdRequest();
					if (mContext != null)
						if (GetCurrentLocation.getInstance(mContext).currentLocation != null)
							request.setLocation(GetCurrentLocation.getInstance(mContext).currentLocation);

					adView.loadAd(request);

				}

			}
			else{
				somaBaner.asyncLoadNewBanner();
				count++;
			}

		}

	}

	@Override
	public void onReceiveAd(AdDownloader arg0, SOMAReceivedBanner arg1) {
		Log.d("Ads","Banner recibido SmaatoBanner");
		somaBaner.setVisibility(View.VISIBLE);
		somaBaner.setAutoRefresh(true);
		count = 0;
	}


}