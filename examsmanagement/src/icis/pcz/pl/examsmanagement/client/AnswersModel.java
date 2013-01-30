package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class AnswersModel 
{
	private AnswersView view;
	private Exam exam;
	private String userName;
	private int resultId;
	
	public AnswersModel(int exam_id, String userName, int resultId, AnswersView view)
	{
		this.view = view;
		this.userName = userName;
		this.resultId = resultId;
		loadExamWithAnswers(exam_id);
	}

	private void loadExamWithAnswers(int exam_id) 
	{
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.getExamWithAnswers(exam_id, userName, resultId, new AsyncCallback<Exam>() {
			
			@Override
			public void onSuccess(Exam result)
			{
				if(result != null)
				{
					exam = result;
					view.showResults();
				}
				else
					view.showError("nie znaleziono odpowiedzi");
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				view.showError("nie udało sie załadować odpowiedzi");
			}
		});
	}
	
	public Exam getExam()
	{
		return this.exam;
	}
}
