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
public class Settings_WebServers_Preferences extends PreferenceFragment
{
	protected SQLiteDatabase sqlDB;
	protected DbHelper db;
	Settings_WebServers_Preferences thsActivity = null;
	
	protected MyAdapter adapter = null;
	protected ListView lv = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		thsActivity = this;
		super.onCreate (savedInstanceState);

		addPreferencesFromResource(R.xml.settings_webservers);
		// getActivity().setContentView(R.layout.webservers);
		// db = new DbHelper(this);	
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		super.onCreateView (inflater, container, savedInstanceState);

		List<String> strings = new ArrayList<String> ();
		strings.add ("WebServer 1");
		strings.add ("WebServer 2");
		strings.add ("WebServer 3");
		strings.add ("WebServer 4");
		strings.add ("WebServer 5");
		strings.add ("WebServer 6");
		
		Log.d ("onCreateView ", "Entered Function" );
		
		View v = inflater.inflate (R.layout.webservers, container, false);
		Log.d ("view", String.valueOf((v == null)?"null":"! null"));
		
		adapter = new MyAdapter (container.getContext(), R.layout.webservers_list, strings);
			 
		lv = (ListView) v.findViewById (android.R.id.list);
		
		lv.setAdapter (adapter);
		
		Log.d ("onCreateView ", "Exiting Function" );
		Log.d ("LinearLayout ", String.valueOf(v.isShown()?"shown":"hidden"));
		Log.d ("ListView ", String.valueOf(lv.isShown()?"shown":"hidden"));

		Log.d ("LinearLayout ", String.valueOf(v.getVisibility()));
		Log.d ("ListView ", String.valueOf(lv.getVisibility()));
	 
		return v;
    }

	@Override
	public void onStart()
	{
		super.onStart();;
		Log.d ("onStart ", "Entered Function" );
		
		View v2 = getActivity().findViewById( R.id.webservers_linearlayout_id ); 
		Log.d ("view 2 ", String.valueOf((v2 == null)?"null":"! null"));
		
		Log.d ("ListView ", String.valueOf(lv.isShown()?"shown":"hidden"));
		
	}
	
	private class MyAdapter extends ArrayAdapter<String>
	{
		private Context context;
		private List<String> strings = new ArrayList<String>();
		private int layoutResourceId;
		private LayoutInflater inflater = null;
		
		public MyAdapter(Context context, int layoutResourceId, List<String> objects)
		{
			super(context, layoutResourceId, objects);

			this.inflater = 
				(LayoutInflater) context.getSystemService 
				(Context.LAYOUT_INFLATER_SERVICE);

			Log.d ("MyAdapter ", "construct entered" );
			
			this.layoutResourceId = layoutResourceId;
			this.strings = objects;
			this.context = context;
			
			Log.d ("MyAdapter ", "construct exited" );
		}

		@Override
		public int getCount ()
		{ 
			Log.d ("getCount ", String.valueOf (strings.size()) );
			return (strings.size()); 
		}
		
		@Override
		public View getView (int position, View convertView, ViewGroup parent)
		{
			Log.d ("getView position", String.valueOf (position) );
			
			TextView lblServerName = null;
			TextView lblServerURL = null;
			
			String current = strings.get (position);

			if (convertView == null)
			{
				convertView = inflater.inflate 
					(R.layout.webservers_list, parent, false);

				lblServerName = (TextView) convertView.findViewById (R.id.lblServerName);
//				lblServerURL = (TextView) convertView.findViewById (R.id.lblServerURL);
				
				convertView.setTag (lblServerName);				
			}
			else
			{
				lblServerName = (TextView) convertView.getTag ();
//				lblServerURL = (TextView) convertView.getTab();
			}
			
			if (lblServerName != null)
				lblServerName.setText (String.valueOf(current));
			
//			if (lblServerURL != null)
//				lblServerURL.setText ("http://www.enetarch.net:8080/daytimers/todos.php");

			return convertView;
		}
	}
}
