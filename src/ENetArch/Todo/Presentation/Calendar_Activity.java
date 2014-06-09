/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ENetArch.Todo.Presentation;

import ENetArch.Todo.R;
import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author mfuhrman
 */
public class Calendar_Activity extends Activity
{
	public final static int  nGET_DATE_TARGET = 1;
	public final static int  nSET_DATE_TARGET = 2;
	public final static String GET_DATE_TARGET = "com.ENetArch.CalendarActivity.GetDateTarget";
	public final static String SET_DATE_TARGET = "com.ENetArch.CalendarActivity.SetDateTarget";

	protected Calendar_Activity thsActivity = null;
	
	@Override
	public void onCreate(Bundle icicle)
	{
		thsActivity = this;
		;
		super.onCreate(icicle);
		
		setContentView(R.layout.calendar_activity);

		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		// ========================================
		
		Intent intent = getIntent();
		String szdTarget = intent.getStringExtra(SET_DATE_TARGET);
	
		Date dTarget = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		try
		{ dTarget = sdf.parse  (szdTarget); }
		catch (ParseException e) 
		{}
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(dTarget);

		// ========================================
		
		DatePicker datePicker = (DatePicker) this.findViewById (R.id.calDateTarget);
		datePicker.init 
			(
				cal.get(Calendar.YEAR), 
				cal.get(Calendar.MONTH), 
				cal.get(Calendar.DAY_OF_MONTH), 
				null
			);		
	}
	// ===========================================
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.        
		getMenuInflater().inflate(R.menu.calendar_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		View v = thsActivity.getCurrentFocus();
		
		// Handle item selection    
		switch (item.getItemId()) 
		{
			case R.id.action_cancel: cmdCancel_click(v); return true;
			case R.id.action_ok: cmdDone_Click(v); return true;
			default: return super.onOptionsItemSelected(item);
		}
	}
	
	public void cmdCancel_click(View v) 
	{ NavUtils.navigateUpFromSameTask(this); }
	
	public void cmdDone_Click (View v)
	{
		Calendar dTarget = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		DatePicker datePicker = (DatePicker) this.findViewById (R.id.calDateTarget);
		dTarget.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth()); 
		
		Intent resultIntent = new Intent("");
		resultIntent.putExtra(SET_DATE_TARGET, sdf.format (dTarget.getTime()) );
		setResult(Activity.RESULT_OK, resultIntent);
		finish();
	}
}
