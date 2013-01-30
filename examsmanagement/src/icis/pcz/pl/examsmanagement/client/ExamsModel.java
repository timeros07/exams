package icis.pcz.pl.examsmanagement.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ExamsModel 
{
	private String userName;
	private List<Exam> examList; 
	private ExamsController controller = null;
	
	public ExamsModel(String userName)
	{
		this.userName = userName;
		//loadUserName();
		examList = new ArrayList<Exam>();
	}

	public void setController(ExamsController controller) 
	{
		this.controller = controller;
	}

	public String getUserName()
	{
		return userName;
	}
	
	public List<Exam> getExamList()
	{
		return examList;
	}
	
	public void loadOwnExams()
	{
		
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		
		//ServiceDefTarget serviceDefTarget = (ServiceDefTarget) service;
		//Window.alert(serviceDefTarget.getServiceEntryPoint());
		//serviceDefTarget.setServiceEntryPoint("http://127.0.0.1:8080/examsManagement/?examsmanagement/examsManagement");

		service.getOwnExams(userName, new AsyncCallback<Exam[]>() {
		
			@Override
			public void onFailure(Throwable caught) 
			{
				controller.showError("nie udało się pobrać testów");
			}

			@Override
			public void onSuccess(Exam[] result)
			{
				if(result != null)
				{
					for(int i=0 ; i<result.length ; i++)
						examList.add(result[i]);
					controller.showExams();
				}
				else
				{
					controller.showError("nie stworzyłeś żadnych egzaminów");
					controller.showExams();
				}
			}
		});
	}

	public void deleteExam(int exam_id) 
	{
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.deleteExam(exam_id, userName, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) 
			{
				examList.remove(controller.getselectedExam());
				controller.refresh();
				controller.showLogMessage("test usunięty");
				controller.clearViews();
			}
			
			@Override
			public void onFailure(Throwable caught) 
			{
				controller.showError("nie udało sie usunąć testu");
			}
		});
	}

	public void update() 
	{
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.getOwnExams(userName, new AsyncCallback<Exam[]>() {

			@Override
			public void onFailure(Throwable caught) 
			{
				controller.showError("nie udało się pobrać testów");			
			}

			@Override
			public void onSuccess(Exam[] result)
			{
				if(result != null)
				{
					examList = new ArrayList<Exam>();
					
					for(int i=0 ; i<result.length ; i++)
						examList.add(result[i]);
					controller.refresh();
				}
				else
				{
					controller.showError("nie stworzyłeś żadnych egzaminów");
				}
			}
		});
	}
}
