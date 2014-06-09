/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ENetArch.Todo.Presentation;

import ENetArch.Todo.Data.DbHelper;
import ENetArch.Todo.BizLogic.TaskType;
import ENetArch.Todo.R;
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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;
import android.database.sqlite.*;
/**
 *
 * @author mfuhrman
 */
public class TaskType_Activity extends Activity
{
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	List<TaskType> list;
	MyAdapter adapt;
	TaskType_Activity thsActivity = null;
	
	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		thsActivity = this;
		super.onCreate(savedInstanceState);
		// ToDo add your GUI initialization code here     
		
		setContentView(R.layout.tasktypes_activity);

		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		db = new DbHelper(this);
		
		list = db.get_TaskTypes().getAll();
		// Toast.makeText(context, "getting tasks", Toast.LENGTH_LONG).show();

		adapt = new MyAdapter(this, R.layout.tasktypes_list, list);
		// Toast.makeText(context, "got the task view adapter", Toast.LENGTH_LONG).show();

		ListView listTask = (ListView) findViewById(R.id.listView2);
		// Toast.makeText(context, "getting the list view adapter", Toast.LENGTH_LONG).show();

		listTask.setAdapter(adapt);
		// Toast.makeText(context, "connecting the task list view to the adapter", Toast.LENGTH_LONG).show();	
	}
	
	public void addTaskType_click(View v)
	{
		EditText t = (EditText) findViewById(R.id.txtTaskType);
		String s = t.getText().toString();

		if (s.equalsIgnoreCase(""))
		{
			Toast.makeText(this, "enter the task description first!!", Toast.LENGTH_LONG).show();
		} else
		{
			TaskType task = new TaskType(s);
			db.get_TaskTypes().add(task);
			Log.d("task type", "data added");
			t.setText("");
			adapt.add(task);
			adapt.notifyDataSetChanged();
		}
	}


	private class MyAdapter extends ArrayAdapter<TaskType>
	{
		Context context;
		List<TaskType> taskList = new ArrayList<TaskType>();
		int layoutResourceId;

		public MyAdapter(Context context, int layoutResourceId, List<TaskType> objects)
		{
			super (context, layoutResourceId, objects);
			this.layoutResourceId = layoutResourceId;
			this.taskList = objects;
			this.context = context;
		}

		@Override
		public View getView (int position, View convertView, ViewGroup parent)
		{
			TextView txt = null;

			TaskType current = taskList.get (position);

			LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService 
			(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate 
			(R.layout.tasktypes_list, parent, false);

			txt = (TextView) convertView.findViewById (R.id.lblTaskType);
			if (txt != null)
			{
				txt.setText (current.get_szType());
				txt.setTag (current);

				convertView.setTag (txt);
			}

			Log.d("listener", String.valueOf(current.get_ID()));
			return convertView;
		}
	}
}
