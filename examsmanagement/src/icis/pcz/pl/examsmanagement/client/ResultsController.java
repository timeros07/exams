package icis.pcz.pl.examsmanagement.client;

public class ResultsController 
{
	private ResultsView view;
	private ResultsModel model;
	
	public ResultsController(ResultsView view, ResultsModel model)
	{
		this.view = view;
		this.model = model;
		model.setController(this);
	}
	
	public ResultsModel getModel()
	{
		return model;
	}

	public void refresh()
	{
		view.clearLogLabel();
		model.update();
	}

	public void showLogMessage(String string)
	{
		view.showLogMessage(string);
	}
	
	public void showError(String error)
	{
		view.showError(error);
	}

	public void showResults() 
	{
		view.showResults();
	}

}
