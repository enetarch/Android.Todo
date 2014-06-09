package ENetArch.Todo.BizLogic;

import java.util.Date;
import java.util.UUID;

public class WebServer 
{    
	private int nID;

	private String szGUID;
	private Date dUpdated;

	private String szName;    
	private String szURL;    
	private String szUID;    
	private String szPSW;    

	public WebServer()    
	{              
		this.szName = "";   
		this.szURL = "";   
		this.szUID = "";   
		this.szPSW = "";   
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

	// =============================
	
	public String get_szName() 
	{  return (this.szName);  } 

	public void set_szName(String thsName) 
	{ 
		this.szName = thsName;  
		this.dUpdated = new Date();
	}    

	public String get_szURL() 
	{  return (this.szURL);  } 

	public void set_szURL(String thsURL) 
	{ 
		this.szURL = thsURL;  
		this.dUpdated = new Date();
	}    

	public String get_szUID() 
	{  return (this.szUID);  } 

	public void set_szUID(String thsUID) 
	{ 
		this.szUID = thsUID;  
		this.dUpdated = new Date();
	}    

	public String get_szPSW() 
	{  return (this.szPSW);  } 

	public void set_szPSW(String thsPSW) 
	{ 
		this.szPSW = thsPSW;  
		this.dUpdated = new Date();
	}    
}
