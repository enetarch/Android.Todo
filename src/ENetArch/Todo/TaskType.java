package ENetArch.Todo;

import java.util.Date;
import java.util.UUID;

public class TaskType 
{    
	private int nID;

	private String szGUID;
	private Date dUpdated;

	private String szType;    

	public TaskType()    
	{              
		this.szType = null;   
		this.szGUID = UUID.randomUUID().toString();
		this.dUpdated = new Date ();
	}     

	public  TaskType(String taskName) 
	{           
		super();
		this.szType = taskName;            
		this.szGUID = UUID.randomUUID().toString();
		this.dUpdated = new Date ();
	}

	// =============================

	public String getState()
	{ return (""); }

	public void setState(String thsState)
	{}

	// =============================

	public int get_ID() 
	{  return (this.nID);    }    

	public void set_ID(int thsID) 
	{  this.nID = thsID;  }    

	public String get_GUID() 
	{  return (this.szGUID);    }    

	public void set_GUID(String thsGUID) 
	{  this.szGUID = thsGUID;  }    

	public Date get_dUpdated() 
	{  return (this.dUpdated);    }    

	public void set_dUpdated(Date thsDate)
	{ this.dUpdated = thsDate; }

	public String get_szType() 
	{  return (this.szType);  } 

	public void set_szType(String thsTask) 
	{ 
		this.szType = thsTask;  
		this.dUpdated = new Date();
	}    
}
