package ENetArch.Todo.Presentation;

import ENetArch.Todo.BizLogic.WebServer;
import ENetArch.Todo.Data.DbHelper;
import ENetArch.Todo.R;
import ENetArch.Todo.Data.WebServers_Tbl;
import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.app.ActionBar;
import android.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
// import android.support.v4.app.DialogFragment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.content.Intent;
import android.database.sqlite.*;
import android.widget.*;

public class Settings_WebServers_Activity extends FragmentActivity
	implements Settings_WebServer_New_Dialog.NoticeDialogListener 
{
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	Settings_WebServers_Activity thsActivity = null;
	
	protected MyAdapter adapter = null;
	protected ListView lv = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		thsActivity = this;
		super.onCreate (savedInstanceState);

		setContentView(R.layout.webservers);
		// db = new DbHelper(this);	

		Log.d ("onCreateView ", "Entered Function" );
		
		db = new DbHelper(this);
		
		cmdRefresh_click();
		
		Log.d ("onCreateView ", "Exiting Function" );
    }

	public void cmdRefresh_click ()
	{
		Log.d ("cmdRefresh ", "Entering Function" );

		WebServers_Tbl webservers = db.get_WebServers();
		
		List<WebServer> strings = webservers.getAll ();
		
		adapter = new MyAdapter (this, R.layout.webservers_list, strings);
			 
		lv = (ListView) findViewById (android.R.id.list);
		
		lv.setAdapter (adapter);
		
		Log.d ("cmdRefresh ", "Exiting Function" );
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.        
		getMenuInflater().inflate(R.menu.webserver_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		Log.d("onOptionsItemSelected", "option selected");
		Log.d ("menu option selected", String.valueOf (item.getItemId()));
		Log.d ("menu option seeking", String.valueOf(R.id.action_webservers));
		
		View v = thsActivity.getCurrentFocus();

		// Handle item selection    
		switch (item.getItemId()) 
		{
			case R.id.action_addserver: cmdShowNewServer(v); return true;
			default: return super.onOptionsItemSelected(item);
		}
	}

	// =====================================

	public void cmdShowNewServer (View v) 
	{
		Log.d("intent", "new ReportActivity");
		
		Settings_WebServer_New_Dialog dialog = new Settings_WebServer_New_Dialog();
		dialog.show (getFragmentManager(), "missiles");		
		
		Log.d("intent", "ReportActivity started");
	}

	// =====================================

	public void cmdEdit () 
	{
		Intent intent = new Intent (thsActivity, Settings_WebServer_Edit_Activity.class);
		Log.d("intent", "new Settings_WebServer_Edit");

		startActivity (intent);
		Log.d("intent", "Settings_WebServer_Edit started");
	}
	
	// =====================================

	public void OK_onClick (DialogFragment dialog) 
	{ 
		Log.d ("OK_onClick", ""); 
		
		String szName = ((Settings_WebServer_New_Dialog) dialog).get_szName();
		String szURL = ((Settings_WebServer_New_Dialog) dialog).get_szURL();
		String szUID = ((Settings_WebServer_New_Dialog) dialog).get_szUID();
		String szPSW = ((Settings_WebServer_New_Dialog) dialog).get_szPSW();
		
		// Add to Database Table
		Log.d ("Data", szName + ", " + szURL + ", " + szUID + ", " + szPSW);

		WebServer webserver = new WebServer ();

		webserver.set_szName ( szName); 
		webserver.set_szURL ( szURL);
		webserver.set_szUID ( szUID );
		webserver.set_szPSW ( szPSW);
		
		Log.d ("Getting Table", "WebServer_Tbl");
		WebServers_Tbl webserver_tbl = db.get_WebServers();
		
		Log.d ("Saving State", "webserver");
		webserver_tbl.add (webserver);
		
		cmdRefresh_click();
	}
	
	public void Test_onClick (DialogFragment dialog) 
	{ 
		String szName = ((Settings_WebServer_New_Dialog) dialog).get_szName();
		String szURL = ((Settings_WebServer_New_Dialog) dialog).get_szURL();
		String szUID = ((Settings_WebServer_New_Dialog) dialog).get_szUID();
		String szPSW = ((Settings_WebServer_New_Dialog) dialog).get_szPSW();
		
		// Add to Database Table
		Log.d ("Data", szName + ", " + szURL + ", " + szUID + ", " + szPSW);
	}
	
	public void Cancel_onClick (DialogFragment dialog) 
	{ Log.d ("Cancel_onClick", ""); }
	
	// =====================================
	
	private class MyAdapter extends ArrayAdapter<WebServer>
	{
		private Context context;
		private List<WebServer> list = new ArrayList<WebServer>();
		private int layoutResourceId;

		public MyAdapter(Context context, int layoutResourceId, List<WebServer> objects)
		{
			super (context, layoutResourceId, objects);

			Log.d ("MyAdapter ", "construct entered" );
			
			this.layoutResourceId = layoutResourceId;
			this.list = objects;
			this.context = context;
			
			Log.d ("MyAdapter ", "construct exited" );
		}

		@Override
		public int getCount ()
		{ 
			Log.d ("getCount ", String.valueOf (list.size()) );
			return (list.size()); 
		}

		private class INT
		{ int value; }
		
		@Override
		public View getView (int position, View convertView, ViewGroup parent)
		{
			Log.d ("getView position", String.valueOf (position) );
			INT thsPosition = new INT ();
			thsPosition.value = position;
			
			LinearLayout rowServer = null;
			TextView lblServerName = null;
			TextView lblServerURL = null;
			
			WebServer webserver = list.get (position);

			LayoutInflater inflater = 
				(LayoutInflater) context.getSystemService 
				(Context.LAYOUT_INFLATER_SERVICE);

			convertView = inflater.inflate 
				(R.layout.webservers_list, parent, false);

			
			rowServer = (LinearLayout) convertView.findViewById (R.id.rowServer);
			
			lblServerName = (TextView) convertView.findViewById (R.id.lblServerName);
			if (lblServerName != null)
				lblServerName.setText (webserver.get_szName());
			
			lblServerURL = (TextView) convertView.findViewById (R.id.lblServerURL);
			if (lblServerURL != null)
				lblServerURL.setText (webserver.get_szURL());

			rowServer.setTag (thsPosition);
			
			rowServer.setOnClickListener (new View.OnClickListener()
			{
				@Override
				public void onClick (View v)
				{
					Log.d ("onClick", "Entering Foo");
					
					LinearLayout cb = (LinearLayout) v;
					Log.d ("onClick", " got cb" );
					
					INT thsPosition = (INT) cb.getTag ();
					Log.d ("onClick", "value = " + thsPosition.value);
					
					WebServer webserver = list.get (thsPosition.value);
					
					Log.d ("onClick", webserver.get_szName ());

					Intent intent = new Intent (thsActivity, Settings_WebServer_Edit_Activity.class);
					intent.putExtra (Settings_WebServer_Edit_Activity.SET_WEBSERVER, webserver.get_GUID());
					startActivity (intent);
					}
				});
			
			return convertView;
		}		
	}
}
