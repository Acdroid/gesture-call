/**
 * 
 */
package ac.gestureCall.preferences;

import ac.gestureCall.R;
import ac.gestureCall.exceptions.NoPreferenceException;
import ac.gestureCall.ui.main;
import ac.gestureCall.util.config.AppConfig;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

/**
 * @author marcos
 *
 */
public class Preferences extends Activity{
	
	private AppConfig ap;
	private CheckBox c;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pref);
        ap = new AppConfig(this, AppConfig.NAME);
        c = (CheckBox) findViewById(R.id.pref_check);
        try {
			c.setChecked(ap.getBool(AppConfig.AVISO_AL_LLAMAR));
		} catch (NoPreferenceException e) {

		}
        
    }
    
	
	public void clickReturn(View v){
		setResult(main.RESULT_OK);
		Preferences.this.finish();
	}
	
	public void clickSave(View v){
		ap.put(c.isChecked(), AppConfig.AVISO_AL_LLAMAR);
		setResult(main.RESULT_OK);
		Preferences.this.finish();
	}
	
}
