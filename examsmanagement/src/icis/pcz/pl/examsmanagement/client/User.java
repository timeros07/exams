package icis.pcz.pl.examsmanagement.client;

import java.io.Serializable;

public class User  implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String name;
	private Status registerStatus;
	
	public User(){}
	public User(String name, Status status)
	{
		this.name = name;
		this.registerStatus = status;
	}
	
	public String getName() 
	{
		return name;
	}
	public Status getStatus() 
	{
		return registerStatus;
	}
	
	public void setStatus(Status status)
	{
		this.registerStatus = status;
	}
}
