package ENetArch.Todo.Data;

import ENetArch.Todo.BizLogic.WebServer;
import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import android.util.Log;
import java.util.Date;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.text.ParseException;


public class WebServers_Tbl 
{
	private SQLiteDatabase db = null;
	
	// tasks table name    
	private static final String TABLE_WEBSERVERS = "webservers";

	// tasks Table Columns names    
	private static final String FIELD_ID = "nID";

	// used for synchronization	
	private static final String FIELD_GUID = "GUID";
	private static final String FIELD_dUPDATED = "dUpdated";
	
	// data fields
	private static final String FIELD_szName = "szName";
	private static final String FIELD_szURL = "szURL";
	private static final String FIELD_szUID = "szUID";
	private static final String FIELD_szPSW = "szPSW";

	public WebServers_Tbl (SQLiteDatabase db)
	{ this.db = db; }
	
	public void onCreate()
	{
		Log.d("onCreate","WebServer_Tbl - Entered Function");
		
		String sql =
			"CREATE TABLE IF NOT EXISTS " + TABLE_WEBSERVERS +
			" ( " +
				FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FIELD_GUID + " varChar(40), " +
				FIELD_dUPDATED + "  DateTime, " +
				FIELD_szName + "  varChar(40), " +
				FIELD_szURL + " varChar(250), " +
				FIELD_szUID + " varChar(250), " +
				FIELD_szPSW + " varChar(250) " +
			")";
	
		Log.d("szSQL ",sql);
		this.db.execSQL(sql);
		
		String tf = ( (this.db.isOpen() ) ? "True" : "False");
		Log.d("db open ",tf);
		
	}

	public void onUpgrade (int oldV, int newV)
	{
		Log.d("onUpgrade","WebServer_Tbl - Entered Function");
	}

	public void add (WebServer webserver)
	{
		Log.d ("add","WebServer_Tbl - Entered Function");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues values = new ContentValues();

		Calendar calUpdated = Calendar.getInstance();
		calUpdated.setTime (webserver.get_dUpdated());
		String szdUpdated = sdf.format (calUpdated.getTime());

		values.put(FIELD_GUID, webserver.get_GUID());
		values.put(FIELD_dUPDATED, szdUpdated);
		
		values.put(FIELD_szName, webserver.get_szName());
		values.put(FIELD_szURL, webserver.get_szURL());
		values.put(FIELD_szUID, webserver.get_szUID());
		values.put(FIELD_szPSW, webserver.get_szPSW());

		Log.d ("addTodo INSERT ", values.toString() );
		// Inserting row
		try
		{ this.db.insert(TABLE_WEBSERVERS, null, values); }
		catch (java.lang.IllegalStateException e)
		{ Log.d ("insert", "Something went wrong with SQL INSERT: " + e.toString()); }
	}

	public void delete (String szGUID)
	{
		Log.d ("DELETE","WebServer_Tbl - Entered Function");
		
		this.db.delete (TABLE_WEBSERVERS, FIELD_GUID + " = ?", 
		new String[]
		{
			szGUID
		});
	}
	
	public List<WebServer> getAll ()
	{
		Log.d("getAll ","WebServer_Tbl - Entered Function");
		
		List<WebServer> list = new ArrayList<WebServer>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String szSQL = 
		"SELECT * " +
		"FROM " + TABLE_WEBSERVERS ;
				  
		Log.d  ("szSQL = ", szSQL);
		
		Cursor cursor = this.db.rawQuery(szSQL, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				Log.d ("getWebServer", cursor.getString(cursor.getColumnIndex (FIELD_szName)) );
				
				WebServer webserver = new WebServer();

				webserver.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
				webserver.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));

				webserver.set_szName (cursor.getString (cursor.getColumnIndex (FIELD_szName)));
				webserver.set_szURL (cursor.getString (cursor.getColumnIndex (FIELD_szURL)));
				webserver.set_szUID (cursor.getString (cursor.getColumnIndex (FIELD_szUID)));
				webserver.set_szPSW (cursor.getString (cursor.getColumnIndex (FIELD_szPSW)));

				// Adding contact to list
				list.add (webserver);
			} while (cursor.moveToNext());
		}

		// return task list
		return list;
		
	}

	public void update (WebServer webserver)
	{
		Log.d ("update","WebServer_Tbl - Entered Function");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calUpdated = Calendar.getInstance();
		calUpdated.setTime (webserver.get_dUpdated());
		String szdUpdated = sdf.format(calUpdated.getTime());

		// updating row
		ContentValues values = new ContentValues();
		
		values.put(FIELD_dUPDATED, szdUpdated);
		
		values.put(FIELD_szName, webserver.get_szName());
		values.put(FIELD_szURL, webserver.get_szURL());
		values.put(FIELD_szUID, webserver.get_szUID());
		values.put(FIELD_szPSW, webserver.get_szPSW());
		
		Log.d ("addTodo INSERT ", values.toString() );
		
		this.db.update (TABLE_WEBSERVERS, values, FIELD_ID + " = ?", new String[]
		{
			String.valueOf (webserver.get_ID())
		});
	}
	
	public WebServer get (String szGUID)
	{
		Log.d ("get","WebServer_Tbl - Entered Function");
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
		WebServer webserver = new WebServer();

		// Select All QueryString 
		String selectQuery = 
		"SELECT * " +
		"FROM " + TABLE_WEBSERVERS + " " +
		"WHERE " +
			FIELD_GUID + " LIKE '" + szGUID + "' ";

		Log.d  ("selectQuery = ", selectQuery);

		Cursor cursor = this.db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			Log.d ("getWebServer", cursor.getString(cursor.getColumnIndex (FIELD_szName)) );

			webserver.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
			webserver.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));

			webserver.set_szName (cursor.getString (cursor.getColumnIndex (FIELD_szName)));
			webserver.set_szURL (cursor.getString (cursor.getColumnIndex (FIELD_szURL)));
			webserver.set_szUID (cursor.getString (cursor.getColumnIndex (FIELD_szUID)));
			webserver.set_szPSW (cursor.getString (cursor.getColumnIndex (FIELD_szPSW)));
		}

		// return task list
		return (webserver);
	}
}
