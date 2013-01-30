package icis.pcz.pl.examsmanagement.client;



import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TabPanel;


public class Examsmanagement implements EntryPoint 
{
	public static final Resources resources = GWT.create(Resources.class);
	HorizontalPanel mainPanel;
	HorizontalPanel innerMainPanel;
	ExamsView examsView;
	TabPanel tabsPanel;
	
	EditView editView;
	RegistrationView regView;
	ResultsView resultsView;
	
	HTML loginLink;
		
	@Override
	public void onModuleLoad() 
	{
		mainPanel = new HorizontalPanel();
		mainPanel.setStyleName("mainPanel");
		RootPanel.get("content").add(mainPanel);
		
		loginLink = new HTML();
		RootPanel.get("login").add(loginLink);
		
		LoginServiceAsync service = GWT.create(LoginService.class);
		service.getUserName(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				if(result.equals(""))
					createlogInLink();
				else
					createlogOutLink(result);
			}
			

			@Override
			public void onFailure(Throwable caught) {
				showError("Wyst¹pi³ b³¹d podczas wczytywanai strony");
			}
		});
	}
	
	public void initAllViews(String userName)
	{
		//mainPanel = new HorizontalPanel();
		innerMainPanel = new HorizontalPanel();
		examsView = new ExamsView(userName);
		tabsPanel = new TabPanel();
		initTabPanel();
		
		mainPanel.add(innerMainPanel);
		
		innerMainPanel.add(examsView);
		innerMainPanel.add(tabsPanel);
		
		
		mainPanel.setCellHorizontalAlignment(innerMainPanel, HorizontalPanel.ALIGN_CENTER);
		//mainPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		//mainPanel.setStyleName("mainPanel");
		tabsPanel.setStyleName("tabsPanel");
		examsView.setStyleName("examsView");
		innerMainPanel.setStyleName("innerMainPanel");
	}
	
	public void initTabPanel()
	{
		editView = new EditView();
		regView = new RegistrationView();
		resultsView = new ResultsView();
		

		examsView.getController().setEditController(editView.getController());
		examsView.getController().setRegController(regView.getController());
		examsView.getController().setResultsController(resultsView.getController());
		
		tabsPanel.add(editView, "Edycja");
		tabsPanel.add(regView, "Rejestracja");
		tabsPanel.add(resultsView, "Wyniki");
		
		tabsPanel.selectTab(0);
	}

	public void showError(String string) 
	{
		HTML logLabel = new HTML(string);
		logLabel.setStyleName("alert alert-error");
		RootPanel.get("content").add(logLabel);
		
	}
	
	private void createlogInLink() 
	{
		LoginServiceAsync service = GWT.create(LoginService.class);
		service.getLoginUrl(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				loginLink.setHTML("<p>" + "<a href=\"" + result + "\">Zaloguj</a></p>");
			}
			

			@Override
			public void onFailure(Throwable caught) {
				showError("Wyst¹pi³ b³¹d podczas wczytywanai strony");
			}
		});
	}
	
	private void createlogOutLink(final String userName) 
	{
		LoginServiceAsync service = GWT.create(LoginService.class);
		service.getLogoutUrl(new AsyncCallback<String>() {
			
			@Override
			public void onSuccess(String result) {
				loginLink.setHTML("<p>" + userName + "<a href=\"" + result + "\"> Wyloguj</a></p>");
				initAllViews(userName);
			}
			

			@Override
			public void onFailure(Throwable caught) {
				showError("Wyst¹pi³ b³¹d podczas wczytywanai strony");
			}
		});
	}

	
}
