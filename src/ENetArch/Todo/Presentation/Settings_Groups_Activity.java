/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ENetArch.Todo.Presentation;

import ENetArch.Todo.R;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
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
import android.widget.*;

public class Settings_Groups_Activity extends Activity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{ 
		super.onCreate (savedInstanceState); 
		setContentView (R.layout.settings_groups);
	}
	
	// @Override
	public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		Log.d ("Settings_Groups_Fragment.onCreateView","enetered");
		
		View view = inflater.inflate(R.layout.settings_groups, container, false);
		return view;
	}

	// =====================================

	public void cmdShowApplicationSettings_onClick (View v) 
	{
		Intent intent = new Intent (this, Settings_Application_Activity.class);
		Log.d("intent", "new ReportActivity");

		startActivity (intent);
		Log.d("intent", "ReportActivity started");
	}
	
	// =====================================

	public void cmdShowWebServers_onClick (View v) 
	{
		Intent intent = new Intent (this, Settings_WebServers_Activity.class);
		Log.d("intent", "new ReportActivity");

		startActivity (intent);
		Log.d("intent", "ReportActivity started");
	}
	// =====================================

	public void cmdShowReminders_onClick (View v) 
	{
		Intent intent = new Intent (this, Settings_Reminders_Activity.class);
		Log.d("intent", "new ReportActivity");

		startActivity (intent);
		Log.d("intent", "ReportActivity started");
	}
	
}
