package icis.pcz.pl.examsmanagement.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Exam implements Serializable
{
	private static final long serialVersionUID = 34342323;
	
	private int id = -1;
    private String name = "brak nazwy";
    private ArrayList<Question> questionList = new ArrayList<Question>();
    private String description = "brak opisu";
    private Duration duration = new Duration();
    private Status status;
    
    
    public Exam(){}
    public Exam(int id, String name, String description, Duration duration, Status status)
    {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.status = status;
    }
    
    public Question getQuestion(int index)
    {
    	return questionList.get(index);
    }
    
    public int getQuestionCount()
    {
    	return questionList.size();
    }
    
    public String getName()
    {
    	return name;
   	}
    
    public int getId()
    {
    	return id;
    }
    
    public String getDescription()
    {
    	return description;
    }
    
    public Duration getDuration()
    {
    	return duration;
    }
    
    public void setId(int id)
    {
    	this.id = id;
    }
    
    public void setName(String name)
    {
    	this.name = name;
    }
    
    public void setDescription(String description)
    {
    	this.description = description;
    }
    public void setDuration(Duration duration)
    {
    	this.duration = duration;
    }
    
    public void addQuestion(Question question)
    {
    	questionList.add(question);
    }
    
	public int checkExam()
	{
		int result = 0;
		for(int i=0 ; i<getQuestionCount() ; i++)
		{
			if(getQuestion(i).checkQuestion() == true)
				result++;
		}
		return result;
	}
	public String getStatus()
	{
		return status.name();
	}
	public void setStatus(Status s)
	{
		status = s;
	}
}
