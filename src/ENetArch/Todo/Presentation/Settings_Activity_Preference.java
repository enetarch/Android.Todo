package ENetArch.Todo.Presentation;

import ENetArch.Todo.Data.DbHelper;
import ENetArch.Todo.R;
import android.app.ActionBar;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import java.util.List;

public class Settings_Activity_Preference extends PreferenceActivity
// implements MyListFragment.OnItemSelectedListener
{
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	Settings_Activity_Preference thsActivity = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		thsActivity = this;
		super.onCreate (savedInstanceState);
		// ToDo add your GUI initialization code here     

		// addPreferencesFromResource(R.layout.settings_activity);

		ActionBar actionBar = getActionBar();
		actionBar.show();

		db = new DbHelper(this);	
	}

	@Override
	public void onBuildHeaders(List<Header> target) 
	{
		loadHeadersFromResource(R.xml.settings_headers, target);
	}
}
