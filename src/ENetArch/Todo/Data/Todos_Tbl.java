package ENetArch.Todo.Data;

import ENetArch.Todo.BizLogic.Todo;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Todos_Tbl 
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

	public static final int ORDERBY_COMPLETED = 1;
	public static final int ORDERBY_PRIORITY = 2;
	public static final int ORDERBY_TASKTYPE = 3;
	public static final int ORDERBY_MEMO = 4;
	public static final int ORDERBY_TIME = 5;
			  
	public Todos_Tbl (SQLiteDatabase db)
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
		// this.db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODOS);

		// Create tables again        
		// onCreate();
	}

	public void add(Todo todo)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ContentValues values = new ContentValues();

		Calendar calTarget = Calendar.getInstance();
		calTarget.setTime (todo.get_dTarget());
		String szdTarget = sdf.format (calTarget.getTime());
		
		Calendar calUpdated = Calendar.getInstance();
		calUpdated.setTime (todo.get_dUpdated());
		String szdUpdated = sdf.format (calUpdated.getTime());

		Calendar calCompleted = Calendar.getInstance();
		calCompleted.setTime (todo.get_dCompleted());
		String szdCompleted = sdf.format (calCompleted.getTime());

		values.put(FIELD_GUID, todo.get_GUID());
		values.put(FIELD_dUPDATED, szdUpdated);
		
		values.put(FIELD_dTARGET, szdTarget);
		values.put(FIELD_nPRIORITY, todo.get_nPriority());
		values.put(FIELD_nTASKTYPE, todo.get_nTaskType());
		values.put(FIELD_nTIME, todo.get_nTime());
		values.put(FIELD_bCOMPLETED, todo.get_bCompleted());
		values.put(FIELD_dCOMPLETED, szdCompleted);
		values.put(FIELD_szMEMO, todo.get_szMemo());

		Log.d ("addTodo INSERT ", values.toString() );
		// Inserting row
		try
		{ this.db.insert(TABLE_TODOS, null, values); }
		catch (java.lang.IllegalStateException e)
		{ Log.d ("insert", "Something went wrong with SQL INSERT: " + e.toString()); }
	}

	
	public List<Todo> getAll (String szFilter)
	{
		Log.d("getAllTodos ","Entered Function");
		List<Todo> taskList = new ArrayList<Todo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		String szSQL = 
		"SELECT * " +
		"FROM " + TABLE_TODOS + " " +
		( (szFilter != "") ?
			"WHERE " + FIELD_szMEMO +" LIKE '%%"  + szFilter + "%%' " :
			""
		)+
		" ORDER BY " + 
			FIELD_bCOMPLETED + " ASC, " +
			FIELD_dTARGET + " ASC, " +
			FIELD_nTASKTYPE + " ASC, " +
			FIELD_nPRIORITY + " ASC ";
				  
		Log.d  ("szSQL = ", szSQL);
		
		Cursor cursor = this.db.rawQuery(szSQL, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				Log.d ("getTodo", 
						  cursor.getString(cursor.getColumnIndex (FIELD_dTARGET)) +" " +
						  cursor.getString (cursor.getColumnIndex (FIELD_szMEMO))
						  );
				
				Todo task = new Todo();

				task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
				task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));

				task.set_bCompleted (cursor.getInt (cursor.getColumnIndex (FIELD_bCOMPLETED)));
				task.set_nPriority (cursor.getInt (cursor.getColumnIndex (FIELD_nPRIORITY)));
				task.set_nTaskType (cursor.getInt (cursor.getColumnIndex (FIELD_nTASKTYPE)));
				task.set_szMemo (cursor.getString (cursor.getColumnIndex (FIELD_szMEMO)));
				task.set_nTime (cursor.getInt (cursor.getColumnIndex (FIELD_nTIME)));

				try
				{ task.set_dTarget (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dTARGET)))); }
				catch (ParseException e) 
				{}
				
				try
				{ task.set_dCompleted (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dCOMPLETED)))); }
				catch (ParseException e) 
				{}

				try
				{ task.set_dUpdated (sdf.parse (cursor.getString(cursor.getColumnIndex (FIELD_dUPDATED)))); }
				catch (ParseException e) 
				{}


				// Adding contact to list
				taskList.add(task);
			} while (cursor.moveToNext());
		}

		// return task list
		return taskList;
		
	}
	
	public List<Todo> getAll (String szFilter, Date dStart, Date dStop)
	{
		Log.d("getAllTodos ","Entered Function");
		List<Todo> taskList = new ArrayList<Todo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calStart = Calendar.getInstance();
		calStart.setTime (dStart);
		String szdStart = sdf.format(calStart.getTime());
		
		Calendar calStop = Calendar.getInstance();
		calStop.setTime (dStop);
		String szdStop = sdf.format(calStop.getTime());

		String szSQL = 
		"SELECT * " +
		"FROM " + TABLE_TODOS + " " +
		"WHERE " + 
			FIELD_dTARGET +" BETWEEN ( "  + szdStart + ", " + szdStop +" ) " +
			( (szFilter != "") ?
				"AND " + FIELD_szMEMO +" LIKE '%%"  + szFilter + "%%' " :
				""
			)+
		" ORDER BY " + 
			FIELD_bCOMPLETED + " ASC, " +
			FIELD_dTARGET + " ASC, " +
			FIELD_nTASKTYPE + " ASC, " +
			FIELD_nPRIORITY + " ASC ";
				  
		Log.d  ("szSQL = ", szSQL);
		
		Cursor cursor = this.db.rawQuery(szSQL, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				Log.d ("getTodo", 
						  cursor.getString(cursor.getColumnIndex (FIELD_dTARGET)) +" " +
						  cursor.getString (cursor.getColumnIndex (FIELD_szMEMO))
						  );
				
				Todo task = new Todo();

				task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
				task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));

				task.set_bCompleted (cursor.getInt (cursor.getColumnIndex (FIELD_bCOMPLETED)));
				task.set_nPriority (cursor.getInt (cursor.getColumnIndex (FIELD_nPRIORITY)));
				task.set_nTaskType (cursor.getInt (cursor.getColumnIndex (FIELD_nTASKTYPE)));
				task.set_szMemo (cursor.getString (cursor.getColumnIndex (FIELD_szMEMO)));
				task.set_nTime (cursor.getInt (cursor.getColumnIndex (FIELD_nTIME)));

				try
				{ task.set_dTarget (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dTARGET)))); }
				catch (ParseException e) 
				{}
				
				try
				{ task.set_dCompleted (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dCOMPLETED)))); }
				catch (ParseException e) 
				{}

				try
				{ task.set_dUpdated (sdf.parse (cursor.getString(cursor.getColumnIndex (FIELD_dUPDATED)))); }
				catch (ParseException e) 
				{}


				// Adding contact to list
				taskList.add(task);
			} while (cursor.moveToNext());
		}

		// return task list
		return taskList;
		
	}
	
	public List<Todo> getAll (Date dTarget, boolean bShowCompleted, int nOrderBy)
	{
		Log.d("getAllTasks ","Entered Function");		
		List<Todo> taskList = new ArrayList<Todo>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		Calendar calCurrent = Calendar.getInstance();
		String szdCurrent = sdf.format(calCurrent.getTime());
		
		Calendar calTarget = Calendar.getInstance();
		calTarget.setTime (dTarget);
		String szdTarget = sdf.format(calTarget.getTime());
		
		calTarget.add(Calendar.DAY_OF_MONTH, -1);
		String szdPrev = sdf.format(calTarget.getTime());
		
		String szOrderBy = "nPriority";
		switch (nOrderBy)
		{
			case ORDERBY_COMPLETED : szOrderBy = FIELD_bCOMPLETED ; break;
			case ORDERBY_PRIORITY : szOrderBy = FIELD_nPRIORITY; break;
			case ORDERBY_TASKTYPE : szOrderBy = FIELD_nTASKTYPE ; break;
			case ORDERBY_MEMO : szOrderBy = FIELD_szMEMO ; break;
			case ORDERBY_TIME : szOrderBy = FIELD_nTIME ; break;
		}
		
		// Select All QueryString 
		// String selectQuery = "SELECT * FROM " + TABLE_TODOS;
		String szSQL = 
			" SELECT * " +
			" FROM " + TABLE_TODOS + " " +
			" WHERE " +
				( (szdTarget.compareTo (szdCurrent) != 0)  ? 
					FIELD_dTARGET + " > '"  + szdPrev + "' AND " :
					""
				) +
				FIELD_dTARGET + " <= '" + szdTarget + "' " +
				( (! bShowCompleted) ? " AND  " + FIELD_bCOMPLETED + " = 0 " : "" ) +
			" ORDER BY " + 
				  FIELD_bCOMPLETED + " ASC, " +
				  szOrderBy + " ASC ";
		
		Log.d ("Query Dates", "Prev = " + szdPrev + ", Current =  " + szdCurrent + ", Target = " + szdTarget);
				  
		Log.d  ("szSQL = ", szSQL);
		
		Cursor cursor = this.db.rawQuery(szSQL, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst())
		{
			do
			{
				Todo task = new Todo();

				task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
				task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));
				// task.set_dUpdated (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dUPDATED))));
				// task.set_dTarget (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dTARGET))));
				task.set_bCompleted (cursor.getInt (cursor.getColumnIndex (FIELD_bCOMPLETED)));
				// task.set_dCompleted (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dCOMPLETED))));
				task.set_nPriority (cursor.getInt (cursor.getColumnIndex (FIELD_nPRIORITY)));
				task.set_nTaskType (cursor.getInt (cursor.getColumnIndex (FIELD_nTASKTYPE)));
				task.set_szMemo (cursor.getString (cursor.getColumnIndex (FIELD_szMEMO)));
				task.set_nTime (cursor.getInt (cursor.getColumnIndex (FIELD_nTIME)));

				try
				{ task.set_dTarget (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dTARGET)))); }
				catch (ParseException e) 
				{}

				try
				{ task.set_dCompleted (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dCOMPLETED)))); }
				catch (ParseException e) 
				{}

				try
				{ task.set_dUpdated (sdf.parse (cursor.getString(cursor.getColumnIndex (FIELD_dUPDATED)))); }
				catch (ParseException e) 
				{}

				// Adding contact to list
				taskList.add(task);
			} while (cursor.moveToNext());
		}

		// return task list
		return taskList;
	}

	public void update (Todo todo)
	{
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calTarget = Calendar.getInstance();
		calTarget.setTime (todo.get_dTarget());
		String szdTarget = sdf.format(calTarget.getTime());

		Calendar calUpdated = Calendar.getInstance();
		calUpdated.setTime (todo.get_dUpdated());
		String szdUpdated = sdf.format(calUpdated.getTime());

		Calendar calCompleted = Calendar.getInstance();
		calCompleted.setTime (todo.get_dCompleted());
		String szdCompleted = sdf.format(calCompleted.getTime());
		
		// updating row
		ContentValues values = new ContentValues();
		
		values.put(FIELD_dUPDATED, szdUpdated);
		values.put(FIELD_dTARGET, szdTarget);
		values.put(FIELD_nPRIORITY, todo.get_nPriority());
		values.put(FIELD_nTASKTYPE, todo.get_nTaskType());
		values.put(FIELD_nTIME, todo.get_nTime());
		values.put(FIELD_bCOMPLETED, todo.get_bCompleted());
		values.put(FIELD_dCOMPLETED, szdCompleted);
		values.put(FIELD_szMEMO, todo.get_szMemo());
		
		Log.d ("addTodo INSERT ", values.toString() );
		
		this.db.update (TABLE_TODOS, values, FIELD_ID + " = ?", new String[]
		{
			String.valueOf (todo.get_ID())
		});
	}
	
	public Todo get (String szGUID)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
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
				Log.d ("getTodo", 
						  cursor.getString(cursor.getColumnIndex (FIELD_dTARGET)) +" " +
						  cursor.getString (cursor.getColumnIndex (FIELD_szMEMO))
						  );
				
			task.set_ID (cursor.getInt (cursor.getColumnIndex (FIELD_ID)));
			task.set_GUID (cursor.getString (cursor.getColumnIndex (FIELD_GUID)));
			// task.set_dUpdated (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dUPDATED))));
			// task.set_dTarget (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dTARGET))));
			task.set_bCompleted (cursor.getInt (cursor.getColumnIndex (FIELD_bCOMPLETED)));
			// task.set_dCompleted (new Date (cursor.getLong(cursor.getColumnIndex (FIELD_dCOMPLETED))));
			task.set_nPriority (cursor.getInt (cursor.getColumnIndex (FIELD_nPRIORITY)));
			task.set_nTaskType (cursor.getInt (cursor.getColumnIndex (FIELD_nTASKTYPE)));
			task.set_szMemo (cursor.getString (cursor.getColumnIndex (FIELD_szMEMO)));
			task.set_nTime (cursor.getInt (cursor.getColumnIndex (FIELD_nTIME)));

			try
			{ task.set_dTarget (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dTARGET)))); }
			catch (ParseException e) 
			{}
				
			try
			{ task.set_dCompleted (sdf.parse  (cursor.getString(cursor.getColumnIndex (FIELD_dCOMPLETED)))); }
			catch (ParseException e) 
			{}

			try
			{ task.set_dUpdated (sdf.parse (cursor.getString(cursor.getColumnIndex (FIELD_dUPDATED)))); }
			catch (ParseException e) 
			{}
		}

		// return task list
		return (task);
	}
	
	public JSONArray getAlljson ()
	{ 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Todo> todos = this.getAll("");
		
		JSONArray jarray = new JSONArray();
		
		for (Todo item : todos )
		try
		{
			Calendar calUpdated = Calendar.getInstance();
			calUpdated.setTime (item.get_dUpdated());
			String szdUpdated = sdf.format(calUpdated.getTime());

			Calendar calTarget = Calendar.getInstance();
			calTarget.setTime (item.get_dTarget());
			String szdTarget = sdf.format(calTarget.getTime());

			Calendar calCompleted = Calendar.getInstance();
			calCompleted.setTime (item.get_dCompleted());
			String szdCompleted = sdf.format(calCompleted.getTime());
			
			JSONObject json = new JSONObject();
			json.put (FIELD_GUID, item.get_GUID());
			json.put (FIELD_dUPDATED, szdUpdated);
			
			json.put (FIELD_dTARGET, szdTarget);
			json.put (FIELD_bCOMPLETED, item.get_bCompleted());
			json.put (FIELD_dCOMPLETED, szdCompleted);
			json.put (FIELD_nPRIORITY, item.get_nPriority());
			json.put (FIELD_nTASKTYPE, item.get_nTaskType());
			json.put (FIELD_szMEMO, item.get_szMemo());
			json.put (FIELD_nTIME, item.get_nTime());
			
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
			
			Date dTARGET = new Date (json.getString (FIELD_dTARGET));
			String bCOMPLETED = json.getString (FIELD_bCOMPLETED);
			Date dCOMPLETED = new Date (json.getString (FIELD_dCOMPLETED));
			String nPRIORITY = json.getString (FIELD_nPRIORITY);
			String nTASKTYPE = json.getString (FIELD_nTASKTYPE);
			String szMEMO = json.getString (FIELD_szMEMO);
			String nTIME = json.getString (FIELD_nTIME);
			
			Todo todo = this.get (szGUID);
			
			if (todo.get_dUpdated().compareTo(dUpdated) > 0)
			{
				todo.set_dTarget (dTARGET);
				todo.set_bCompleted (Integer.parseInt(bCOMPLETED));
				todo.set_dCompleted (dCOMPLETED);
				todo.set_nPriority (Integer.parseInt(nPRIORITY));
				todo.set_nTaskType (Integer.parseInt(nTASKTYPE));
				todo.set_szMemo (szMEMO);
				todo.set_nTime (Integer.parseInt(nTIME));
				
				todo.set_dUpdated (dUpdated);
				
				update (todo);
			}
		}
		catch (JSONException e)
		{ Log.d ("params.put", e.getMessage() ); }

	}
	
}
