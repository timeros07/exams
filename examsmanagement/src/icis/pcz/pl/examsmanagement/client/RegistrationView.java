package icis.pcz.pl.examsmanagement.client;

import java.util.List;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.StyleInjector;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.cellview.client.CellTree.Resources;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.gwt.view.client.TreeViewModel;


public class RegistrationView extends Composite
{
	private HorizontalPanel panel;
	private RegistrationModel model;
	private RegistrationController controller;
	
	private CellTree allUsersCellTree;
	private CellTable<User> regUsersCelltable;
	private Button addUserButton;
	private Button addGroupButton;
	private Button removeUserButton;
	private Button activateAllButton;
	private Button deactivateAllButton;
	
	private SingleSelectionModel<String> allUsersSelectionModel;
	private SingleSelectionModel<Group> allGroupsSelectionModel;
	private SingleSelectionModel<User> regUsersSelectionModel;
	private ListDataProvider<User> regUserDataProvider;
	private HTML logLabel;
	
	private ScrollPanel allUsersScroll;
	
	public RegistrationView()
	{
		model = new RegistrationModel();
		controller = new RegistrationController(this, model);
		panel = new HorizontalPanel();
		initWidget(panel);
		
		regUsersCelltable = new CellTable<User>();
		addUserButton = new Button("Dodaj użytkownika");
		addGroupButton = new Button("Dodaj grupę");
		removeUserButton = new Button("Usuń");
		removeUserButton.setEnabled(false);
		
		activateAllButton = new Button("aktywuj wszystkich");
		deactivateAllButton = new Button("dezaktywuj wszystkich");
		
		logLabel = new HTML();
		
		setStyleName("registrationView");
		
		init();
		
		showAllUsers();
		showRegUsers();
	}

	private void init() 
	{
		initRegUsersPanel();
		initAllUsersPanel();
		
		VerticalPanel allUsersPanel = new VerticalPanel();
		allUsersPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		HTML allUserPanel = new HTML("<h5>Wszyscy użytkownicy systemu:</h5><br/>");
		
		allUsersPanel.add(allUserPanel);
		allUsersScroll = new ScrollPanel();
		allUsersScroll.setSize("240px","400px");
		allUsersPanel.add(allUsersScroll);
		allUsersPanel.add(addUserButton);
		allUsersPanel.add(addGroupButton);
		panel.add(allUsersPanel);
		
		
		VerticalPanel regUsersPanel = new VerticalPanel();
		regUsersPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		HTML regUserPanel = new HTML("<h5>Użytkownicy zapisani na test:<h5><br/>");
		
		regUsersPanel.add(regUserPanel);
		ScrollPanel regUsersScroll = new ScrollPanel();
		regUsersScroll.setWidget(regUsersCelltable);
		regUsersScroll.setHeight("400px");
		regUsersPanel.add(regUsersScroll);
		
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.setStyleName("buttonsPanel");
		removeUserButton.setStyleName("btn btn-danger");
		activateAllButton.setStyleName("btn btn-primary");
		deactivateAllButton.setStyleName("btn btn-primary");
		
		buttonsPanel.add(removeUserButton);
		buttonsPanel.add(activateAllButton);
		buttonsPanel.add(deactivateAllButton);
		
		regUsersPanel.add(buttonsPanel);
		panel.add(regUsersPanel);
		regUsersPanel.add(logLabel);		
		logLabel.setVisible(false);
		
		addUserButton.setEnabled(false);
		addGroupButton.setEnabled(false);
	}

	private void initAllUsersPanel()
	{
		allUsersSelectionModel = new SingleSelectionModel<String>();
		
		allUsersSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event)
			{
				if(allUsersSelectionModel.getSelectedObject() != null)
				{
					addUserButton.setEnabled(true);
					logLabel.setVisible(false);
				}
				else
					addUserButton.setEnabled(false);
			}
		});
		
		allGroupsSelectionModel = new SingleSelectionModel<Group>();
		
		allGroupsSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event) 
			{
				if(allGroupsSelectionModel.getSelectedObject() != null)
				{
					addGroupButton.setEnabled(true);
					logLabel.setVisible(false);
				}
				else
					addGroupButton.setEnabled(false);
			}
		});
		
		
		addUserButton.setStyleName("btn btn-primary");
		addUserButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				if(allUsersSelectionModel.getSelectedObject() != null)
					controller.addUserToExam(allUsersSelectionModel.getSelectedObject());
			}
		});
		
		addGroupButton.setStyleName("btn btn-primary");
		addGroupButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				if(allGroupsSelectionModel.getSelectedObject() != null)
					controller.addGroupToExam(allGroupsSelectionModel.getSelectedObject());
			}
		});
	}
	
	private void initRegUsersPanel() 
	{
		Column<User, ImageResource> imageColumn = new Column<User, ImageResource>(new ImageResourceCell()) {
			
			@Override
			public ImageResource getValue(User test)
			{
				return Examsmanagement.resources.person_image();
			}
		}; 

		TextColumn<User> regNameColumn = new TextColumn<User>(){
			@Override
			public String getValue(User value)
			{
				return value.getName();
			}
		};
		
		TextColumn<User> statusColumn = new TextColumn<User>(){
			@Override
			public String getValue(User value)
			{
				return value.getStatus().name();
			}
		};
		
		CellTableButton cellButton = new CellTableButton();
		
		Column<User, String> activateColumn = new Column<User, String>(cellButton)
		{
			@Override
			public String getValue(User object)
			{
				if(object.getStatus() == Status.w_trakcie)
				{
					return null;
				}
				else if(object.getStatus() == Status.aktywny)
				{
					return "dezaktywuj";
				}
				return "aktywuj";
			}
		};
		
		activateColumn.setFieldUpdater(new FieldUpdater<User, String>() {
			
			@Override
			public void update(int index, User object, String value)
			{
				if(value != null)
				{
					if(value.equals("aktywuj"))
					{
						object.setStatus(Status.aktywny);
						model.changeUserRegisterStatus(object.getName(), object.getStatus());
					}
					else if(value.equals("dezaktywuj"))
					{
						object.setStatus(Status.nieaktywny);
						model.changeUserRegisterStatus(object.getName(), object.getStatus());
					}
				}
			}
		});
		
		regUsersCelltable.setVisibleRange(0, 999);
		
		regUsersCelltable.addColumn(imageColumn);
		regUsersCelltable.addColumn(regNameColumn);
		regUsersCelltable.addColumn(statusColumn);
		regUsersCelltable.addColumn(activateColumn);
		
		
		regUsersSelectionModel = new SingleSelectionModel<User>();
		regUsersCelltable.setSelectionModel(regUsersSelectionModel);
		
		regUsersSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
			
			@Override
			public void onSelectionChange(SelectionChangeEvent event)
			{
				if(regUsersSelectionModel.getSelectedObject() != null && 
						regUsersSelectionModel.getSelectedObject().getStatus() != Status.w_trakcie)
					removeUserButton.setEnabled(true);
				else
					removeUserButton.setEnabled(false);
				
			}
		});
		
		removeUserButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event)
			{
				if(regUsersSelectionModel.getSelectedObject() != null)
					controller.removeUserFromExam(regUsersSelectionModel.getSelectedObject().getName());
			}
		});
		
		activateAllButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				controller.changeAllUsersRegisterStatus(Status.aktywny);
			}
		});
		
		deactivateAllButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				controller.changeAllUsersRegisterStatus(Status.nieaktywny);
			}
		});
		
	}

	public void showAllUsers()
	{
		
		UsersTreeModel treeModel = new UsersTreeModel(model.getAllUsersList(),
				allUsersSelectionModel, allGroupsSelectionModel);
	    Resources resource = GWT.create(Resources.class);
	    StyleInjector.injectAtEnd("."+resource.cellTreeStyle().cellTreeTopItem()+" {margin-top: 0px;}"); // Not the best code
	    allUsersCellTree = new CellTree(treeModel, null,resource);
		
		allUsersScroll.setWidget(allUsersCellTree);
		
	}

	public void showRegUsers() 
	{
		regUserDataProvider = new ListDataProvider<User>();
		regUserDataProvider.addDataDisplay(regUsersCelltable);
		regUserDataProvider.setList(model.getRegUsersList());
		
		if(model.getExamId() == 0)
			regUsersCelltable.setVisible(false);
		else
		{
			regUsersCelltable.setVisible(true);
			
			activateAllButton.setEnabled(false);
			deactivateAllButton.setEnabled(false);
			
			for(User u : model.getRegUsersList())
			{
				if(u.getStatus() == Status.nieaktywny || u.getStatus() == Status.zakonczony)
					activateAllButton.setEnabled(true);
				if(u.getStatus() == Status.aktywny)
					deactivateAllButton.setEnabled(true);
			}
		}
		
	}

	public void diselectTables()
	{
		allUsersSelectionModel.setSelected(allUsersSelectionModel.getSelectedObject(), false);
		regUsersSelectionModel.setSelected(regUsersSelectionModel.getSelectedObject(), false);
		addUserButton.setEnabled(false);
		addGroupButton.setEnabled(false);
	}
	
	public RegistrationController getController()
	{
		return this.controller;
	}

	public void showError(String error)
	{
		logLabel.setVisible(true);
		logLabel.setText(error);
		logLabel.setStyleName("alert alert-error");
	}
	public void showLogMessage(String message) 
	{
		logLabel.setVisible(true);
		logLabel.setText(message);
		logLabel.setStyleName("alert alert-success");		
	}
	
	class CellTableButton extends ButtonCell
	{
		@Override
		public void render(com.google.gwt.cell.client.Cell.Context context,	SafeHtml data, SafeHtmlBuilder sb) 
		{
			SafeHtml html;
			if(data != null)
			{
				html = SafeHtmlUtils.fromTrustedString("<div class=\"btn  btn-small \">" + data.asString() + "</div>");
			}
			else
			{
				html = SafeHtmlUtils.fromTrustedString("<img src=\"" +
					Examsmanagement.resources.alert_image().getSafeUri().asString() + "\">");
			}
	        sb.append(html);
		}
	}

	public void clearLogLabel()
	{
		logLabel.setVisible(false);
	}
	
	private static class UsersTreeModel implements TreeViewModel 
	{
		private final List<Group> groups;

	    private SingleSelectionModel<String> userSelectionModel;;
	    private SingleSelectionModel<Group> groupSelectionModel; 
	    public UsersTreeModel(List<Group> groups, SingleSelectionModel<String> _userSelectionModel
	    		, SingleSelectionModel<Group> _groupSelectionModel) 
	    {
	    	this.groups = groups;
	    	
	    	this.userSelectionModel = _userSelectionModel;
	    	this.groupSelectionModel = _groupSelectionModel;
	    	
	    	groupSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				@Override
				public void onSelectionChange(SelectionChangeEvent event) 
				{
					if(groupSelectionModel.getSelectedObject() != null)
					{
						userSelectionModel.setSelected(userSelectionModel.getSelectedObject(), false);
					}
				}
			});
	    	
	    	userSelectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
				
				@Override
				public void onSelectionChange(SelectionChangeEvent event) 
				{
					if(userSelectionModel.getSelectedObject() != null)
					{
						groupSelectionModel.setSelected(groupSelectionModel.getSelectedObject(), false);
					}
				}
			});
	    	
	    }
	    public <T> NodeInfo<?> getNodeInfo(T value)
	    {
	    	if (value == null)
	    	{
	    		ListDataProvider<Group> dataProvider = new ListDataProvider<Group>(groups);
	    		Cell<Group> cell = new AbstractCell<Group>()
	    		{
		        @Override
		        public void render(Context context, Group value, SafeHtmlBuilder sb)
		        {
		        	if (value != null) 
		        	{
		             	sb.appendEscaped(value.getName());
		            }
		        }};
		        return new DefaultNodeInfo<Group>(dataProvider, cell, groupSelectionModel,null);
	    	} 
		    else if (value instanceof Group)
		    {
		        ListDataProvider<String> dataProvider = new ListDataProvider<String>(((Group) value).getUserNames());
		        return new DefaultNodeInfo<String>(dataProvider, new TextCell(), userSelectionModel, null);
		    }
	
	    	return null;
    }

    public boolean isLeaf(Object value) 
    {
    	// The leaf nodes are the users, which are Strings.
	    if (value instanceof String) 
	    {
	    	return true;
	    }
	    return false;
    }

  }
}
