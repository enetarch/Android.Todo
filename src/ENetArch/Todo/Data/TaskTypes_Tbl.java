package ENetArch.Todo.Data;

import ENetArch.Todo.BizLogic.TaskType;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;


public class TaskTypes_Tbl 
{
	SQLiteDatabase db = null;
	
	// tasks table name    
	private static final String TABLE_TASKS = "tasktypes";

	// tasks Table Columns names    
	private static final String FIELD_ID = "nID";

	// used for synchronization	
	private static final String FIELD_GUID = "GUID";
	private static final String FIELD_dUPDATED = "dUpdated";
	
	// data fields
	
	private static final String FIELD_szTYPE = "szType";


	public TaskTypes_Tbl (SQLiteDatabase db )
	{ this.db = db; }
	
	public void onCreate()
	{
		Log.d("onCreate tasktypes ","Entered Function");
		
		String sql =
			"CREATE TABLE IF NOT EXISTS " + TABLE_TASKS +
			" ( " +
				FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FIELD_GUID + " varChar(40), " +
				FIELD_dUPDATED + " DateTime, " +
				FIELD_szTYPE + " varChar(40) " +
			")";
			
		Log.d("szSQL ",sql);
		this.db.execSQL(sql);
		
		sql = 
			"INSERT INTO " + TABLE_TASKS +
			" ( " + FIELD_GUID + ", " + FIELD_dUPDATED + ", " + FIELD_szTYPE +" )" +
			" VALUES " + 
			" ( '___', '2014-04-04', 'Do' ), " +
			" ( '___', '2014-04-04', 'Get' ), " +
			" ( '___', '2014-04-04', 'Locate' ), " +
			" ( '___', '2014-04-04', 'Ask' ), " +
			" ( '___', '2014-04-04', 'Call' ), " +
			" ( '___', '2014-04-04', 'Tell' ), " +
			" ( '___', '2014-04-04', 'Consult' ), " +
			" ( '___', '2014-04-04', 'Create' ), " +
			" ( '___', '2014-04-04', 'Schedule' ), " +
			" ( '___', '2014-04-04', 'Meet' ), " +
			" ( '___', '2014-04-04', 'Install' ), " +
			" ( '___', '2014-04-04', 'Expect' ), " +
			" ( '___', '2014-04-04', 'Goto' ), " +
			" ( '___', '2014-04-04', 'Sync' ), " +
			" ( '___', '2014-04-04', 'Monthly' ), " +
			" ( '___', '2014-04-04', 'Quote' ), " +
			" ( '___', '2014-04-04', 'Invoice' ), " +
			" ( '___', '2014-04-04', 'WO' ), " +
			" ( '___', '2014-04-04', 'Order' ), " +
			" ( '___', '2014-04-04', 'Mail' ), " +
			" ( '___', '2014-04-04', 'Complete' ), " +
			" ( '___', '2014-04-04', 'Fix' ), " +
			" ( '___', '2014-04-04', 'Load' ), " +
			" ( '___', '2014-04-04', 'Add' ), " +
			" ( '___', '2014-04-04', 'Take' ), " +
			" ( '___', '2014-04-04', 'Send' ), " +
			" ( '___', '2014-04-04', 'File' ), " +
			" ( '___', '2014-04-04', 'Pickup' ), " +
			" ( '___', '2014-04-04', 'Deliver' ), " +
			" ( '___', '2014-04-04', 'Make' ), " +
			" ( '___', '2014-04-04', 'Review' ), " +
			" ( '___', '2014-04-04', 'Update' ), " +
			" ( '___', '2014-04-04', 'Change' ), " +
			" ( '___', '2014-04-04', 'Remind' ), " +
			" ( '___', '2014-04-04', 'Work On' ), " +
			" ( '___', '2014-04-04', 'Budget' ) ";

		Log.d("szSQL ",sql);
		this.db.execSQL(sql);

		
		String tf = ( (this.db.isOpen() ) ? "True" : "False");
		Log.d("db open ",tf);
	}

	public void onUpgrade(int oldV, int newV)
	{
		Log.d("onUpgrade tasktypes","Entered Function");
		
		// Drop older table if existed        
		this.db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);

		// Create tables again        
		onCreate();
	}

	public void add(TaskType task)
	{
		ContentValues values = new ContentValues();
		values.put(FIELD_GUID, task.get_GUID());
		values.put(FIELD_dUPDATED, task.get_dUpdated().toString());
		
		values.put(FIELD_szTYPE, task.get_szType());

		// Inserting row
		try 
		{ this.db.insert(TABLE_TASKS, null, values); }
		catch (java.lang.IllegalStateException e)
		{ Log.d ("insert", "Something went wrong with SQL INSERT: " + e.toString()); }
	}

	public TaskType get(String szGUID)
	{
		Log.d("get ","Entered Function");
		

		// Select All QueryString 
		String selectQuery = 
		"SELECT * " +
		"FROM " + TABLE_TASKS + " " +
		"WHERE " +
			FIELD_GUID + " LIKE '" + szGUID + "' ";

		Log.d  ("selectQuery = ", selectQuery);
		
		Cursor cursor = this.db.rawQuery(selectQuery, null);
		
		TaskType task = new TaskType();
		
		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{			
			task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
			task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));
			task.set_dUpdated (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dUPDATED))));
			task.set_szType (cursor.getString (cursor.getColumnIndex (FIELD_szTYPE)));
		}

		// return task list
		return task;
	}

	public List<TaskType> getAll()
	{
		Log.d("getAllTasks ","Entered Function");
		List<TaskType> taskList = new ArrayList<TaskType>();

		// Select All QueryString 
		String selectQuery = "SELECT * FROM " + TABLE_TASKS;

		Log.d  ("selectQuery = ", selectQuery);
		
		Cursor cursor = this.db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				TaskType task = new TaskType();
				task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
				task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));
				task.set_dUpdated (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dUPDATED))));
				task.set_szType (cursor.getString (cursor.getColumnIndex (FIELD_szTYPE)));
				
				// Adding task type to list
				taskList.add(task);
			} while (cursor.moveToNext());
		}

		// return task list
		return taskList;
	}

	public void update(TaskType task)
	{
		// updating row
		ContentValues values = new ContentValues();
		values.put (FIELD_szTYPE, task.get_szType());
		values.put (FIELD_dUPDATED, task.get_dUpdated().toString());
		
		this.db.update (TABLE_TASKS, values, FIELD_ID + " = ?", new String[]
		{
			String.valueOf (task.get_ID())
		});
	}
	
	public JSONArray getAlljson ()
	{ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		List<TaskType> tasktypes = this.getAll();
		
		JSONArray jarray = new JSONArray();
		
		for (TaskType item : tasktypes )
		try
		{
			Calendar calUpdated = Calendar.getInstance();
			calUpdated.setTime (item.get_dUpdated());
			String szdUpdated = sdf.format(calUpdated.getTime());

			JSONObject json = new JSONObject();
			json.put (FIELD_GUID, item.get_GUID());
			json.put (FIELD_dUPDATED, szdUpdated);
			json.put (FIELD_szTYPE, item.get_szType());
			
			jarray.put (json);
		}
		catch (JSONException e)
		{ Log.d ("params.put", e.getMessage() ); }

		return (jarray);
	}
	
	public void sync (JSONArray jarray)
	{
		int t = 0;
		for (t=0; t<jarray.length(); t++)
		try
		{
			JSONObject json = jarray.getJSONObject(t);
			
			String szGUID = json.getString (FIELD_GUID);
			Date dUpdated = new Date (json.getString (FIELD_dUPDATED));
			String szType = json.getString (FIELD_szTYPE);
			
			TaskType tasktype = this.get (szGUID);
			
			if (tasktype.get_dUpdated().compareTo(dUpdated) > 0)
			{
				tasktype.set_szType (szType);
				tasktype.set_dUpdated (dUpdated);
				
				update (tasktype);
			}
		}
		catch (JSONException e)
		{ Log.d ("params.put", e.getMessage() ); }

	}
}
