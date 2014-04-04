package ENetArch.Todo;

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

public class TodoTbl 
{
	private SQLiteDatabase db = null;
	
	// tasks table name    
	private static final String TABLE_TODOS = "todos";

	// tasks Table Columns names    
	private static final String FIELD_ID = "nID";

	// used for synchronization	
	private static final String FIELD_GUID = "GUID";
	private static final String FIELD_dUPDATED = "dUpdated";
	
	// data fields
	private static final String FIELD_dTARGET = "dTarget";
	private static final String FIELD_nPRIORITY = "nPriority";
	private static final String FIELD_nTASKTYPE = "nTask";
	private static final String FIELD_nTIME = "nTime";
	private static final String FIELD_bCOMPLETED = "bCompleted";
	private static final String FIELD_dCOMPLETED = "dCompleted";
	private static final String FIELD_szMEMO = "szMemo";

	public TodoTbl (SQLiteDatabase db)
	{ this.db = db; }
	
	public void onCreate()
	{
		Log.d("onCreate todo","Entered Function");
		
		String sql =
			"CREATE TABLE IF NOT EXISTS " + TABLE_TODOS +
			" ( " +
				FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
				FIELD_GUID + " varChar(40), " +
				FIELD_dUPDATED + "  DateTime, " +
				FIELD_dTARGET + "  DateTime, " +
				FIELD_nPRIORITY + " Integer, " +
				FIELD_nTASKTYPE + " Integer, " +
				FIELD_nTIME + " Integer, " +
				FIELD_bCOMPLETED + " VarChar (1), " +
				FIELD_dCOMPLETED  + " DateTime, " +
				FIELD_szMEMO + " varChar(250) " +
			")";
	
		Log.d("szSQL ",sql);
		this.db.execSQL(sql);
		
		String tf = ( (this.db.isOpen() ) ? "True" : "False");
		Log.d("db open ",tf);
		
	}

	public void onUpgrade(int oldV, int newV)
	{
		Log.d("onUpgrade todo","Entered Function");
		
		// Drop older table if existed        
		this.db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);

		// Create tables again        
		onCreate();
	}

	public void addTodo(Todo task)
	{
		ContentValues values = new ContentValues();

		values.put(FIELD_GUID, task.get_GUID());
		values.put(FIELD_dUPDATED, task.get_dUpdated().toString());
		
		values.put(FIELD_dTARGET, task.get_dTarget().toString());
		values.put(FIELD_nPRIORITY, task.get_nPriority());
		values.put(FIELD_nTASKTYPE, task.get_nTaskType());
		values.put(FIELD_nTIME, task.get_nTime());
		values.put(FIELD_bCOMPLETED, task.get_bCompleted());
		values.put(FIELD_dCOMPLETED, task.get_dCompleted().toString());
		values.put(FIELD_szMEMO, task.get_szMemo());

		// Inserting row
		try
		{ this.db.insert(TABLE_TODOS, null, values); }
		catch (java.lang.IllegalStateException e)
		{ Log.d ("insert", "Something went wrong with SQL INSERT: " + e.toString()); }
	}

	public List<Todo> getAllTodos()
	{
		Log.d("getAllTasks ","Entered Function");
		List<Todo> taskList = new ArrayList<Todo>();

		// Select All QueryString 
		String selectQuery = "SELECT * FROM " + TABLE_TODOS;

		Log.d  ("selectQuery = ", selectQuery);
		
		Cursor cursor = this.db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				Todo task = new Todo();

				task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
				task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));
				task.set_dUpdated (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dUPDATED))));
				task.set_dTarget (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dTARGET))));
				task.set_bCompleted (cursor.getInt (cursor.getColumnIndex (FIELD_bCOMPLETED)));
				task.set_dCompleted (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dCOMPLETED))));
				task.set_nPriority (cursor.getInt (cursor.getColumnIndex (FIELD_nPRIORITY)));
				task.set_nTaskType (cursor.getInt (cursor.getColumnIndex (FIELD_nTASKTYPE)));
				task.set_szMemo (cursor.getString (cursor.getColumnIndex (FIELD_szMEMO)));
				task.set_nTime (cursor.getInt (cursor.getColumnIndex (FIELD_nTIME)));

				// Adding contact to list
				taskList.add(task);
			} while (cursor.moveToNext());
		}

		// return task list
		return taskList;
	}

	public void updateTodo(Todo task)
	{
		// updating row
		ContentValues values = new ContentValues();
		
		values.put(FIELD_dUPDATED, task.get_dUpdated().toString());
		values.put(FIELD_dTARGET, task.get_dTarget().toString());
		values.put(FIELD_nPRIORITY, task.get_nPriority());
		values.put(FIELD_nTASKTYPE, task.get_nTaskType());
		values.put(FIELD_nTIME, task.get_nTime());
		values.put(FIELD_bCOMPLETED, task.get_bCompleted());
		values.put(FIELD_dCOMPLETED, task.get_dCompleted().toString());
		values.put(FIELD_szMEMO, task.get_szMemo());
		
		this.db.update (TABLE_TODOS, values, FIELD_ID + " = ?", new String[]
		{
			String.valueOf (task.get_ID())
		});
	}
	
	public Todo getTodo (String szGUID)
	{
		Log.d("getAllTasks ","Entered Function");
		Todo task = new Todo ();

		// Select All QueryString 
		String selectQuery = 
		"SELECT * " +
		"FROM " + TABLE_TODOS + " " +
		"WHERE " +
			FIELD_GUID + " LIKE '" + szGUID + "' ";

		Log.d  ("selectQuery = ", selectQuery);

		Cursor cursor = this.db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
			task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));
			task.set_dUpdated (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dUPDATED))));
			task.set_dTarget (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dTARGET))));
			task.set_bCompleted (cursor.getInt (cursor.getColumnIndex (FIELD_bCOMPLETED)));
			task.set_dCompleted (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dCOMPLETED))));
			task.set_nPriority (cursor.getInt (cursor.getColumnIndex (FIELD_nPRIORITY)));
			task.set_nTaskType (cursor.getInt (cursor.getColumnIndex (FIELD_nTASKTYPE)));
			task.set_szMemo (cursor.getString (cursor.getColumnIndex (FIELD_szMEMO)));
			task.set_nTime (cursor.getInt (cursor.getColumnIndex (FIELD_nTIME)));
		}

		// return task list
		return (task);
	}
}
