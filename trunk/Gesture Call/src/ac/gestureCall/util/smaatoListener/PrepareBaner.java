package ac.gestureCall.util.smaatoListener;

import android.content.Context;

import com.google.ads.AdView;
import com.mobclix.android.sdk.MobclixMMABannerXLAdView;
import com.smaato.SOMA.AdDownloader.MediaType;
import com.smaato.SOMA.SOMABanner;
import com.smaato.SOMA.SOMABanner.AnimationType;

public class PrepareBaner {
        
        private static final int PUBLISHERID = 923850990; 
        private static final int ADSSPACE = 65748253;
        
        /**
         * Metodo estatico para configurar la publicidad de 
         * smaato con ciertos valores preconfigurados, fácil
         * de mover entre proyectos.
         * @param sb baner que queremos configurar
         */
        public static void prepareAndCall(SOMABanner sb){
                
                sb.setPublisherId(PUBLISHERID);
                sb.setAdSpaceId(ADSSPACE);
                
                sb.setLocationUpdateEnabled(true);
                sb.setMediaType(MediaType.ALL);
                
                sb.setAutoRefresh(true);
                
                sb.setAnimationType(AnimationType.RANDOM_ANIMATION);
                
                sb.addAdListener(new SmaatoListener(sb));
                
                sb.asyncLoadNewBanner();
                
        }
        
        /**
         * Metodo estatico para configurar la publicidad de 
         * smaato con ciertos valores preconfigurados, fácil
         * de mover entre proyectos.
         * @param sb baner que queremos configurar
         */
        public static void prepareAndCall(SOMABanner sb,MobclixMMABannerXLAdView mobclixView){
                
                sb.setPublisherId(PUBLISHERID);
                sb.setAdSpaceId(ADSSPACE);
                
                sb.setLocationUpdateEnabled(true);
                sb.setMediaType(MediaType.ALL);
                
                sb.setAutoRefresh(true);
                
                sb.setAnimationType(AnimationType.RANDOM_ANIMATION);
                
                sb.addAdListener(new SmaatoListener(sb,mobclixView));
                
                sb.asyncLoadNewBanner();
                
        }
        
        
        /**
         * Metodo estatico para configurar la publicidad de 
         * smaato con ciertos valores preconfigurados, fácil
         * de mover entre proyectos.
         * @param sb baner que queremos configurar
         */
        public static void prepareAndCall(SOMABanner sb,AdView ad){
                
                sb.setPublisherId(PUBLISHERID);
                sb.setAdSpaceId(ADSSPACE);
                
                sb.setLocationUpdateEnabled(true);
                sb.setMediaType(MediaType.ALL);
                
                sb.setAutoRefresh(true);
                
                sb.setAnimationType(AnimationType.RANDOM_ANIMATION);
                
                sb.addAdListener(new SmaatoListener(sb,ad));
                
                sb.asyncLoadNewBanner();
                
        }
        
        
        /**
         * Metodo estatico para configurar la publicidad de 
         * smaato con ciertos valores preconfigurados, fácil
         * de mover entre proyectos.
         * @param sb baner que queremos configurar
         */
        public static void prepareAndCall(SOMABanner sb,AdView ad,Context context){
                
                sb.setPublisherId(PUBLISHERID);
                sb.setAdSpaceId(ADSSPACE);
                
                sb.setLocationUpdateEnabled(true);
                sb.setMediaType(MediaType.ALL);
                
                sb.setAutoRefresh(true);
                
                sb.setAnimationType(AnimationType.RANDOM_ANIMATION);
                
                sb.addAdListener(new SmaatoListener(sb,ad,context));
                
                sb.asyncLoadNewBanner();
                
        }
        
        

}