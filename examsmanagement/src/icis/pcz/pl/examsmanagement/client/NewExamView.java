package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;
import com.google.gwt.user.client.ui.FormPanel.SubmitEvent;

public class NewExamView extends DialogBox
{
	private VerticalPanel panel;
	private NewExamController controller;
	
	private TextBox nameTextBox;
	private TextBox descTextBox;
	private TextBox durationTextBox;
	private Button createButton;
	private Button cancelButton;
	private FileUpload examUpload;
	private FormPanel form;
	private HTML logLabel;
	private TextBox userNameHidden;
	private ExamsController examsController;
	
	public NewExamView(String userName, ExamsController examsController)
	{
		panel = new VerticalPanel();
		form = new FormPanel();
		controller = new NewExamController(this);
		this.examsController = examsController;
		
		nameTextBox = new TextBox();
		descTextBox = new TextBox();
		durationTextBox = new TextBox();
		createButton = new Button("Stwórz");
		cancelButton = new Button("Anuluj");
		examUpload = new FileUpload();
		logLabel = new HTML();
		userNameHidden = new TextBox();
		
		userNameHidden.setText(userName);
		userNameHidden.setVisible(false);
		
		nameTextBox.setName("name");
		descTextBox.setName("description");
		durationTextBox.setName("duration");
		examUpload.setName("examFile");
		userNameHidden.setName("userName");
		
		add(form);
		
		addStyleName("newExamView");
 		panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		init();
		initFileUpload();
 		form.setWidget(panel);
	}

	private void initFileUpload() 
	{
		form.setAction(GWT.getModuleBaseURL() + "examUpload");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);

		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			
			@Override
			public void onSubmitComplete(SubmitCompleteEvent event)
			{
				if(event.getResults().substring(event.getResults().indexOf(">")+1, event.getResults().indexOf("</pre>")).equals("ok"))
				{
					Window.alert("test został dodany");
					hide();

					examsController.update();
					
					examsController.refresh();
					examsController.clearViews();
				}
				else
				{
					showLogMessage("nie udało się zapisać testu");
					enableButtons();
				}
			}
		});
		
		form.addSubmitHandler(new FormPanel.SubmitHandler() {
			
			@Override
			public void onSubmit(SubmitEvent event)
			{
				if(!controller.validate())
					event.cancel();
				else
					disableButtons();
			}
		});
	}

	private void init()
	{
	
		setTitle("Tworzenie nowego testu");
		setText("Tworzenie nowego testu");
		setAnimationEnabled(true);
		
		Label nameLabel = new Label("Nazwa: ");
		nameLabel.setStyleName("label label-info");
		Label descLabel = new Label("Opis: ");
		descLabel.setStyleName("label label-info");
		Label durationLabel = new Label("Czas trwania(w minutach): ");
		durationLabel.setStyleName("label label-info");
		Label fileLabel = new Label("Plik z pytaniami: ");
		fileLabel.setStyleName("label label-info");
		
		panel.add(nameLabel);
		panel.add(nameTextBox);
		panel.add(descLabel);
		panel.add(descTextBox);
		panel.add(durationLabel);
		panel.add(durationTextBox);
		panel.add(fileLabel);
		panel.add(userNameHidden);
		panel.add(examUpload);

		
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(createButton);
		buttonsPanel.add(cancelButton);
		
		createButton.setStyleName("btn btn-primary");
		cancelButton.setStyleName("btn btn-primary");
		
		panel.add(buttonsPanel);
		panel.add(logLabel);
		logLabel.setVisible(false);
		
		createButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				form.submit();
			}

		});
		
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event)
			{
				hide();
			}
		});
		
	}
	private void disableButtons() 
	{
		cancelButton.setEnabled(false);
		createButton.setEnabled(false);
	}
	private void enableButtons() 
	{
		cancelButton.setEnabled(true);
		createButton.setEnabled(true);
	}
	
	public void showLogMessage(String message)
	{
		logLabel.setVisible(true);
		logLabel.setText(message);
		logLabel.setStyleName("alert alert-error");
	}
	
	public String getExamName()
	{
		return nameTextBox.getText();
	}
	
	public String getExamDescription()
	{
		return descTextBox.getText();
	}
	
	public String getExamDuration()
	{
		return durationTextBox.getText();
	}
	
	public String getFileName()
	{
		return examUpload.getFilename();
	}
}
