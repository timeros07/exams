package icis.pcz.pl.examsmanagement.client;

import java.io.Serializable;

public class Answer implements Serializable
{
	private static final long serialVersionUID = 123;
	private String content = "brak tresci";
    private boolean correct = false;
    private boolean marked = false;
    
    public Answer(){}
    public Answer(String content, boolean correct)
    {
        this.content = content;
        this.correct = correct;
    }
    
    public String getContent()
    {
        return content;
    }
    public boolean isCorrect()
    {
        return correct;
    }

	public boolean isMarked() 
	{
		return marked;
	}

	public void setMarked(boolean marked)
	{
		this.marked = marked;
	}
}
