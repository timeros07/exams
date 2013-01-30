package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class EditModel 
{
	private EditController controller;
	private Exam exam;
	private String userName;
	
	
	public EditModel()
	{
	}
	
	public Exam getExam()
	{
		return this.exam;
	}
	
	public void setExam(Exam exam)
	{
		this.exam = exam;
	}
	
	public void setController(EditController controller)
	{
		this.controller = controller;
	}

	public void saveChanges(String name, String description, int duration) 
	{
		exam.setName(name);
		exam.setDescription(description);
		exam.setDuration(new Duration(duration, 0));
		
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.editExam(exam, userName, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result)
			{
				controller.showSuccess("zmiany zostały zapisane");			
			}
			
			@Override
			public void onFailure(Throwable caught) 
			{
				controller.showError("nie udało sie zapisać danych");
			}
		});
		
	}
	
	public void setUserName(String userName)
	{
		this.userName = userName;
	}

}
