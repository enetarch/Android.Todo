/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ENetArch.Todo.RPC;

import ENetArch.Todo.BizLogic.*;
import ENetArch.Todo.Data.*;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author mfuhrman
 */
public class Todos_RPC
{
	private static final String cTodos_Test = "todos::test"; 
	private static final String cTodos_Login = "todos::login";
	private static final String cTodos_List = "todos::list"; 
	private static final String cTodos_Sync = "todos::sync";
	
	private static final String cTodo_Add = "todo::add"; 
	private static final String cTodo_Delete = "todo::delete"; 
	private static final String cTodo_Get = "todo::get";
	private static final String cTodo_Update = "todo::update";

	private String szURL = "";
	private String szUID = "";
	private String szPSW = "";
	private String szSessionID = "";
	
	
	public Todos_RPC (String thsURL, String thsUID, String thsPSW)
	{
		szURL = thsURL;
		szUID = thsUID;
		szPSW = thsPSW;
	}
	
	private JSONObject execute (String szURL, String szCommand, JSONObject params) 
	{
		Log.d ("execute", "entered foo ");
		
		String szParams = "";
		if (params != null)
			szParams = params.toString();

		Log.d ("JSON", szParams);
		
		callRPC call = new callRPC (szURL, Todos_RPC.cTodos_Test, szParams);
		
		RPC task = new RPC();
		task.execute (call);
		
		String szJSON = "";
		JSONObject returned = new JSONObject();
		
		try
		{
			szJSON = task.get();
			
			returned = new JSONObject (szJSON);
		}
		catch (InterruptedException e)
		{ Log.d ("task.get", e.getMessage() ); }
		catch (ExecutionException e)
		{ Log.d ("task.get", e.getMessage() ); }
		catch (JSONException e)
		{ Log.d ("json", e.getMessage() ); }
		
		return (returned);
	}

	public boolean cmdTest () 
	{
		Log.d ("cmdTest", "entered foo ");
		
		JSONObject params = new JSONObject ();
		
		try
		{
			params.put ("szUID", szUID);
			params.put ("szPSW", szPSW);
		}
		catch (JSONException e)
		{ Log.d ("params.put", e.getMessage() ); }
		
		JSONObject json = execute (szURL, Todos_RPC.cTodos_Test, params);
		
		boolean bSuccess = false;
		
		try
		{
			Log.d ("json", "getJSONObject ( return )");
			JSONObject jsnRtn = json.getJSONObject ("return");
			
			Log.d ("jsnRtn", "getString ( bSuccess )");
			String szSuccess = jsnRtn.getString ("bSuccess");
			
			Log.d ("bSuccess", szSuccess);
			bSuccess = (szSuccess == "true");
		}
		catch (JSONException e)
		{ Log.d ("json", e.getMessage() ); }
		
		return (bSuccess);
	}
	
	public boolean Login ()
	{
		Log.d ("cmdTest", "entered foo ");
		
		JSONObject params = new JSONObject ();
		
		try
		{
			params.put ("szUID", szUID);
			params.put ("szPSW", szPSW);
		}
		catch (JSONException e)
		{ Log.d ("params.put", e.getMessage() ); }
		
		JSONObject json = execute (szURL, Todos_RPC.cTodos_Login, params);
		
		boolean bSuccess = false;
		
		try
		{
			Log.d ("json", "getJSONObject ( return )");
			JSONObject jsnRtn = json.getJSONObject ("return");
			
			Log.d ("jsnRtn", "getString ( bSuccess )");
			szSessionID = jsnRtn.getString ("SessionID");
			
			Log.d ("SessionID", szSessionID);
			bSuccess = (szSessionID == "true");
		}
		catch (JSONException e)
		{ Log.d ("json", e.getMessage() ); }
		
		return (bSuccess);
	}

	public boolean Synch (DbHelper db)
	{
		Log.d ("cmdTest", "entered foo ");
		
		JSONArray todos = db.get_Todos ().getAlljson ();
		JSONArray tasktypes = db.get_TaskTypes ().getAlljson ();
		
		String szTodos = todos.toString();
		String szTaskTypes = tasktypes.toString();
		
		JSONObject params = new JSONObject ();
		
		try
		{
			params.put ("szSessionID", szSessionID);
			params.put ("Todos", szTodos);
			params.put ("TaskTypes", szTaskTypes);
		}
		catch (JSONException e)
		{ Log.d ("params.put", e.getMessage() ); }
		
		JSONObject json = execute (szURL, Todos_RPC.cTodos_Sync, params);
		
		try
		{
			Log.d ("json", "getJSONObject ( return )");
			JSONObject jsnRtn = json.getJSONObject ("return");
			
			Log.d ("todos", "getJSONObject ( todos )");
			todos = jsnRtn.getJSONArray ("Todos");

			Log.d ("tasktypes", "getJSONObject ( tasktypes )");
			tasktypes = jsnRtn.getJSONArray ("TaskTypes");
			
		}
		catch (JSONException e)
		{ Log.d ("json", e.getMessage() ); }

		db.get_Todos ().sync (todos);
		db.get_TaskTypes ().sync (tasktypes);
		
		
		return (true);
	}
}