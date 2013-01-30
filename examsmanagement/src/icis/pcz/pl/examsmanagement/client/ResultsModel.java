package icis.pcz.pl.examsmanagement.client;

import java.util.ArrayList;
import java.util.List;



import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ResultsModel 
{
	private ResultsController controller;
	private List<Result> resultsList;
	private int examId;
	private int questionCount;
	
	public ResultsModel()
	{
		resultsList = new ArrayList<Result>();
	}
	
	public void setController(ResultsController controller)
	{
		this.controller = controller;
	}
	
	public List<Result> getResultsList() 
	{
		return resultsList;
	}

	public void setExamId(int id)
	{
		examId = id;
	}
	
	public void setQuestionCount(int questionCount)
	{
		this.questionCount = questionCount;
	}

	public void update()
	{
		resultsList = new ArrayList<Result>();
		
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.getResults(examId, new AsyncCallback<Result[]>() {
			
			@Override
			public void onSuccess(Result[] result)
			{
				if(result != null)
				{
					for(int i=0 ; i<result.length ; i++)
						resultsList.add(result[i]);
				}
				controller.showResults();
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				controller.showError("nie udało się pobrać wyników");
			}
		});
	}

	public int getExamId() 
	{
		return examId;
	}
	
	public int getQuestionCount() 
	{
		return questionCount;
	}


}
