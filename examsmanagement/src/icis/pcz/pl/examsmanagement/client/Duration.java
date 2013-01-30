package icis.pcz.pl.examsmanagement.client;

import java.io.Serializable;

public class Duration implements Serializable
{
	private static final long serialVersionUID = 12121212;
	
	private int minutes;
	private int seconds;
	
	public Duration()
	{
		this(0,0);
	}
	public Duration(int minutes, int seconds)
	{
		this.minutes = minutes;
		this.seconds = seconds;
	}
	public int getMinutes()
	{
		return minutes;
	}
	
	public int getSeconds()
	{
		return seconds;
	}
	
	public void setMinutes(int minutes)
	{
		this.minutes = minutes;
	}
	
	public void setSeconds(int seconds)
	{
		this.seconds = seconds;
	}
}
