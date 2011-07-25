package ac.gestureCall.util.shortcut;

import ac.gestureCall.R;
import ac.gestureCall.util.mToast.mToast;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

public class CreateShortcut {
	
	
	public static final void create (Context context,String name){
		
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN); 
		 shortcutIntent.setClassName(context, name);
		 
		 
		 Intent i = new Intent();
		 i.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		 i.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		 i.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Gesture Call");
		 Parcelable iconResource = Intent.ShortcutIconResource.fromContext(context,R.drawable.icon); 
		 i.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,iconResource);
		 context.sendBroadcast(i);
	}
}
