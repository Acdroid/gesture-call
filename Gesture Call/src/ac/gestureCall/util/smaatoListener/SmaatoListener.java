package ac.gestureCall.util.smaatoListener;

import ac.gestureCall.util.mobclixListener.MobclixListener;
import android.util.Log;
import android.view.View;

import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
import com.smaato.SOMA.AdDownloader;
import com.smaato.SOMA.AdListener;
import com.smaato.SOMA.ErrorCode;
import com.smaato.SOMA.SOMABanner;
import com.smaato.SOMA.SOMAReceivedBanner;

public class SmaatoListener implements AdListener{
        private SOMABanner somaBaner;
        private MobclixMMABannerXLAdView mobclixView = null;
        private int count = 0;
        
        
        public SmaatoListener (SOMABanner sb){
                somaBaner = sb;
        }
        
        public SmaatoListener (SOMABanner sb,MobclixMMABannerXLAdView mv ){
                somaBaner = sb;
                mobclixView = mv;
        }
                

        @Override
        public void onFailedToReceiveAd(AdDownloader adDownloader, ErrorCode errCode) {
                if (errCode == ErrorCode.NO_AD_AVAILABLE){
                        Log.d("Ads","Error al recibir ads " + count);
                        if (count > 1){
                        		
                                mobclixView.addMobclixAdViewListener(new MobclixListener());
                                somaBaner.setVisibility(View.GONE);
                                somaBaner.setAutoRefresh(false);
                                
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