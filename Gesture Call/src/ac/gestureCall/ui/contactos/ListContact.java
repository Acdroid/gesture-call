/**
 *  Poner licencia
 * @author Carlos Diaz Canovas y Marcos Trujillo Seoane
 * 
 */
package ac.gestureCall.ui.contactos;


import ac.gestureCall.R;
import ac.gestureCall.ui.main;
import ac.gestureCall.ui.creadorGestos.CreadorGestos;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.gesture.GestureLibrary;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public final class ListContact extends ListActivity
{
	public static final int ID = 2;
	public static final String KEY_NAME ="NAME";
	public static final String KEY_PHONE ="PHONE";
	public GestureLibrary store;

	public Cursor cursor;
	/**
	 * Called when the activity is first created. Responsible for initializing the UI.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lista_contactos);     
		
		store = main.getStore();
		
		cursor = getContacts();
		startManagingCursor(cursor);
		String[] fields = new String[] {
				Data.DISPLAY_NAME,
		};

        int[] to = new int[] { R.id.item_lista_nombre};

		SimpleCursorAdapter adapter = new mySimpleCursorAdapter(this, R.layout.item_lista_contactos, cursor,
				fields, to);

		setListAdapter(adapter);
	}



	/**
	 * Obtains the contact list for the currently selected account.
	 *
	 * @return A cursor for for accessing the contact list.
	 */
	private Cursor getContacts()
	{
        // Run query
//        Uri uri = ContactsContract.Contacts.CONTENT_URI;
//        String[] projection = new String[] {
//                ContactsContract.Contacts._ID,
//                ContactsContract.Contacts.DISPLAY_NAME
//        };
//        String selection = ContactsContract.Contacts.IN_VISIBLE_GROUP + " = '1'";
//        
//        String[] selectionArgs = null;
//        String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";

		
		Uri uri =  Data.CONTENT_URI;
		String[] projection = new String []{
				Data._ID,
				Data.DISPLAY_NAME,
				Phone.NUMBER,
				Phone.TYPE 
		};
		String selection = Data.MIMETYPE + "='" + Phone.CONTENT_ITEM_TYPE + "' AND "
        		+ Phone.NUMBER + " IS NOT NULL";
		
		String[] selectionArgs = null;
		String sortOrder = Data.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        return managedQuery(uri, projection, selection, selectionArgs, sortOrder);
    }



	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Toast.makeText(this, "click", Toast.LENGTH_SHORT).show();
		Intent i = new Intent(ListContact.this,CreadorGestos.class);
		String nombre = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
		String phone= cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
		
		Log.d("DEBUG","phone " + phone);
		
		i.putExtra(KEY_NAME, nombre);
		i.putExtra(KEY_PHONE, phone);
		startActivityForResult(i, ID);
	}
	
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (resultCode == RESULT_OK) {
        	setResult(main.RESULT_OK);
            ListContact.this.finish();
        }
        
    }
	
	
	private class mySimpleCursorAdapter extends SimpleCursorAdapter{

		private Context mContext;

		public mySimpleCursorAdapter(Context context, int layout, Cursor c,
				String[] from, int[] to) {
			super(context, layout, c, from, to);

			this.mContext = context;
		}

		@Override
		public void bindView(View view, Context context, Cursor cursor) {

			//Cambiamos el color de fondo
			LinearLayout l = (LinearLayout) view.findViewById(R.id.lay_item);
			TextView t = (TextView) view.findViewById(R.id.item_lista_nombre);
			if (cursor.getPosition() % 2 == 0){
				l.setBackgroundResource(android.R.color.background_dark);
				t.setTextColor(mContext.getResources().getColor(android.R.color.white));
			}
			else{
				l.setBackgroundResource(android.R.color.background_light);
				t.setTextColor(mContext.getResources().getColor(android.R.color.background_dark));
			}
			super.bindView(view, context, cursor);
			
			//Comprobamos si hay gesto o no
			String contacto =t.getText().toString();
			Log.d("DEBUG","asdf " + contacto);
			if (store.getGestures(contacto) != null){
				ImageView i = (ImageView)view.findViewById(R.id.item_list_image);
				i.setImageResource(R.drawable.btn_check_on);
				
				
			}
		}
	}
	

}
