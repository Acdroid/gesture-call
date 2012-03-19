package ac.gestureCall.util.rater;


import ac.gestureCall.R;
import ac.gestureCall.exceptions.NoPreferenceException;
import ac.gestureCall.util.config.AppConfig;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;




public class Rater {

	public static int APP_USES = 30;
	public static int APP_DAYS_USES = 20;
	public static int REMIND_LATER_USES_MORE = 5;
	public static int REMIND_LATER_DAYS_MORE = 2;
	//public static String TITLE_DIALOG = "Gracias por utilizar ";
	//public static String TEXT_DIALOG1 = "Si te gusta ";
	//public static String TEXT_DIALOG2 = ", ¿te importaría puntuarla en el Market? No te llevará más de un minuto. ¡Muchas gracias por tu ayuda!";
	//public static String RATE_LATER ="Recuérdamelo más tarde";
	//public static String RATE_NO_THANKS = "No, gracias";
	//public static String RATE_VALORAR = "Puntúa ";


	/**
	 * This method check the app uses and the days after install
	 * and show a dialog to motivate the user rate the app
	 * @param context context of the application
	 */
	public static void checkRater (Context context){

		AppConfig ap = new AppConfig(context, AppConfig.NAME);

		try {

			//Check if the user select not show this again
			if (!ap.getBool(AppConfig.RATER)){
				int count = ap.getInt(AppConfig.RATER_LAUCH_COUNTER);

				//protect the case when the uses will bigger than MAX_INT xDDDDDDDD
				if (count > 65000)
					count = APP_USES + 1;
				else
					count ++;
				ap.put( count, AppConfig.RATER_LAUCH_COUNTER);
				return;
			}


			int counter = ap.getInt(AppConfig.RATER_LAUCH_COUNTER) + 1;
			ap.put(counter, AppConfig.RATER_LAUCH_COUNTER);
			if (APP_USES != -1){
				//Uses +  number of remind later * 5
				if (counter >= APP_USES + (ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_USES_MORE)){
					showRaterDialog(context);
					return;
				}
			}

			
			Long dateFirstLauch = ap.getLong(AppConfig.RATER_FIRST_USE);
			if (APP_DAYS_USES != -1)
				if (System.currentTimeMillis() >= (dateFirstLauch + ( ((long)APP_DAYS_USES) * 24 * 60 * 60 * 1000) + 
				(((long)ap.getInt(AppConfig.RATER_REMIND_LATER)) * ((long)REMIND_LATER_DAYS_MORE) * 24 * 60 * 60 * 1000))  ){
					
//					Log.d("DEBUG","Ahora: " + System.currentTimeMillis() + " Cuentas: date " +dateFirstLauch + " AppDayUses " + (((long)APP_DAYS_USES) * 24 * 60 * 60 * 1000) + 
//							
//							" remind later " + (ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_DAYS_MORE * 24 * 60 * 60 * 1000) + " TOTAL " 
//							+ ( dateFirstLauch + ( APP_DAYS_USES * 24 * 60 * 60 * 1000) + 
//							(ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_DAYS_MORE * 24 * 60 * 60 * 1000)) );
//					
					showRaterDialog(context);
					return;
				}
			


		} catch (NoPreferenceException e) {
			ap.put(true, AppConfig.RATER);
			ap.put(0, AppConfig.RATER_LAUCH_COUNTER);
			ap.put(System.currentTimeMillis(), AppConfig.RATER_FIRST_USE);
			ap.put(0, AppConfig.RATER_REMIND_LATER);

		}



	}

	/**
	 * This method check the app uses and the days after install
	 * and show a dialog to motivate the user rate the app
	 * @param context context of the application
	 * @param appUses When the app uses is bigger than this number
	 * show the dialog
	 * @param daysUses When the number of days after installation 
	 * is bigger than this number show the dialog.
	 */
	public static void checkRater (Context context ,int appUses,int daysUses){
		AppConfig ap = new AppConfig(context, AppConfig.NAME);

		try {
			//Check if the user select not show this again
			if (!ap.getBool(AppConfig.RATER)){
				int count = ap.getInt(AppConfig.RATER_LAUCH_COUNTER);

				//protect the case when the uses will bigger than MAX_INT xDDDDDDDD
				if (count > 65000)
					count = APP_USES + 1;
				else
					count ++;
				ap.put( count, AppConfig.RATER_LAUCH_COUNTER);
				return;
			}

			int counter = ap.getInt(AppConfig.RATER_LAUCH_COUNTER) + 1;
			ap.put(counter, AppConfig.RATER_LAUCH_COUNTER );
			if (appUses != -1){
				//Uses +  number of remind later * 5
				if (counter >= appUses + (ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_USES_MORE)){
					showRaterDialog(context);
					return;
				}
			}

			Long dateFirstLauch = ap.getLong(AppConfig.RATER_FIRST_USE);
			if (daysUses != -1)
				if (System.currentTimeMillis() >= dateFirstLauch + (daysUses * 24 * 60 * 60 * 1000) + 
				(ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_DAYS_MORE * 24 * 60 * 60 * 1000)){
					showRaterDialog(context);
					return;
				}	


		} catch (NoPreferenceException e) {
			ap.put(true, AppConfig.RATER);
			ap.put(0, AppConfig.RATER_LAUCH_COUNTER);
			ap.put(System.currentTimeMillis(), AppConfig.RATER_FIRST_USE);
		}

	}


	/**
	 * This method check the app uses and the days after install
	 * and show a dialog to motivate the user rate the app
	 * @param context context of the application
	 */
	public static boolean mustShowRater (Context context){

		AppConfig ap = new AppConfig(context, AppConfig.NAME);

		try {

			//Check if the user select not show this again
			if (!ap.getBool(AppConfig.RATER)){
				int count = ap.getInt(AppConfig.RATER_LAUCH_COUNTER);

				//protect the case when the uses will bigger than MAX_INT xDDDDDDDD
				if (count > 65000)
					count = APP_USES + 1;
				else
					count ++;
				ap.put( count, AppConfig.RATER_LAUCH_COUNTER);
				return false;
			}


			int counter = ap.getInt(AppConfig.RATER_LAUCH_COUNTER) + 1;
			ap.put(counter, AppConfig.RATER_LAUCH_COUNTER);
			if (APP_USES != -1){
				//Uses +  number of remind later * 5
				if (counter >= APP_USES + (ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_USES_MORE)){
					return true;
				}
			}

			Long dateFirstLauch = ap.getLong(AppConfig.RATER_FIRST_USE);
			if (APP_DAYS_USES != -1)
				if (System.currentTimeMillis() >= dateFirstLauch + ( APP_DAYS_USES * 24 * 60 * 60 * 1000) + 
				(ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_DAYS_MORE * 24 * 60 * 60 * 1000)){
					return true;
				}		


		} catch (NoPreferenceException e) {
			ap.put(true, AppConfig.RATER);
			ap.put(0, AppConfig.RATER_LAUCH_COUNTER);
			ap.put(System.currentTimeMillis(), AppConfig.RATER_FIRST_USE);
			ap.put(0, AppConfig.RATER_REMIND_LATER);

		}

		return false;


	}

	/**
	 * This method check the app uses and the days after install
	 * and show a dialog to motivate the user rate the app
	 * @param context context of the application
	 * @param appUses When the app uses is bigger than this number
	 * show the dialog
	 * @param daysUses When the number of days after installation 
	 * is bigger than this number show the dialog.
	 * @return boolean true if we must to show the Rater dialog, False in other case
	 */
	public static boolean mustShowRater (Context context ,int appUses,int daysUses){
		AppConfig ap = new AppConfig(context, AppConfig.NAME);

		try {
			//Check if the user select not show this again
			if (!ap.getBool(AppConfig.RATER)){
				int count = ap.getInt(AppConfig.RATER_LAUCH_COUNTER);

				//protect the case when the uses will bigger than MAX_INT xDDDDDDDD
				if (count > 65000)
					count = APP_USES + 1;
				else
					count ++;
				ap.put( count, AppConfig.RATER_LAUCH_COUNTER);
				return false;
			}

			int counter = ap.getInt(AppConfig.RATER_LAUCH_COUNTER) + 1;
			ap.put(counter, AppConfig.RATER_LAUCH_COUNTER );
			if (appUses != -1){
				//Uses +  number of remind later * 5
				if (counter >= appUses + (ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_USES_MORE)){
					return true;
				}
			}

			Long dateFirstLauch = ap.getLong(AppConfig.RATER_FIRST_USE);
			if (daysUses != -1)
				if (System.currentTimeMillis() >= dateFirstLauch + (daysUses * 24 * 60 * 60 * 1000) + 
				(ap.getInt(AppConfig.RATER_REMIND_LATER) * REMIND_LATER_DAYS_MORE * 24 * 60 * 60 * 1000)){
					return true;
				}	


		} catch (NoPreferenceException e) {
			ap.put(true, AppConfig.RATER);
			ap.put(0, AppConfig.RATER_LAUCH_COUNTER);
			ap.put(System.currentTimeMillis(), AppConfig.RATER_FIRST_USE);
		}
		return false;

	}

	public static void showRaterDialog (final Context context){

		//Create the dialog 
		final Dialog dialog;
		if (Build.VERSION.SDK_INT > 13)
			dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog);
		else if (Build.VERSION.SDK_INT > 10)
			dialog = new Dialog(context, android.R.style.Theme_Holo_Light_Dialog);
		else
			dialog = new Dialog(context, android.R.style.Theme_Dialog);


		//Get the app name and put the title
		String app_name = context.getResources().getString(R.string.app_name);
		final String app_package = context.getApplicationInfo().packageName;
		String Title = context.getResources().getString(R.string.rate_title);
		dialog.setTitle(Title);

		//Create the layout of the dialog
		LinearLayout ll = new LinearLayout(context);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		ll.setLayoutParams(params);	
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setGravity(Gravity.CENTER);


		ImageView im = new ImageView(context);		
		im.setImageDrawable(context.getResources().getDrawable(R.drawable.icon));
		//Calculate the size of the banner in pixels

		float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 65, context.getResources().getDisplayMetrics()); 
		//LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int)px,(int)px);
		params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

		im.setLayoutParams(params);	
		im.setPadding(0, 2, 0, 0);
		ll.addView(im);


		TextView tv = new TextView(context);
		if (Build.VERSION.SDK_INT < 11 ) tv.setTextColor(Color.WHITE);
		tv.setText(context.getResources().getString(R.string.rate_text1) + app_name + context.getResources().getString(R.string.rate_text2) );
		px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()); 
		tv.setPadding((int)px, 0, (int)px, 10);
		ll.addView(tv);

		Button b1 = new Button(context);
		b1.setText(context.getResources().getString(R.string.rate_ok) + app_name); //The same text than the title
		b1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + app_package)));
				//Change the value of rate to dont show more times the dialog rate
				AppConfig ap = new AppConfig(context, AppConfig.NAME);
				ap.put(false, AppConfig.RATER);
				dialog.dismiss();

			}
		});

		ll.addView(b1);

		Button b2 = new Button(context);
		b2.setText(context.getResources().getString(R.string.rate_later));
		b2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppConfig ap = new AppConfig(context, AppConfig.NAME);
				try {
					ap.put(ap.getInt(AppConfig.RATER_REMIND_LATER) + 1, AppConfig.RATER_REMIND_LATER);
				} catch (NoPreferenceException e) {
					ap.put(2, AppConfig.RATER_REMIND_LATER);
				}
				dialog.dismiss();
			}
		});
		ll.addView(b2);

		Button b3 = new Button(context);
		b3.setText(context.getResources().getString(R.string.rate_no_thanks));
		b3.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AppConfig ap = new AppConfig(context, AppConfig.NAME);
				ap.put(false, AppConfig.RATER);
				dialog.dismiss();
			}
		});
		ll.addView(b3);
		
		TextView tx2 = new TextView(context);
		tx2.setTextColor(Color.WHITE);
		tx2.setText(" ");

		ll.addView(tx2);

		dialog.setContentView(ll);        
		dialog.show();        
	}



}
