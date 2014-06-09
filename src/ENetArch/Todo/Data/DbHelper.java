package ENetArch.Todo.Data;

import ENetArch.Todo.Data.TaskTypes_Tbl;
import ENetArch.Todo.Data.WebServers_Tbl;
import ENetArch.Todo.Data.Todos_Tbl;
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
	private static final int DATABASE_VERSION = 3;

	// Database Name    
	private static final String DATABASE_NAME = "Todos";
	private Todos_Tbl todo_tbl = null;
	private TaskTypes_Tbl tasktype_tbl = null;
	private WebServers_Tbl webserver_tbl = null;
	
	public DbHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);		
	}
	
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		Log.d("onCreate","Entered Function");

		if (todo_tbl == null)
			todo_tbl = new Todos_Tbl (db);
		
		if (tasktype_tbl == null)
			tasktype_tbl = new TaskTypes_Tbl (db);
		
		if (webserver_tbl == null)
			webserver_tbl = new WebServers_Tbl (db);

		todo_tbl.onCreate();
		tasktype_tbl.onCreate();
		webserver_tbl.onCreate();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldV, int newV)
	{
		Log.d("onUpgrade todo","Entered Function");

		if (todo_tbl == null)
			todo_tbl = new Todos_Tbl (db);
		
		if (tasktype_tbl == null)
			tasktype_tbl = new TaskTypes_Tbl (db);
		
		if (webserver_tbl == null)
			webserver_tbl = new WebServers_Tbl (db);

		todo_tbl.onUpgrade (oldV, newV);
		tasktype_tbl.onUpgrade (oldV, newV);
		webserver_tbl.onCreate();
	}

	private void connectTables ()
	{
		Log.d("connectTables","Entered Function");
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		if (todo_tbl == null)
			todo_tbl = new Todos_Tbl (db);
		
		if (tasktype_tbl == null)
			tasktype_tbl = new TaskTypes_Tbl (db);
		
		if (webserver_tbl == null)
			webserver_tbl = new WebServers_Tbl (db);

	}

	public TaskTypes_Tbl get_TaskTypes ()
	{ 
		connectTables();
		return (tasktype_tbl); 
	}
	
	public Todos_Tbl get_Todos ()
	{ 
		connectTables();
		return (todo_tbl); 
	}

	public WebServers_Tbl get_WebServers ()
	{ 
		connectTables();
		return (webserver_tbl); 
	}
}
