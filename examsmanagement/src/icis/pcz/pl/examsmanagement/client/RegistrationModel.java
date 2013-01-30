package icis.pcz.pl.examsmanagement.client;

import java.util.ArrayList;
import java.util.List;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RegistrationModel
{
	private RegistrationController controller;
	private int examId;
	private List<Group> allUsersNames;
	private List<User> regUsersNames;
	
	public RegistrationModel()
	{
		allUsersNames = new ArrayList<Group>();
		regUsersNames = new ArrayList<User>();
	}
	
	public void setController(RegistrationController controller) 
	{
		this.controller = controller;
	}
	
	public List<Group> getAllUsersList()
	{
		return allUsersNames;
	}
	
	public List<User> getRegUsersList()
	{
		return regUsersNames;
	}
	
	public void setExamId(int id)
	{
		this.examId = id;
	}
	
	public int getExamId()
	{
		return this.examId;
	}

	public void update()
	{
		regUsersNames = new ArrayList<User>();
		allUsersNames = new ArrayList<Group>();

		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.getAllUsers(new AsyncCallback<Group[]>() {
			
			@Override
			public void onSuccess(Group[] result)
			{
				if(result != null)
				{
					for(int i=0 ; i<result.length ; i++)
						allUsersNames.add(result[i]);
					controller.showAllUsers();
				}
				else
				{
					controller.showError("nie znaleziono żadnych użytkowników");
				}
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				controller.showError("nie udało się pobrać uzytowników");
			}
		});
		
		service = GWT.create(ExamsManagementService.class);
		service.getRegisteredUsers(examId, new AsyncCallback<User[]>() {
			
			@Override
			public void onSuccess(User[] result) 
			{
				if(result != null)
				{
					for(int i=0 ; i<result.length ; i++)
						regUsersNames.add(result[i]);
					controller.showRegUsers();
				}
				else
				{
					controller.showRegUsers();
				}
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				controller.showError("nie udało się pobrać uzytkowników zapisanych na test");
			}
		});
		
		
	}
	
	public void addUserToExam(String userName)
	{
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.registerUser(userName, examId, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) 
			{
				if(result)
				{
					controller.showLogMessage("użytkownik został dodany do testu");
					update();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) 
			{
				controller.showLogMessage("nie udało się dodać użytkownika");
			}
		});
		

	}
	
	public void removeUserFromExam(String userName)
	{
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.unRegisterUser(userName, examId, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) 
			{
				if(result)
				{
					controller.showLogMessage("użytkownik został usunięty");
					update();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) 
			{
				controller.showLogMessage("nie udało się usunąć użytkownika");
			}
		});
	}
	
	public void addGroupToExam(Group group)
	{
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.registerGroup(group, examId, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) 
			{
				if(result)
				{
					controller.showLogMessage("grupa została dodana do testu");
					update();
				}
			}
			
			@Override
			public void onFailure(Throwable caught) 
			{
				controller.showLogMessage("nie udało się dodać grupy");
			}
		});
	}
	public List<String> getAvailableStatus()
	{
		List<String> table = new ArrayList<String>();
		table.add(Status.aktywny.name());
		table.add(Status.nieaktywny.name());
		table.add(Status.zakonczony.name());
		table.add(Status.w_trakcie.name());
		
		return table;
	}

	public void changeUserRegisterStatus(String userName, Status status)
	{
		String users[] = {userName};
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.changeRegisterStatus(users, examId, status, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result)
			{
				controller.showLogMessage("status został zmieniony");
				update();
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				controller.showError("nie udało sie zmienić statusu użytkownika");
			}
		});
	}
	
	public void changeAllUsersRegisterStatus(String userNames[], Status status)
	{
		ExamsManagementServiceAsync service = GWT.create(ExamsManagementService.class);
		service.changeRegisterStatus(userNames, examId, status, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result)
			{
				controller.showLogMessage("status wszystkich użytkowników został zmieniony");
				update();
			}
			
			@Override
			public void onFailure(Throwable caught)
			{
				controller.showError("nie udało sie zmienić statusu wszystkich użytkowników");
			}
		});
	}
	
	public boolean containUser(String userName)
	{
		for (User user : regUsersNames) 
		{
			if(user.getName().equals(userName))
			{
				return true;
			}
		}
		return false;
	}

}
