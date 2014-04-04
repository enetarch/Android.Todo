package ENetArch.Todo;

import static ENetArch.Todo.TodoActivity.EXTRA_MESSAGE;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import java.util.HashSet;
import java.util.Set;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.lang.Integer;

public class TodoDetailActivity extends Activity
{
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	protected TaskTypeTbl tblTT;
	protected Todo thsTodo = null;
	protected TodoDetailActivity thsActivity = null;
	
	protected CheckBox cbxComplete = null;
	protected Button btnDatePicker = null;
	protected EditText txtDate = null;
	protected Spinner lbxPriority = null;
	protected Spinner lbxTaskType = null;
	protected EditText txtMemo = null;
	protected EditText txtTime = null;
	protected TextView lblCompleted = null;
	protected Button btnUpdate = null;
	protected Button btnCancel = null;

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Context context = getApplicationContext();
		
		thsActivity = this;
		
		// ============================
		
		Log.d("create instance of DB Helper ","");
		db = new DbHelper(this);
		Log.d("DB Helper created ","");
		
		// ============================
		
		super.onCreate(savedInstanceState);
		
		setContentView (R.layout.todo_details_activity);
		
		// ============================
		
		cbxComplete = (CheckBox) this.findViewById (R.id.cbxStatus);
		btnDatePicker = (Button) this.findViewById (R.id.btnTargetDate);
		txtDate = (EditText) this.findViewById (R.id.txtTargetDate);
		lbxPriority = (Spinner) this.findViewById (R.id.lbxPriority);
		lbxTaskType = (Spinner) this.findViewById (R.id.lbxTaskType);
		txtMemo = (EditText) this.findViewById (R.id.txtMemo);
		txtTime = (EditText) this.findViewById (R.id.txtTime);
		lblCompleted = (TextView) this.findViewById (R.id.lblCompleted);
		btnUpdate = (Button) this.findViewById (R.id.btnUpdate);
		btnCancel = (Button) this.findViewById (R.id.btnCancel);

		// ============================
		
		List<String> aryPriorities = new ArrayList<String>();
		
		// ============================
		
		int t=0;
		for(t=1;t<11;t++)
			aryPriorities.add(String.valueOf(t));
	
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aryPriorities); 
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		lbxPriority.setAdapter(adapter);
		
		// ============================
		
		tblTT = db.getTaskType_Table ();
		List<TaskType> allTaskTypes = tblTT.getAllTaskTypes();
		
		List<String> aryTaskTypes = new ArrayList<String>();
		
		// ============================
		
		for(t=0; t<allTaskTypes.size(); t++ )
			aryTaskTypes.add(allTaskTypes.get(t).get_szType());
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aryTaskTypes); 
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		lbxTaskType.setAdapter(adapter2);
		
		// ============================
		
		Intent intent = getIntent();
		String szGUID = intent.getStringExtra(TodoActivity.EXTRA_MESSAGE);
	
		thsTodo = db.getTodo_Table().getTodo(szGUID);
		if (thsTodo == null)
		{
			Toast.makeText (getApplicationContext(), 
				  "Todo Not Found!!!", Toast.LENGTH_LONG).show();
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			String YMD = "";
			
			if (thsTodo.get_bCompleted() == 1)
			{
				cal.setTime (thsTodo.get_dCompleted());
				YMD = 
					String.valueOf (cal.get(Calendar.YEAR)) + "-" +
					String.valueOf (cal.get(Calendar.MONTH)) + "-" +
					String.valueOf (cal.get(Calendar.DAY_OF_MONTH)) ;
				
				cbxComplete.setChecked ( true );
				lblCompleted.setText (YMD);
			}
			else
			{
				cbxComplete.setChecked ( false );
				lblCompleted.setText ("");
			}
			
			cal.setTime (thsTodo.get_dTarget());
			YMD = 
				String.valueOf (cal.get(Calendar.YEAR)) + "-" +
				String.valueOf (cal.get(Calendar.MONTH)) + "-" +
				String.valueOf (cal.get(Calendar.DAY_OF_MONTH)) ;
			
			txtDate.setText (YMD);

			lbxPriority.setSelection (thsTodo.get_nPriority());
			lbxTaskType.setSelection (thsTodo.get_nTaskType());
			
			txtMemo.setText (thsTodo.get_szMemo());
			txtTime.setText (String.valueOf(thsTodo.get_nTime()));
		}
		
		btnDatePicker.setOnClickListener (new View.OnClickListener() 
		{
			@Override
			public void onClick (View v)
			{ thsActivity.btnCalendar_onClick (v); }
		  });

		btnCancel.setOnClickListener (new View.OnClickListener() 
		{
			@Override
			public void onClick (View v)
			{ thsActivity.btnCancel_onClick (v); }
		  });

		btnUpdate.setOnClickListener (new View.OnClickListener() 
		{
			@Override
			public void onClick (View v)
			{ thsActivity.btnUpdate_onClick (v); }
		  });

	}	

	public void btnCalendar_onClick (View v)
	{
		Log.d("intent", "start TodoDetailActivity ==");
		Log.d("intent", "thsActivity == " + ((thsActivity == null) ? "true" : "false"));

		Intent intent = new Intent (thsActivity, CalendarActivity.class);
		Log.d("intent", "new TodoDetailActivity");
		
		intent.putExtra (EXTRA_MESSAGE, thsTodo.get_GUID());
		Log.d("intent", "TodoDetailActivity message sent");
		
		startActivity (intent);
		Log.d("intent", "TodoDetailActivity started");
	}
	
	public void btnCancel_onClick (View v)
	{
		NavUtils.navigateUpFromSameTask(this);
	}
	
	public void btnUpdate_onClick (View v)
	{
		thsTodo.set_bCompleted (cbxComplete.isChecked () == true ? 1 : 0);
		// thsTodo.set_dTarget (new Date ( txtDate.getText ().toString() ));
		thsTodo.set_nPriority ((int) lbxPriority.getSelectedItemId());
		thsTodo.set_nTaskType ((int) lbxTaskType.getSelectedItemId());
		thsTodo.set_szMemo (txtMemo.getText ().toString());
		thsTodo.set_nTime (Integer.parseInt (txtTime.getText().toString()));
		
		db.getTodo_Table().updateTodo (thsTodo);

		Toast.makeText (getApplicationContext(), 
				  "Saving UPdates ", Toast.LENGTH_SHORT).show();
		
		NavUtils.navigateUpFromSameTask(this);
	}
	

}
