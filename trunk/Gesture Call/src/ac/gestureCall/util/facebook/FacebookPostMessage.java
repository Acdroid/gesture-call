package ac.gestureCall.util.facebook;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;


public class FacebookPostMessage implements DialogListener{
	private Facebook facebookClient;
	private Activity activity;
	private String post="";
	private String link="";
	private String image="";
	private String nameApp ="";
	private Boolean posteado = false;

	public FacebookPostMessage (Activity act,String name, String mes, String l,String i){
		activity = act;
		nameApp = name;
		post = mes;
		link = l;
		image = i;

		facebookClient = new Facebook("145501312222682");
		String [] permisos = new String[] {"publish_stream", "publish_actions"};
		facebookClient.authorize(activity, permisos, this);
		Log.d("Gesture Call","Terminada petición autorizacion");

		//Try to post the message. if cant, when autorize the app we will try other time
		try
		{
			if (!posteado){
				posteado = true;
				Bundle parameters = new Bundle();
				parameters.putString("message", post);// the message to post to the wall

				if (!link.equals("") && (link != null))
					parameters.putString("link",link);

				if (!image.equals("") && (image != null))
					parameters.putString("picture", image);

				if (!nameApp.equals("") && (nameApp!= null))
					parameters.putString("name", nameApp);    

				facebookClient.dialog(activity, "stream.publish", parameters, this);
			}
		}
		catch (Exception e)
		{
			Log.e("Gesture Call",e.getMessage());
		}

		Log.d("Gesture Call","Escrito comentario");
	}

	@Override
	public void onCancel() {
		Log.i("Gesture Call","Autorizacion de facebook cancelada.\n");

	}

	@Override
	public void onComplete(Bundle values) {
		Log.i("Gesture Call","Autorización de facebook completada.");
		if (values.isEmpty())
		{
			//"skip" clicked ?
			return;
		}

		// if facebookClient.authorize(...) was successful, this runs
		// this also runs after successful post
		// after posting, "post_id" is added to the values bundle
		// I use that to differentiate between a call from
		// faceBook.authorize(...) and a call from a successful post
		// is there a better way of doing this?
		if (!values.containsKey("post_id"))
		{
			try{
				if (!posteado){
					posteado = true;
					Bundle parameters = new Bundle();
					parameters.putString("message", post);// the message to post to the wall

					if (!link.equals("") && (link != null))
						parameters.putString("link",link);

					if (!image.equals("") && (image != null))
						parameters.putString("picture", image);

					if (!nameApp.equals("") && (nameApp!= null))
						parameters.putString("name", nameApp);    

					facebookClient.dialog(activity, "stream.publish", parameters, this);
				}
			}
			catch (Exception e)
			{
				Log.e("Gesture Call",e.getMessage());
			}
		}

	}

	@Override
	public void onError(DialogError arg0) {
		Log.e("Gesture Call","Error al intentar autorizar facebook.\n" + arg0.toString());

	}

	@Override
	public void onFacebookError(FacebookError arg0) {
		Log.e("Gesture Call","Error al intentar autorizar facebook, ERORR EN FACEBOOK.\n" + arg0.toString() + " " + arg0.getMessage());

	}


}
