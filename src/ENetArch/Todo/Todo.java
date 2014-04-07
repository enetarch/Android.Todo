package ENetArch.Todo;

import java.util.Date;
import java.util.Calendar;
import java.util.UUID;

public class Todo 
{    
	private int nID;
	
	private String szGUID;
	private Date dUpdated = new Date ();;

	private Date dTarget = new Date ();
	private int nPriority = 1;
	private int nTask = 1;
	private int nTime = 0;
	private int  bCompleted;
	private Date dCompleted = new Date ();
	private String szMemo;    
	
	public Todo()    
	{              
		this.szMemo = null;   
		this.bCompleted = 0;
		this.szGUID = UUID.randomUUID().toString();
	}     

	// =============================
	
	public String getState ()
	{ return (""); }
	
	public void setState (String thsState)
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

	public void set_dUpdated (Date thsDate)
	{ this.dUpdated = thsDate; }

	public Date get_dTarget() 
	{  return (this.dTarget);    }    

	public void set_dTarget (Date thsDate)
	{ 
		this.dTarget = thsDate; 
		this.dUpdated = new Date();
	}

	public String get_szMemo() 
	{  return (this.szMemo);  } 

	public void set_szMemo(String thsTask) 
	{ 
		this.szMemo = thsTask;  
		this.dUpdated = new Date();
	}    

	public int get_bCompleted() 
	{  return (this.bCompleted);    }    

	public void set_bCompleted(int status) 
	{        
		this.bCompleted = status; 
		this.dCompleted = new Date();
		this.dUpdated = new Date();
	}

	public void set_dCompleted (Date thsDate)
	{ this.dCompleted = thsDate; }

	public Date get_dCompleted ()
	{ return (this.dCompleted); }

	
	public int get_nTaskType() 
	{  return (this.nTask);    }    

	public void set_nTaskType(int thsTaskType) 
	{  
		this.nTask = thsTaskType;  
		this.dUpdated = new Date();
	}    

	public int get_nPriority() 
	{  return (this.nPriority);    }    

	public void set_nPriority(int thsPriority) 
	{  
		this.nPriority = thsPriority;  
		this.dUpdated = new Date();
	}    
	
	public int get_nTime() 
	{  return (this.nTime);    }    

	public void set_nTime(int thsTime) 
	{  
		this.nTime = thsTime; 
		this.dUpdated = new Date();
	}    
}
