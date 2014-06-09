package ENetArch.Todo.Presentation;

import ENetArch.Todo.Data.DbHelper;
import ENetArch.Todo.Presentation.Calendar_Activity;
import ENetArch.Todo.R;
import ENetArch.Todo.BizLogic.TaskType;
import ENetArch.Todo.BizLogic.Todo;
import ENetArch.Todo.Data.Todos_Tbl;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.widget.PopupMenu;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.text.ParseException;

public class Todo_Activity extends Activity
// public class Todo_Activity extends ActionBarActivity
{
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	List<Todo> todoList;
	List<TaskType> tasktypeList;
	MyAdapter adapt;
	Todo_Activity thsActivity = null;
	protected Spinner lbxTaskType = null;

	protected Date dTarget = new Date ();
	protected boolean bCompleted = false;
	protected int nOrderBy = Todos_Tbl.ORDERBY_PRIORITY;
	// protected Array aryView = {};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		thsActivity = this;
		Context context = getApplicationContext();
		
		// Toast.makeText(context, "starting the app", Toast.LENGTH_LONG).show();
		
		super.onCreate(savedInstanceState);
		// Toast.makeText(context, "created app", Toast.LENGTH_LONG).show();
		
		setContentView (R.layout.todos_activity);
		// Toast.makeText(context, "setting content view", Toast.LENGTH_LONG).show();
		
		ActionBar actionBar = getActionBar();
		actionBar.show();

		// ==========================================
		
		Log.d("create instance of DB Helper ","");
		
		db = new DbHelper(this);
		// Toast.makeText(context, "connecting to DB", Toast.LENGTH_LONG).show();
		
		Log.d("DB Helper created ","");
		
		cmdRefresh_click ();
	}

	public void cmdRefresh_click ()
	{
		// todoList = db.get_Todos().getAll();
		todoList = db.get_Todos().getAll(dTarget, bCompleted, nOrderBy);
		tasktypeList = db.get_TaskTypes().getAll();
		// Toast.makeText(context, "getting tasks", Toast.LENGTH_LONG).show();
		
		// =========================================
		
		lbxTaskType = (Spinner) this.findViewById (R.id.lbxTaskType);
		List<String> aryTaskTypes = new ArrayList<String>();
		
		int t=0;
		for(t=0; t<tasktypeList.size(); t++ )
			aryTaskTypes.add(tasktypeList.get(t).get_szType());
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aryTaskTypes); 
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		lbxTaskType.setAdapter(adapter2);
		
		// =========================================
		
		adapt = new MyAdapter(this, R.layout.todos_list, todoList);
		// Toast.makeText(context, "got the task view adapter", Toast.LENGTH_LONG).show();
		
		// ListView listTask = (ListView) findViewById(R.id.listView1);
		ListView listTask = (ListView) findViewById(R.id.listView1);
		// Toast.makeText(context, "getting the todoList view adapter", Toast.LENGTH_LONG).show();
		
		listTask.setAdapter(adapt);
		// Toast.makeText(context, "connecting the task todoList view to the adapter", Toast.LENGTH_LONG).show();		
	}
	
	public void addTodo_click(View v)
	{
		EditText txtMemo = (EditText) findViewById(R.id.txtTodo);
		String szMemo = txtMemo.getText().toString();

		if (szMemo.equalsIgnoreCase(""))
		{
			Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG).show();
		} else
		{
			Todo todo = new Todo();
			
			todo.set_szMemo (szMemo);
			todo.set_dTarget (dTarget);
			
			lbxTaskType = (Spinner) this.findViewById (R.id.lbxTaskType);
			if (lbxTaskType != null)
				todo.set_nTaskType ((int) lbxTaskType.getSelectedItemId() +1);

			db.get_Todos().add(todo);
			Log.d("tasker", "dataadded");

			txtMemo.setText("");
			
			adapt.add(todo);
			adapt.notifyDataSetChanged();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.        
		getMenuInflater().inflate(R.menu.todos_menu, menu);
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu (Menu menu) 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Calendar calCurrent = Calendar.getInstance();
		String szdCurrent = sdf.format(calCurrent.getTime());
		
		Calendar calTarget = Calendar.getInstance();
		calTarget.setTime (dTarget);
		String szdTarget = sdf.format(calTarget.getTime());

		boolean bShow = szdTarget.compareTo (szdCurrent) != 0;
		menu.findItem(R.id.action_now).setVisible(bShow);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		View v = thsActivity.getCurrentFocus();
		
		// Handle item selection    
		switch (item.getItemId()) 
		{
			case R.id.action_refresh: cmdRefresh_click(); return true;
			case R.id.action_tasktypes: cmdShowTaskTypes(); return true;
			case R.id.action_settings: cmdShowSettings(v); return true;
			case R.id.action_webservers: cmdShowWebServers(v); return true;
			case R.id.action_now: cmdNow(v); return true;
			case R.id.action_calendar: cmdShowCalendar(); return true;
			case R.id.action_sortby: cmdShowSortBy (v); return true;
			case R.id.action_view: cmdShowView (v); return true;
			case R.id.action_find: cmdShowFind (v); return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	// =====================================
	
	public void cmdShowWebServers (View v) 
	{
		Intent intent = new Intent (thsActivity, Settings_WebServers_Activity.class);
		Log.d("intent", "new ReportActivity");

		startActivity (intent);
		Log.d("intent", "ReportActivity started");
	}
	
	// =====================================
	
	public void cmdShowSettings (View v) 
	{
		// Intent intent = new Intent (thsActivity, Settings_Application_Activity.class);
		Intent intent = new Intent (thsActivity, Settings_Groups_Activity.class);
		// Intent intent = new Intent (thsActivity, Settings_WebServers_Activity.class);
		// Intent intent = new Intent (thsActivity, Settings_Activity.class);
		Log.d("intent", "new ReportActivity");

		startActivity (intent);
		Log.d("intent", "ReportActivity started");
	}
	
	// =====================================
	
	public void cmdShowFind (View v) 
	{
		Intent intent = new Intent (thsActivity, Report_Activity.class);
		Log.d("intent", "new ReportActivity");

		startActivity (intent);
		Log.d("intent", "ReportActivity started");
	}
	
	// =====================================
	
	public void cmdShowCalendar () 
	{
		Intent intent = new Intent (thsActivity, Calendar_Activity.class);
		Log.d("intent", "new CalendarActivity");

		Calendar calTarget = Calendar.getInstance();	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		calTarget.setTime (dTarget);
		String szdTarget = sdf.format(dTarget.getTime());

		intent.putExtra (Calendar_Activity.SET_DATE_TARGET, szdTarget);
		Log.d("intent", "TodoDetailActivity message sent");
		
		startActivityForResult (intent, Calendar_Activity.nGET_DATE_TARGET);
		Log.d("intent", "CalendarActivity started");
	}

	// =====================================
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{     
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) 
		{ 
			case (Calendar_Activity.nGET_DATE_TARGET) : 
			{ 
				if (resultCode == Activity.RESULT_OK) 
				{ 
					String szdTarget = data.getStringExtra (Calendar_Activity.SET_DATE_TARGET);
					// TODO Switch tabs using the index.
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					try 
					{ dTarget = sdf.parse (szdTarget); }
					catch (ParseException e) {}
				} 
				
			} break; 
		} 
		
		invalidateOptionsMenu();
		cmdRefresh_click();
	}	
	
	// =====================================

	public void cmdNow (View v)
	{	
		dTarget = new Date(); 
		invalidateOptionsMenu();
		cmdRefresh_click();
	}
	
	public void cmdShowSortBy (View v)
	{
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () 
		{
			public boolean onMenuItemClick (MenuItem item)
			{ return (thsActivity.onSortByItem_click (item)); }
		} );
		
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.sortby_context_menu, popup.getMenu());
		
		popup.show();	
	}
	
	public void cmdShowView (View v)
	{
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () 
		{
			public boolean onMenuItemClick (MenuItem item)
			{ return (thsActivity.onViewItem_click (item)); }
		} );
		
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.view_context_menu, popup.getMenu());
		
		popup.show();	

		MenuItem cbxCompleted = (MenuItem) popup.getMenu().findItem (R.id.action_view_completed);
		cbxCompleted.setChecked (bCompleted);		
	}
	
	public void cmdShowTaskTypes () 
	{
		Intent intent = new Intent (thsActivity, TaskType_Activity.class);
		Log.d("intent", "new TaskTypeActivity");

		startActivity (intent);
		Log.d("intent", "TaskTypeActivity started");
	}
	
	// =========================================================
	
	public boolean onSortByItem_click(MenuItem item) 
	{
		 switch (item.getItemId()) 
		 {
			case R.id.action_sortby_completed: cmdSortBy_Completed(); break;
			case R.id.action_sortby_priority: cmdSortBy_Priority(); break;
			case R.id.action_sortby_tasktype: cmdSortBy_TaskType(); break;
			case R.id.action_sortby_memo: cmdSortBy_Memo(); break;
			case R.id.action_sortby_time: cmdSortBy_Time(); break;
			default: return super.onContextItemSelected(item);
		 }
		 
		 cmdRefresh_click ();
		 return true;
	}

	public void cmdSortBy_Completed () { nOrderBy = Todos_Tbl.ORDERBY_COMPLETED; }
	public void cmdSortBy_Priority () { nOrderBy = Todos_Tbl.ORDERBY_PRIORITY; }
	public void cmdSortBy_TaskType () { nOrderBy = Todos_Tbl.ORDERBY_TASKTYPE; }
	public void cmdSortBy_Memo () { nOrderBy = Todos_Tbl.ORDERBY_MEMO; }
	public void cmdSortBy_Time () { nOrderBy = Todos_Tbl.ORDERBY_TIME; }
	
	// =========================================================
	
	public boolean onViewItem_click(MenuItem item) 
	{
		 switch (item.getItemId()) 
		 {
			case R.id.action_view_completed: cmdView_Completed(); break;
			case R.id.action_view_priority: cmdView_Priority(); break;
			case R.id.action_view_tasktype: cmdView_TaskType(); break;
			case R.id.action_view_memo: cmdView_Memo(); break;
			case R.id.action_view_time: cmdView_Time(); break;
			default: return super.onContextItemSelected(item);
		 }
		 
		 cmdRefresh_click ();
		 return true;
	}

	public void cmdView_Completed () { bCompleted = !bCompleted; cmdRefresh_click (); }
	public void cmdView_Priority () {}
	public void cmdView_TaskType () {}
	public void cmdView_Memo () {}
	public void cmdView_Time () {}
	
	// =========================================================
	
	private class MyAdapter extends ArrayAdapter<Todo>
	{
		Context context;
		List<Todo> todoList = new ArrayList<Todo>();
		int layoutResourceId;

		public MyAdapter(Context context, int layoutResourceId, List<Todo> objects)
		{
			super(context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.todoList = objects;
			this.context = context;
		}

		@Override
		public View getView (int position, View convertView, ViewGroup parent)
		{
			Log.d ("getView position", String.valueOf (position) );
			
			CheckBox cbxCompleted = null;
			TextView lblPriority = null;
			TextView lblTaskType = null;
			TextView lblMemo = null;
			TextView lblTime = null;
			
			Todo current = todoList.get (position);

			LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService 
				(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate 
				(R.layout.todos_list, parent, false);

			cbxCompleted = (CheckBox) convertView.findViewById (R.id.chkStatus);
			if (cbxCompleted != null)
			{
				// chk.setText (current.get_szMemo());
				cbxCompleted.setChecked (current.get_bCompleted() == 1 ? true : false);
				cbxCompleted.setTag (current);

				convertView.setTag (cbxCompleted);
				cbxCompleted.setOnClickListener (new View.OnClickListener()
				{
					@Override
					public void onClick (View v)
					{
						CheckBox cb = (CheckBox) v;
						Todo changeTask = (Todo) cb.getTag ();

						changeTask.set_bCompleted (cb.isChecked() == true ? 1 : 0);
						db.get_Todos().update (changeTask);

					}
				});
			}
			
			lblPriority = (TextView) convertView.findViewById (R.id.lblPriority);
			if (lblPriority != null)
				lblPriority.setText (String.valueOf(current.get_nPriority()));
			
			lblMemo = (TextView) convertView.findViewById (R.id.lblMemo);
			if (lblMemo != null)
			{
				String szTT = "";
				int nTT = current.get_nTaskType();
				if ((nTT > 0) && (nTT < tasktypeList.size()))
					szTT = tasktypeList.get (nTT-1).get_szType() + ": ";
				
				szTT += current.get_szMemo();
				
				lblMemo.setText (szTT);
				lblMemo.setTag (current);

				convertView.setTag (lblMemo);
				lblMemo.setOnClickListener (new View.OnClickListener()
				{
					@Override
					public void onClick (View v)
					{
						TextView cb = (TextView) v;
						Todo changeTask = (Todo) cb.getTag ();

						Log.d("intent", "start TodoDetailActivity ==");
						Log.d("intent", "thsActivity == " + ((thsActivity == null) ? "true" : "false"));

						Intent intent = new Intent (thsActivity, TodoDetail_Activity.class);
						Log.d("intent", "new TodoDetailActivity");

						intent.putExtra (TodoDetail_Activity.SET_TODO, changeTask.get_GUID());
						Log.d("intent", "TodoDetailActivity message sent");

						startActivity (intent);
						Log.d("intent", "TodoDetailActivity started");
					}
				});
			}
			
			lblTime = (TextView) convertView.findViewById (R.id.lblTime);
			if (lblTime != null)
				lblTime.setText (String.valueOf(current.get_nTime()));
			
			Log.d("listener", String.valueOf(current.get_ID()));
			return convertView;
		}		
	}
}
