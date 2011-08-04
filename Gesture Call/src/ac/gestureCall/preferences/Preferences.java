/**
 * 
 */
package ac.gestureCall.preferences;

import ac.gestureCall.R;
import ac.gestureCall.exceptions.NoPreferenceException;
import ac.gestureCall.ui.main;
import ac.gestureCall.util.config.AppConfig;
import ac.gestureCall.util.mToast.mToast;
import ac.gestureCall.util.shortcut.CreateShortcut;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

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
	
	public void clickAbout(View v){
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.credits_layout);
		dialog.setTitle("Credits:");
		
		dialog.show();
	}
	
	public void clickContact(View v){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("plain/text");
		i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"aracemcskyline@gmail.com"});
		startActivity(Intent.createChooser(i, "Send mail..."));
		
		
	}
	
	
	public void clickNovedades(View v){
		Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.whats_new);
		dialog.setTitle("Whats new");
		
		dialog.show();
	}
	
	
	public void clickShortcut(View v){
		mToast.Make(this, getResources().getString(R.string.creando), 0);
		LinearLayout l = (LinearLayout)findViewById(R.id.pref_lay_shortcut);
		l.setEnabled(false);
		CreateShortcut.create(this,"ac.gestureCall.ui.main");
		
	}
	
	public void clickDonate(View v){
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("market://details?id=ac.gestureCallPro"));
		//intent.setData(Uri.parse("amzn://apps/android?p=ac.gestureCallPro")); //enlace con la app de amazon	
		startActivity(intent);
	}
	
	
	
	//Menu para los creditos
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_creditos, menu);
		return true;
	}
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle item selection
		switch (item.getItemId()) {
		
		
			case R.id.me_creditos:
				
				
				Dialog dialog = new Dialog(this);
				dialog.setContentView(R.layout.credits_layout);
				dialog.setTitle("Credits:");
				
				dialog.show();
				
				
				
				return true;
		
				
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	
	
	
	
	
	
	
}