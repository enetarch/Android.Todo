package ENetArch.Todo.Presentation;

import ENetArch.Todo.Data.DbHelper;
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
/**
 *
 * @author mfuhrman
 */
public class Settings_Application_Preferences extends PreferenceFragment
{
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	Settings_Application_Preferences thsActivity = null;

	/**
	 * Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		thsActivity = this;
		super.onCreate (savedInstanceState);
		// ToDo add your GUI initialization code here     

		addPreferencesFromResource(R.xml.settings_application);

		// db = new DbHelper(this);	
	}
}
