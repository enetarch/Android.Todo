/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ENetArch.Todo.RPC;

import ENetArch.Todo.BizLogic.WebServer;

import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import android.widget.Toast;

/**
 *
 * @author mfuhrman
 */

public class RPC extends AsyncTask <callRPC, Integer, String>
{
	protected void onPreExecute()
	{
		Log.d ("onPreExecute", "What's supposed to happen here???" );
	}
		
	protected String doInBackground (callRPC... call)
	{
		Log.d ("doInBackground", "entering foo");
		
		String szURL = call[0].szURL;
		String szCommand = call[0].szCommand;
		String szParams = call[0].szParams;
		
		Log.d ("doInBackground", "got settings");
		
		HttpResponse response = null;
		String responseBody = "";

		try
		{ 
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost (szURL + "/query.php");

			Log.d ("doInBackground", "setEntity ");

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add (new BasicNameValuePair ("szCommand", szCommand));
			nameValuePairs.add (new BasicNameValuePair ("bVerbose", "true"));
			nameValuePairs.add (new BasicNameValuePair ("szParams", szParams));
			post.setEntity (new UrlEncodedFormEntity (nameValuePairs));
		  
			response = client.execute (post);
			responseBody = EntityUtils.toString (response.getEntity ());
		}
		catch (UnsupportedEncodingException e)
		{ Log.d ("doInBackground", "UnsupportedEncodingException " + e.getMessage()); }
		catch (IOException e)
		{ Log.d ("doInBackground", "IOException " + e.getMessage()); }
		
		Log.d ("doInBackground", "execute ");
		
		Log.d ("doInBackground", responseBody);
		
		return (responseBody);
	}
	
	protected void onProgressUpdate (int progress)
	{
		Log.d ("onProgressUpdate", String.valueOf (progress) );
	}
	
	protected void onPostExecute (String result)
	{
		Log.d ("onPostExecute", result);
	}
}
