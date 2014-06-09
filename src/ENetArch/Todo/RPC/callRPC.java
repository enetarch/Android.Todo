/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ENetArch.Todo.RPC;

/**
 *
 * @author mfuhrman
 */
public class callRPC
{
	public String szURL = null;
	public String szCommand = "";
	public String szParams = "";
	
	public callRPC (String thsURL, String thsCommand, String thsParams)
	{
		szURL = thsURL;
		szCommand = thsCommand;
		szParams = thsParams;
	}
			 
}
