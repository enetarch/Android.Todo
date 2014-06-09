package ENetArch.Todo.Presentation;

import ENetArch.Todo.Data.DbHelper;
import ENetArch.Todo.Presentation.Calendar_Activity;
import ENetArch.Todo.R;
import ENetArch.Todo.BizLogic.TaskType;
import ENetArch.Todo.Data.TaskTypes_Tbl;
import ENetArch.Todo.BizLogic.Todo;
import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar;
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
import android.widget.ImageButton;
import android.widget.Toast;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import java.util.HashSet;
import java.util.Set;
import android.widget.ArrayAdapter;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;
import java.lang.Integer;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class TodoDetail_Activity extends Activity
{
	public final static String GET_TODO = "com.ENetArch.TodoDetailActivity.GET_TODO";
	public final static String SET_TODO = "com.ENetArch.TodoDetailActivity.SET_TODO";

	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	protected TaskTypes_Tbl tblTT;
	protected Todo thsTodo = null;
	protected TodoDetail_Activity thsActivity = null;
	
	protected CheckBox cbxComplete = null;
	protected ImageButton btnDatePicker = null;
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

		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		// ============================
		
		cbxComplete = (CheckBox) this.findViewById (R.id.cbxStatus);
		btnDatePicker = (ImageButton) this.findViewById (R.id.btnTargetDate);
		txtDate = (EditText) this.findViewById (R.id.txtTargetDate);
		lbxPriority = (Spinner) this.findViewById (R.id.lbxPriority);
		lbxTaskType = (Spinner) this.findViewById (R.id.lbxTaskType);
		txtMemo = (EditText) this.findViewById (R.id.txtMemo);
		txtTime = (EditText) this.findViewById (R.id.txtTime);
		lblCompleted = (TextView) this.findViewById (R.id.ldlDateCompleted);

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
		
		tblTT = db.get_TaskTypes ();
		List<TaskType> allTaskTypes = tblTT.getAll();
		
		List<String> aryTaskTypes = new ArrayList<String>();
		
		for(t=0; t<allTaskTypes.size(); t++ )
			aryTaskTypes.add(allTaskTypes.get(t).get_szType());
		
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, aryTaskTypes); 
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); 
		lbxTaskType.setAdapter(adapter2);
		
		// ============================
		
		Intent intent = getIntent();
		String szGUID = intent.getStringExtra(SET_TODO);
	
		thsTodo = db.get_Todos().get(szGUID);
		if (thsTodo == null)
		{
			Toast.makeText (getApplicationContext(), 
				  "Todo Not Found!!!", Toast.LENGTH_LONG).show();
		}
		else
		{
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			String YMD = "";
			
			if (thsTodo.get_bCompleted() == 1)
			{
				cal.setTime (thsTodo.get_dCompleted());
				YMD = sdf.format(cal.getTime());
				
				cbxComplete.setChecked ( true );
				lblCompleted.setText (YMD);
			}
			else
			{
				cbxComplete.setChecked ( false );
				lblCompleted.setText ("");
			}
			
			cal.setTime (thsTodo.get_dTarget());
			YMD = sdf.format(cal.getTime());
			
			txtDate.setText (YMD);

			lbxPriority.setSelection ((thsTodo.get_nPriority()-1 < 0) ? 0 : thsTodo.get_nPriority()-1);
			lbxTaskType.setSelection ((thsTodo.get_nTaskType()-1 < 0) ? 0 : thsTodo.get_nTaskType()-1);
			
			txtMemo.setText (thsTodo.get_szMemo());
			txtTime.setText (String.valueOf(thsTodo.get_nTime()));
		}
		
		btnDatePicker.setOnClickListener (new View.OnClickListener() 
		{
			@Override
			public void onClick (View v)
			{ thsActivity.btnCalendar_onClick (v); }
		  });
	}	
	
	// ===========================================
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.        
		getMenuInflater().inflate(R.menu.todo_detail_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		View v = thsActivity.getCurrentFocus();
		
		// Handle item selection    
		switch (item.getItemId()) 
		{
			case R.id.action_cancel: cmdCancel_click(); return true;
			case R.id.action_save: cmdSave_click(); return true;
			case R.id.action_delete: cmdDelete_click(); return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	public void cmdCancel_click() 
	{ NavUtils.navigateUpFromSameTask(this); }
	
	public void cmdSave_click() 
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try
		{ thsTodo.set_dTarget (sdf.parse  ( txtDate.getText ().toString())); }
		catch (ParseException e) 
		{}

		thsTodo.set_bCompleted (cbxComplete.isChecked () == true ? 1 : 0);
		// thsTodo.set_dTarget (new Date ( txtDate.getText ().toString() ));
		thsTodo.set_nPriority ((int) lbxPriority.getSelectedItemId()+1);
		thsTodo.set_nTaskType ((int) lbxTaskType.getSelectedItemId()+1);
		thsTodo.set_szMemo (txtMemo.getText ().toString());
		thsTodo.set_nTime (Integer.parseInt (txtTime.getText().toString()));
		
		db.get_Todos().update (thsTodo);

//		Toast.makeText (getApplicationContext(), 
//				  "Saving UPdates ", Toast.LENGTH_SHORT).show();
		
		NavUtils.navigateUpFromSameTask(this);
	}
	
	public void cmdDelete_click() 
	{ NavUtils.navigateUpFromSameTask(this); }
	
	// ===========================================
	
	public void btnCalendar_onClick (View v)
	{
		Log.d("intent", "start TodoDetailActivity ==");
		Log.d("intent", "thsActivity == " + ((thsActivity == null) ? "true" : "false"));

		Intent intent = new Intent (thsActivity, Calendar_Activity.class);
		Log.d("intent", "new TodoDetailActivity");

		Calendar dTarget = Calendar.getInstance();	
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		dTarget.setTime (thsTodo.get_dTarget());
		String szdTarget = sdf.format(dTarget.getTime());

		intent.putExtra (Calendar_Activity.SET_DATE_TARGET, szdTarget);
		Log.d("intent", "TodoDetailActivity message sent");
		
		startActivityForResult (intent, Calendar_Activity.nGET_DATE_TARGET);
		Log.d("intent", "TodoDetailActivity started");
	}
	
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
					txtDate.setText (szdTarget);
				}				  
			} break; 
		} 
	}	
}
