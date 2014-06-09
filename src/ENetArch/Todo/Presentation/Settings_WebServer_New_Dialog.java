package ENetArch.Todo.Presentation;

import ENetArch.Todo.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.widget.EditText;
import android.support.v4.app.FragmentActivity;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.app.AlertDialog;
import android.app.AlertDialog.*;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View.*;


public class Settings_WebServer_New_Dialog extends DialogFragment
{
	protected DialogFragment thsActivity = null;
	protected NoticeDialogListener mListener = null;
	
	@Override
	public void onCreate (Bundle savedInstanceState)
	{
		thsActivity = this;
		super.onCreate (savedInstanceState);
	}
	
	public interface NoticeDialogListener 
	{
		public void OK_onClick (DialogFragment dialog);
		public void Test_onClick (DialogFragment dialog);
		public void Cancel_onClick (DialogFragment dialog);
	}
 
	@Override
	public void onAttach (Activity activity) 
	{
		super.onAttach (activity);
		Log.d ("onAttach","Enetered");
		// Verify that the host activity implements the callback interface
		
		try {
			// Instantiate the NoticeDialogListener so we can send events to the host
			mListener = (NoticeDialogListener) activity;
			Log.d ("onAttach","Attached Listener");
		} catch (ClassCastException e) {
			// The activity doesn't implement the interface, throw exception
			throw new ClassCastException(activity.toString() +
				" must implement NoticeDialogListener");
		}
	}
  
	// @Override
	public View onCreateview (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		View view = inflater.inflate (R.layout.webserver_new, container, false);
		return view;
	}
	
	@Override
	public Dialog onCreateDialog (Bundle savedInstanceState)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder (getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		builder.setView (inflater.inflate (R.layout.webserver_new, null));
		
		builder.setPositiveButton (R.string.save, new DialogInterface.OnClickListener ()
		{
			public void onClick (DialogInterface dialog, int id)
			{ 
				Log.d ("onClick","Test_onClick");
				Log.d ("mListener == null", (mListener == null) ? "True" : "False");

				if (mListener != null) mListener.OK_onClick (thsActivity); 
			}
		});
		
		builder.setNeutralButton (R.string.test, new DialogInterface.OnClickListener() 
		{
			public void onClick (DialogInterface dialog, int id)
			{ 
				Log.d ("onClick","Test_onClick");
				Log.d ("mListener == null", (mListener == null) ? "True" : "False");

				if (mListener != null) mListener.Test_onClick (thsActivity); 
			}
		});

		builder.setNegativeButton (R.string.cancel, new DialogInterface.OnClickListener() 
		{
			public void onClick (DialogInterface dialog, int id)
			{ 
				Log.d ("onClick","Cancel_onClick");
				
				if (mListener != null) mListener.Cancel_onClick (thsActivity);
				thsActivity.getDialog().cancel(); 
			}
		});
	
		// Dialog dialog = superonCreateDialog(savedInstanceState);
		return (builder.create() );
	}
	
	public String get_szName ()
	{
		EditText txtName = (EditText) this.getDialog().findViewById(R.id.txtName);
		return (txtName.getText().toString());
	}
	
	public String get_szURL ()
	{
		EditText txtURL = (EditText) this.getDialog().findViewById(R.id.txtURL);
		return (txtURL.getText().toString());
	}
	
	public String get_szUID ()
	{
		EditText txtUserName = (EditText) this.getDialog().findViewById(R.id.txtUserName);
		return (txtUserName.getText().toString());
	}
	
	public String get_szPSW ()
	{
		EditText txtPassword = (EditText) this.getDialog().findViewById(R.id.txtPassword);
		return (txtPassword.getText().toString());
	}

	public String get_szConfirm () 
	{ 
		EditText txtConfirm = (EditText) this.getDialog().findViewById(R.id.txtConfirm);
		return (txtConfirm.getText().toString());
	}
}
