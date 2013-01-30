package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditView extends Composite
{
	private VerticalPanel panel;
	private EditModel model;
	private EditController controller;
	
	private TextBox nameTextBox;
	private TextBox descTextBox;
	private TextBox durationTextBox;
	private Button saveButton;
	private Button cancelButton;
	private HTML logLabel;
	
	public EditView()
	{
		model = new EditModel();
		controller = new EditController(this, model);
		panel = new VerticalPanel();
		initWidget(panel);
		
		nameTextBox = new TextBox();
		descTextBox = new TextBox();
		durationTextBox = new TextBox();
		saveButton = new Button("Zapisz");
		cancelButton = new Button("Anuluj");

		logLabel = new HTML();
		
		setStyleName("editView");
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		init();
	}
	
	private void init() 
	{
		Label nameLabel = new Label("Nazwa: ");
		nameLabel.setStyleName("label label-info");
		nameLabel.setWidth("200px");
		Label descLabel = new Label("Opis: ");
		descLabel.setStyleName("label label-info");
		descLabel.setWidth("200px");
		Label durationLabel = new Label("Czas trwania(w minutach): ");
		durationLabel.setStyleName("label label-info");
		durationLabel.setWidth("200px");
		
		panel.add(nameLabel);
		panel.add(nameTextBox);
		panel.add(descLabel);
		panel.add(descTextBox);
		panel.add(durationLabel);
		panel.add(durationTextBox);
		
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(saveButton);
		buttonsPanel.add(cancelButton);
		
		saveButton.setStyleName("btn btn-primary");
		cancelButton.setStyleName("btn btn-primary");
		
		panel.add(new HTML("</br>"));
		panel.add(buttonsPanel);
		panel.add(logLabel);
		
		logLabel.setVisible(false);
		
		nameTextBox.setEnabled(false);
		descTextBox.setEnabled(false);
		durationTextBox.setEnabled(false);
		
		saveButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				controller.saveChanges();
			}
		});
		
		cancelButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event)
			{
				refresh();
			}
		});
		
	}

	public void refresh()
	{
		if(model.getExam() != null)
		{
			nameTextBox.setText(model.getExam().getName());
			descTextBox.setText(model.getExam().getDescription());
			durationTextBox.setText(Integer.toString(model.getExam().getDuration().getMinutes()));
			
			nameTextBox.setEnabled(true);
			descTextBox.setEnabled(true);
			durationTextBox.setEnabled(true);
		}
		else
		{
			nameTextBox.setText("");
			descTextBox.setText("");
			durationTextBox.setText("");
			
			nameTextBox.setEnabled(false);
			descTextBox.setEnabled(false);
			durationTextBox.setEnabled(false);
		}
		logLabel.setVisible(false);
	}

	public EditController getController() 
	{
		return this.controller;
	}

	public void showError(String error)
	{
		logLabel.setVisible(true);
		logLabel.setHTML(error);
		logLabel.setStyleName("alert alert-error");
	}
	public void showSuccess(String success)
	{
		logLabel.setVisible(true);
		logLabel.setHTML(success);
		logLabel.setStyleName("alert alert-success");
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

}
