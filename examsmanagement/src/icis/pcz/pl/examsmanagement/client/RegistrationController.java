package icis.pcz.pl.examsmanagement.client;

import java.util.ArrayList;
import java.util.List;

public class RegistrationController
{
	private RegistrationModel model;
	private RegistrationView view;
	
	public RegistrationController(RegistrationView view, RegistrationModel model)
	{
		this.view = view;
		this.model = model;
		model.setController(this);
	}

	public void showAllUsers()
	{
		view.showAllUsers();
	}

	public void showRegUsers() 
	{
		view.showRegUsers();
	}

	public RegistrationModel getModel()
	{
		return this.model;
	}

	public void showError(String string)
	{
		view.showError(string);
	}

	public void removeUserFromExam(String userName) 
	{
		model.removeUserFromExam(userName);
		view.diselectTables();
	}

	public void addUserToExam(String userName) 
	{
		if(model.containUser(userName))
		{
			showError("użytkownik jest już zapisany na test");
		}
		else
		{
			model.addUserToExam(userName);
			view.diselectTables();
		}
	}
	
	public void addGroupToExam(Group group) 
	{
		Group g = new Group();
		for(String name : group.getUserNames())
		{
			if(!model.containUser(name))
			{
				g.addUserName(name);
			}
		}
		if(g.getUsersCount() <= 0)
		{
			showError("grupa jest już zapisana na test");
		}
		else
		{
			model.addGroupToExam(g);
			view.diselectTables();
		}
	}
	
	public void refresh()
	{
		view.clearLogLabel();
		if(model.getExamId() != 0)
			model.update();
		else
		{
			showAllUsers();
			showRegUsers();
		}
	}
	
	public void showLogMessage(String message)
	{
		view.showLogMessage(message);
	}

	public void changeUserRegisterStatus(String userName, Status status) 
	{
		model.changeUserRegisterStatus(userName, status);
	}

	public void changeAllUsersRegisterStatus(Status status)
	{
		List<String> users = new ArrayList<String>();
		for(User u : model.getRegUsersList())
		{
			if(u.getStatus() != Status.w_trakcie)
			{
				users.add(u.getName());
			}
		}
		String table[] = new String[users.size()];
		for(int i=0 ; i<users.size() ; i++)
			table[i] = users.get(i);
		model.changeAllUsersRegisterStatus(table, status);
	}

	
}
