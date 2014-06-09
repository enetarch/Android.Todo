package ENetArch.Todo.Presentation;

import ENetArch.Todo.R;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;


public class Settings_Activity extends FragmentActivity
{
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		Log.d ("Settings_Activity.onCreate","Enetered Function");
				  
		super.onCreate (savedInstanceState);

		Log.d ("setContentView","Entering Function");
		setContentView (R.layout.settings_activity);
		Log.d ("setContentView","Leaving Function");
		
		Log.d ("onCreate","Leaving Function");
	}
}

