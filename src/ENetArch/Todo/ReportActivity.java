package ENetArch.Todo;
import static ENetArch.Todo.TodoDetailActivity.SET_TODO;
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

public class ReportActivity extends Activity
{
	public final static String GET_TODO = "com.ENetArch.TodoDetailActivity.GET_TODO";
	public final static String SET_TODO = "com.ENetArch.TodoDetailActivity.SET_TODO";

	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	protected TaskTypeTbl tblTT;
	protected ReportActivity thsActivity = null;
	
	protected EditText txtFind = null;
	protected CheckBox cbxFindAll = null;
	protected ImageButton btnFind = null;
	protected ImageButton btnDateFrom = null;
	protected ImageButton btnDateTo = null;
	protected EditText txtDateFrom = null;
	protected EditText txtDateTo = null;
	protected EditText txtReport = null;
	
	protected List<TaskType> allTaskTypes = null;
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		Context context = getApplicationContext();
		
		thsActivity = this;
		
		// ============================
		
		super.onCreate(savedInstanceState);
		
		setContentView (R.layout.report_activity);

		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		// ============================
		
		txtFind = (EditText) this.findViewById (R.id.txtFind);
		cbxFindAll = (CheckBox) this.findViewById (R.id.cbxFindAll);
		btnFind = (ImageButton) this.findViewById (R.id.btnFind);
		btnDateFrom = (ImageButton) this.findViewById (R.id.btnDateFrom);
		btnDateTo = (ImageButton) this.findViewById (R.id.btnDateTo);
		txtDateFrom = (EditText) this.findViewById (R.id.txtDateFrom);
		txtDateTo = (EditText) this.findViewById (R.id.txtDateTo);
		txtReport = (EditText) this.findViewById (R.id.txtReport);

		// ============================
		
		db = new DbHelper(this);
		
		// ============================
		
		tblTT = db.getTaskType_Table ();
		allTaskTypes = tblTT.getAllTaskTypes();
		
		// ============================
		
		
		btnFind.setOnClickListener (new View.OnClickListener() 
		{
			@Override
			public void onClick (View v)
			{ thsActivity.cmdFind_onClick (v); }
		  });

		btnDateFrom.setOnClickListener (new View.OnClickListener() 
		{
			@Override
			public void onClick (View v)
			{ thsActivity.cmdCalendar_onClick (v); }
		  });

		btnDateTo.setOnClickListener (new View.OnClickListener() 
		{
			@Override
			public void onClick (View v)
			{ thsActivity.cmdCalendar_onClick (v); }
		  });
	}	
	
	// ===========================================
	
	
	public void cmdCalendar_onClick (View v)
	{
		Log.d("intent", "start TodoDetailActivity ==");
		Log.d("intent", "thsActivity == " + ((thsActivity == null) ? "true" : "false"));

		Intent intent = new Intent (thsActivity, CalendarActivity.class);
		Log.d("intent", "new TodoDetailActivity");

		String szdTarget = txtDateFrom.getText().toString();
		// String szdTarget = txtDateTo.getText().toString();

		intent.putExtra (CalendarActivity.SET_DATE_TARGET, szdTarget);
		Log.d("intent", "TodoDetailActivity message sent");
		
		startActivityForResult (intent, CalendarActivity.nGET_DATE_TARGET);
		Log.d("intent", "TodoDetailActivity started");
	}
	
	@Override 
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{     
		super.onActivityResult(requestCode, resultCode, data); 
		switch(requestCode) 
		{ 
			case (CalendarActivity.nGET_DATE_TARGET) : 
			{ 
				if (resultCode == Activity.RESULT_OK) 
				{ 
					String szdTarget = data.getStringExtra (CalendarActivity.SET_DATE_TARGET);
					// TODO Switch tabs using the index.
					txtDateFrom.setText (szdTarget);
					txtDateTo.setText (szdTarget);
				}				  
			} break; 
		} 
	}	
	
	// ==================================================
	
	public void cmdFind_onClick (View v)
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		List<Todo> todoList;
		String szFilter = txtFind.getText().toString();
		String szdStart = txtDateFrom.getText().toString();
		String szdStop = txtDateTo.getText().toString();
		
		Date dStart = new Date();
		Date dStop = new Date();
		
		if (cbxFindAll.isChecked()) 
		{ todoList = db.getTodo_Table().getAllTodos(szFilter); }
		else
		{ 
			try 
			{ dStart = sdf.parse (szdStart); }
			catch (ParseException e) 
			{ return; }

			try
			{ dStop = sdf.parse (szdStop); }
			catch (ParseException e) 
			{ return; }

			todoList = db.getTodo_Table().getAllTodos(szFilter, dStart, dStop); 
		}

		String szRtn = "";
		int t = 0;
		for (t=0; t<todoList.size(); t++)
		{
			Todo todo = todoList.get(t);
			int nTask = todo.get_nTaskType() -1;
			nTask = (nTask < 0) ? 0 : nTask;
			
			Calendar cal = Calendar.getInstance();
			cal.setTime (todo.get_dCompleted());
			String YMD = sdf.format(cal.getTime());

			szRtn +=
					  YMD + " " +
					  ( todo.get_bCompleted() == 1 ? "[ x ] " : "[ _ ] " ) +
					  String.valueOf (todo.get_nPriority()) + ": " + 
					  allTaskTypes.get(nTask).get_szType() + " " +
					  todo.get_szMemo () + " " +
					  todo.get_nTime() + "\r\n";
		}
		
		txtReport.setText (szRtn);
	}
}
