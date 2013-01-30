package icis.pcz.pl.examsmanagement.client;

import java.io.Serializable;

public class Result implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private String Username;
	private int result;
	private String date;
	private int resultId;
	private int questionCount;
	
	public Result(){}
	
	public Result(int resultId, int result, String username, String date, int questionCount)
	{
		this.resultId = resultId;
		this.result = result;
		this.Username = username;
		this.date = date;
		this.questionCount = questionCount;
	}
	
	public int getResult()
	{
		return result;
	}
	
	public int getQuestionCount()
	{
		return this.questionCount;
	}
	
	public String getDate()
	{
		return date;
	}
	public String getUserName() 
	{
		return Username;
	}
	
	public int getResultId()
	{
		return resultId;
	}
	
}
