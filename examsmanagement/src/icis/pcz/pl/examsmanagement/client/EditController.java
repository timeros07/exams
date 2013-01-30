package icis.pcz.pl.examsmanagement.client;

public class EditController 
{
	private EditView view;
	private EditModel model;
	
	public EditController(EditView view, EditModel model)
	{
		this.view = view;
		this.model = model;
		this.model.setController(this);
	}

	public void refresh()
	{
		view.refresh();
	}

	public EditModel getModel() 
	{
		return this.model;
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
	public void saveChanges() 
	{
		if(validate())
		{
			model.saveChanges(view.getExamName().trim(), view.getExamDescription().trim(),
				Integer.parseInt(view.getExamDuration().trim()));
		}
	}
	
	public void showSuccess(String success)
	{
		view.showSuccess(success);		
	}
	
	public void showError(String error)
	{
		view.showError(error);
	}
	
	public boolean validate() 
	{
		if(view.getExamName().trim().equals("") || view.getExamDescription().trim().equals("") ||
				view.getExamDuration().trim().equals("") )
		{
			view.showError("żadne pole nie może być puste");
			return false;
		}
		else
		{
			if(view.getExamName().length() > 40)
			{
				showError("za długa nazwa testu");
				return false;
			}
			else if(view.getExamDescription().length() > 100)
			{
				showError("za długi opis");
				return false;
			}
			else if(!isNumeric(view.getExamDuration()))
			{
				showError("czas trwania musi być liczbą naturalną");
				return false;
			}
			else if(Integer.parseInt(view.getExamDuration()) >= 300)
			{
				showError("za długi czas trwania");
				return false;
			}
			else if(Integer.parseInt(view.getExamDuration()) <= 0)
			{
				showError("za krótki czas trwania");
				return false;
			}
		}
		return true;
	}
}
