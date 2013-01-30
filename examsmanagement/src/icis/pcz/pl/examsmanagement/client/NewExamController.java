package icis.pcz.pl.examsmanagement.client;


public class NewExamController
{
	private NewExamView view;
	
	public NewExamController(NewExamView view)
	{
		this.view = view;
	}

	public boolean validate() 
	{
		if(view.getExamName().trim().equals("") || view.getExamDescription().trim().equals("") ||
				view.getExamDuration().trim().equals("") || view.getFileName().equals(""))
		{
			view.showLogMessage("żadne pole nie może być puste");
			return false;
		}
		else
		{
			if(view.getExamName().length() > 40)
			{
				view.showLogMessage("za długa nazwa testu");
				return false;
			}
			else if(view.getExamDescription().length() > 100)
			{
				view.showLogMessage("za długi opis");
				return false;
			}
			else if(!isNumeric(view.getExamDuration()))
			{
				view.showLogMessage("czas trwania musi być liczbą naturalną");
				return false;
			}
			else if(Integer.parseInt(view.getExamDuration()) >= 300)
			{
				view.showLogMessage("za długi czas trwania");
				return false;
			}
			else if(Integer.parseInt(view.getExamDuration()) <= 0)
			{
				view.showLogMessage("za krótki czas trwania");
				return false;
			}
			else if(!view.getFileName().substring(view.getFileName().length()-4).equals(".xml"))
			{
				view.showLogMessage("niepoprawny format pliku");
				return false;
			}
		}
		return true;
	}
	
	private boolean isNumeric(String string)
	{
		boolean onlyNumeric = true;
		for(int i=0;i<string.length();++i)
		{
			if(string.charAt(i)<'0'|| string.charAt(i)>'9')
		    {
				onlyNumeric=false;
		    } 
		}
		return onlyNumeric;
	}
}
