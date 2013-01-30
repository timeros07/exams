package icis.pcz.pl.examsmanagement.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Group implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<String> userNames;
	private String name;
	
	public Group()
	{
		userNames = new ArrayList<String>();
		name = "brak nazwy";
	}
	
	public Group(String name)
	{
		this.name = name;
		userNames = new ArrayList<String>();
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getUserName(int index)
	{
		return userNames.get(index);
	}
	
	public int getUsersCount()
	{
		return userNames.size();
	}
	
	public void addUserName(String userName)
	{
		userNames.add(userName);
	}
	
	public List<String> getUserNames()
	{
		return userNames;
	}
	
	public boolean contain(String name)
	{
		for(String userName : userNames)
		{
			if(userName.equals(name))
			{
				return true;
			}
		}
		return false;
	}
}
