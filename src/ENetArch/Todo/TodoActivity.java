package ENetArch.Todo;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;
import android.view.MenuInflater;
import android.widget.PopupMenu;

public class TodoActivity extends Activity
// public class TodoActivity extends ActionBarActivity
{
	public final static String EXTRA_MESSAGE = "com.ENetArch.TodoActivity.MESSAGE";
	
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	List<Todo> todoList;
	List<TaskType> tasktypeList;
	MyAdapter adapt;
	TodoActivity thsActivity = null;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		thsActivity = this;
		Context context = getApplicationContext();
		
		// Toast.makeText(context, "starting the app", Toast.LENGTH_LONG).show();
		
		super.onCreate(savedInstanceState);
		// Toast.makeText(context, "created app", Toast.LENGTH_LONG).show();
		
		setContentView(R.layout.todos_activity);
		// Toast.makeText(context, "setting content view", Toast.LENGTH_LONG).show();
		
		Log.d("create instance of DB Helper ","");
		
		db = new DbHelper(this);
		// Toast.makeText(context, "connecting to DB", Toast.LENGTH_LONG).show();
		
		Log.d("DB Helper created ","");
		
		todoList = db.getTodo_Table().getAllTodos();
		tasktypeList = db.getTaskType_Table().getAllTaskTypes();
		// Toast.makeText(context, "getting tasks", Toast.LENGTH_LONG).show();
		
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
		EditText t = (EditText) findViewById(R.id.txtTodo);
		String s = t.getText().toString();

		if (s.equalsIgnoreCase(""))
		{
			Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG).show();
		} else
		{
			Todo task = new Todo(s, 0);
			db.getTodo_Table().addTodo(task);
			Log.d("tasker", "dataadded");
			t.setText("");
			adapt.add(task);
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
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		View v = thsActivity.getCurrentFocus();
		
		// Handle item selection    
		switch (item.getItemId()) 
		{
			case R.id.action_refresh: cmdRefresh(); return true;
			case R.id.action_tasktypes: cmdShowTaskTypes(); return true;
			case R.id.action_calendar: cmdShowCalendar(); return true;
			case R.id.action_sortby: cmdShowSortBy (v); return true;
			case R.id.action_view: cmdShowView (v); return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	public void cmdRefresh () {}
	public void cmdShowCalendar () 
	{
		Intent intent = new Intent (thsActivity, CalendarActivity.class);
		Log.d("intent", "new CalendarActivity");

		startActivity (intent);
		Log.d("intent", "CalendarActivity started");
	}
	
	public void cmdShowSortBy (View v)
	{
		PopupMenu popup = new PopupMenu(this, v);
		popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener () 
		{
			public boolean onMenuItemClick (MenuItem item)
			{ return (thsActivity.onSortByItemClick (item)); }
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
			{ return (thsActivity.onViewItemClick (item)); }
		} );
		
		MenuInflater inflater = popup.getMenuInflater();
		inflater.inflate(R.menu.view_context_menu, popup.getMenu());
		
		popup.show();	
	}
	
	public void cmdShowTaskTypes () 
	{
		Intent intent = new Intent (thsActivity, TaskTypeActivity.class);
		Log.d("intent", "new TaskTypeActivity");

		startActivity (intent);
		Log.d("intent", "TaskTypeActivity started");
	}
	
	// =========================================================
	
	public boolean onSortByItemClick(MenuItem item) 
	{
		 switch (item.getItemId()) 
		 {
			case R.id.action_sortby_completed: cmdSortBy_Completed(); return true;
			case R.id.action_sortby_priority: cmdSortBy_Priority(); return true;
			case R.id.action_sortby_tasktype: cmdSortBy_TaskType(); return true;
			case R.id.action_sortby_memo: cmdSortBy_Memo(); return true;
			case R.id.action_sortby_time: cmdSortBy_Time(); return true;
			default: return super.onContextItemSelected(item);
		 }
	}

	public void cmdSortBy_Completed () {}
	public void cmdSortBy_Priority () {}
	public void cmdSortBy_TaskType () {}
	public void cmdSortBy_Memo () {}
	public void cmdSortBy_Time () {}
	
	// =========================================================
	
	public boolean onViewItemClick(MenuItem item) 
	{
		 switch (item.getItemId()) 
		 {
			case R.id.action_view_completed: cmdView_Completed(); return true;
			case R.id.action_view_priority: cmdView_Priority(); return true;
			case R.id.action_view_tasktype: cmdView_TaskType(); return true;
			case R.id.action_view_memo: cmdView_Memo(); return true;
			case R.id.action_view_time: cmdView_Time(); return true;
			default: return super.onContextItemSelected(item);
		 }
	}

	public void cmdView_Completed () {}
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
						db.getTodo_Table().updateTodo (changeTask);

						Toast.makeText (getApplicationContext(), 
								  "Clicked on Checkbox: " + cb.getText() + " is " + cb.isChecked(), 
								  Toast.LENGTH_LONG).show();
					}
				});
			}
			
			lblPriority = (TextView) convertView.findViewById (R.id.lblPriority);
			if (lblPriority != null)
				lblPriority.setText (String.valueOf(current.get_nPriority()));
			
			lblTaskType = (TextView) convertView.findViewById (R.id.lblTaskType);
			if (lblTaskType != null)
			{
				String szTT = "";
				int nTT = current.get_nTaskType();
				if ((nTT != 0) && (nTT < tasktypeList.size()))
					szTT = tasktypeList.get (nTT).get_szType();
				
				lblTaskType.setText (szTT);
			}

			lblMemo = (TextView) convertView.findViewById (R.id.lblMemo);
			if (lblMemo != null)
			{
				lblMemo.setText (current.get_szMemo());
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

						Intent intent = new Intent (thsActivity, TodoDetailActivity.class);
						Log.d("intent", "new TodoDetailActivity");

						intent.putExtra (EXTRA_MESSAGE, changeTask.get_GUID());
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
