package ENetArch.Todo.Presentation;

import ENetArch.Todo.RPC.callRPC;
import ENetArch.Todo.BizLogic.WebServer;
import ENetArch.Todo.Data.DbHelper;
import ENetArch.Todo.R;
import ENetArch.Todo.RPC.RPC;
import ENetArch.Todo.RPC.Todos_RPC;

import android.app.ActionBar;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import java.util.ArrayList;
import java.util.List;
import android.widget.EditText;
import android.support.v4.app.FragmentActivity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.app.AlertDialog;
import android.app.AlertDialog.*;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.*;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import java.io.UnsupportedEncodingException;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.lang.InterruptedException;
import java.util.concurrent.ExecutionException;

import 	org.json.JSONObject;
import org.json.JSONException;

public class Settings_WebServer_Edit_Activity extends Activity
{
	public final static String GET_WEBSERVER = "com.ENetArch.WebServer.GET_WEBSERVER";
	public final static String SET_WEBSERVER = "com.ENetArch.WebServer.SET_WEBSERVER";

	protected Activity thsActivity = null;
	protected DbHelper db;
	
	protected WebServer thsWebServer = null;
	
	protected EditText txtName = null;
	protected EditText txtURL = null;
	protected EditText txtUID = null;
	protected EditText txtPSW = null;
	protected EditText txtConfirm = null;	
	
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		thsActivity = this;
		super.onCreate (savedInstanceState);
		
		db = new DbHelper(this);
		
		setContentView (R.layout.webserver_new);

		ActionBar actionBar = getActionBar();
		actionBar.show();
		
		// ============================
		
		txtName = (EditText) this.findViewById (R.id.txtName);
		txtURL = (EditText) this.findViewById (R.id.txtURL);
		txtUID = (EditText) this.findViewById (R.id.txtUserName);
		txtPSW = (EditText) this.findViewById (R.id.txtPassword);
		txtConfirm = (EditText) this.findViewById (R.id.txtConfirm);


		Intent intent = getIntent();
		String szGUID = intent.getStringExtra (SET_WEBSERVER);
	
		Log.d ("onCreate", "getting web server");
		
		thsWebServer = db.get_WebServers().get (szGUID);
		
		Log.d ("onCreate", "got web server ");
		
		if (thsWebServer == null)
		{
			Toast.makeText (getApplicationContext(), 
				  "WebServer Not Found!!!", Toast.LENGTH_LONG).show();
		}
		else
		{
			Log.d ("onCreate", "Initializing Fields");
			
			txtName.setText (thsWebServer.get_szName());
			txtURL.setText (thsWebServer.get_szURL());
			txtUID.setText (thsWebServer.get_szUID());
			txtPSW.setText (thsWebServer.get_szPSW());
			txtConfirm.setText (thsWebServer.get_szPSW());
			
			Log.d ("onCreate", "Fields Initialized");
		}
	}
  
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.        
		getMenuInflater().inflate(R.menu.webserver_edit_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		View v = thsActivity.getCurrentFocus();
		
		// Handle item selection    
		switch (item.getItemId()) 
		{
			case R.id.action_delete: cmdDelete (v); return true;
			case R.id.action_save: cmdSave (v); return true;
			case R.id.action_test: cmdTest (v); return true;
			default: return super.onOptionsItemSelected(item);
		}
	}

	// =====================================
	
	public void cmdTest (View v) 
	{
		Log.d ("cmdTest", "entered foo ");
		
		thsWebServer.set_szName ( get_szName () );
		thsWebServer.set_szURL ( get_szURL () );
		thsWebServer.set_szUID ( get_szUID () );
		thsWebServer.set_szPSW ( get_szPSW () );
		
		Todos_RPC rpc = new Todos_RPC (get_szURL (), get_szUID (),  get_szPSW ());
		
		if (rpc.cmdTest() )
		{ Toast.makeText(this, "Test Successful", Toast.LENGTH_LONG).show(); }
		else
		{ Toast.makeText(this, "Test Failed", Toast.LENGTH_LONG).show(); }
		
		Log.d ("cmdTest", "exiting foo ");
	}
	
	// =====================================
	
	public void cmdDelete (View v) 
	{
		Log.d ("cmdDelete", "entered foo ");
		
		db.get_WebServers().delete (thsWebServer.get_GUID());
		
		NavUtils.navigateUpFromSameTask(this); 
	}
	
	// =====================================
	
	public void cmdSave (View v) 
	{
		Log.d ("cmdSave", "entered foo ");
		
		Toast.makeText (getApplicationContext(), 
			"cmdSave WebServer ", Toast.LENGTH_LONG).show();
		
		thsWebServer.set_szName ( get_szName () );
		thsWebServer.set_szURL ( get_szURL () );
		thsWebServer.set_szUID ( get_szUID () );
		thsWebServer.set_szPSW ( get_szPSW () );
		
		db.get_WebServers().update (thsWebServer);
		
		NavUtils.navigateUpFromSameTask(this); 
	}
	
	// =====================================
	
	public String get_szName ()
	{
		EditText txtName = (EditText) findViewById(R.id.txtName);
		return (txtName.getText().toString());
	}
	
	public String get_szURL ()
	{
		EditText txtURL = (EditText) findViewById(R.id.txtURL);
		return (txtURL.getText().toString());
	}
	
	public String get_szUID ()
	{
		EditText txtUserName = (EditText) findViewById(R.id.txtUserName);
		return (txtUserName.getText().toString());
	}
	
	public String get_szPSW ()
	{
		EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
		return (txtPassword.getText().toString());
	}

	public String get_szConfirm () 
	{ 
		EditText txtConfirm = (EditText) findViewById(R.id.txtConfirm);
		return (txtConfirm.getText().toString());
	}
}
