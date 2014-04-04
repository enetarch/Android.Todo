package ENetArch.Todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class TaskTypeTbl 
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


	public TaskTypeTbl (SQLiteDatabase db )
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

	public void addTaskType(TaskType task)
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

	public List<TaskType> getAllTaskTypes()
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

	public void updateTaskType(TaskType task)
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
}
