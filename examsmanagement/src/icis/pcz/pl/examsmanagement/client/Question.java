package icis.pcz.pl.examsmanagement.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Question implements Serializable
{
	private static final long serialVersionUID = 231;
	private String content = "brak tresci";
    private ArrayList<Answer> answerList = new ArrayList<Answer>();
    
    public Question(){answerList = new ArrayList<Answer>();}
    public Question(String content)
    {
    	answerList = new ArrayList<Answer>();
        this.content = content;
    }
    
    public Answer getAnswer(int index)
    {
        return answerList.get(index);
    }
    
    public int getAnswerCount(){return answerList.size();}
    
    public String getContent()
    {
        return content;
    }
    
    public void setContent(String content)
    {
    	this.content = content;
    }
    public void addAnswer(String content, boolean correct)
    {
        answerList.add(new Answer(content, correct));
    }
    
    public boolean checkQuestion()
    {
    	for(int i=0 ; i<getAnswerCount() ; i++)
    	{
    		if(getAnswer(i).isCorrect() != getAnswer(i).isMarked())
    			return false;
    			
    	}
    	return true;
    }
}
