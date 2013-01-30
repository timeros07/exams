package icis.pcz.pl.examsmanagement.client;

public class ExamsController 
{
	private ExamsView view;
	private ExamsModel model;
	
	private EditController editController;
	private RegistrationController regController;
	private ResultsController resultsController;
	
	public ExamsController(ExamsModel model, ExamsView view)
	{
		this.model = model;
		this.view = view;
		this.model.setController(this);
		
		//if(!model.isLogged())
		//	view.redirectToLoginPage();
		//else
			loadOwnExams();
	}
	
	public void loadOwnExams()
	{
		model.loadOwnExams();
	}
	public void showError(String error) 
	{
		view.showError(error);
	}

	public void showExams() 
	{
		view.showExams();
	}
	
	public void setEditController(EditController editController)
	{
		this.editController = editController;
	}
	
	public void setRegController(RegistrationController regController)
	{
		this.regController = regController;
	}
	
	public void setResultsController(ResultsController resultsController)
	{
		this.resultsController = resultsController;
	}

	public void updateEditView(Exam exam)
	{
		editController.getModel().setUserName(model.getUserName());
		editController.getModel().setExam(exam);
		editController.refresh();
	}
	
	public void updateRegView(int id)
	{
		regController.getModel().setExamId(id);
		regController.refresh();
	}
	
	public void updateResultsView(int id)
	{
		resultsController.getModel().setExamId(id);
		resultsController.refresh();
	}

	public void deleteExam(int examId)
	{
		model.deleteExam(examId);
	}

	public void showLogMessage(String message) 
	{
		view.showLogMessage(message);
		
	}
	public Exam getselectedExam()
	{
		return view.getSelectedExam();
	}

	public void refresh()
	{
		view.refresh();
	}

	public void update() 
	{
		model.update();
	}

	public void clearViews()
	{
		editController.getModel().setExam(null);
		editController.refresh();
		
		regController.getModel().setExamId(0);
		regController.refresh();
	}

}
