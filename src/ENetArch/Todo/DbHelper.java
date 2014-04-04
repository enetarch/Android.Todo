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

public class DbHelper extends SQLiteOpenHelper
{
	private static final int DATABASE_VERSION = 1;

	// Database Name    
	private static final String DATABASE_NAME = "Todos";
	private TodoTbl todo_tbl = null;
	private TaskTypeTbl tasktype_tbl = null;
	
	public DbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d("onCreate","Entered Function");

		if (todo_tbl == null)
			todo_tbl = new TodoTbl (db);
		
		if (tasktype_tbl == null)
			tasktype_tbl= new TaskTypeTbl (db);
		
		todo_tbl.onCreate();
		tasktype_tbl.onCreate();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV)
	{
		Log.d("onUpgrade todo","Entered Function");

		if (todo_tbl == null)
			todo_tbl = new TodoTbl (db);
		
		if (tasktype_tbl == null)
			tasktype_tbl= new TaskTypeTbl (db);
		
		todo_tbl.onUpgrade (oldV, newV);
		tasktype_tbl.onUpgrade (oldV, newV);
	}

	private void connectTables ()
	{
		Log.d("connectTables","Entered Function");
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		if (todo_tbl == null)
			todo_tbl = new TodoTbl (db);
		
		if (tasktype_tbl == null)
			tasktype_tbl= new TaskTypeTbl (db);
		
	}

	public TaskTypeTbl getTaskType_Table ()
	{ 
		connectTables();
		return (tasktype_tbl); 
	}
	
	public TodoTbl getTodo_Table ()
	{ 
		connectTables();
		return (todo_tbl); 
	}
}
