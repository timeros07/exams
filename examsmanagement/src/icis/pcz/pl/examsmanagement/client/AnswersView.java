package icis.pcz.pl.examsmanagement.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AnswersView extends DialogBox
{
	private VerticalPanel panel; 
	private AnswersModel model;
	private HTML logLabel;
	private Button okButton;
	ScrollPanel scroll;
	
	public AnswersView(int exam_id, String userName, int result_id)
	{
		panel = new VerticalPanel();
		model = new AnswersModel(exam_id, userName, result_id, this);
		init();
	}
	
	public void init()
	{
		panel.setStyleName("answersViewPanel");
		panel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		
		scroll = new ScrollPanel();
		logLabel = new HTML();
		okButton = new Button("ok");
		
		panel.add(scroll);
		scroll.setHeight("500px");
		scroll.setWidth("800px");
		add(panel);
		okButton.setStyleName("btn btn-primary");
		okButton.setWidth("25%");
		okButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event)
			{
				hide();
			}
		});
	}
	
	public void showResults() 
	{
		VerticalPanel resultsPanel = new VerticalPanel();
		resultsPanel.setStyleName("resultsPanel");
		
		for(int i=0 ; i<model.getExam().getQuestionCount() ; i++)
		{
			VerticalPanel questionPanel = new VerticalPanel();
			questionPanel.setStyleName("questionPanel");
			
			HTML questionLabel = new HTML("<h4>Pytanie " + (i+1) + ":  " + model.getExam().getQuestion(i).getContent() + "</h4>");
			//questionLabel.setStyleName("questionLabel");
			
			if(model.getExam().getQuestion(i).checkQuestion())
				questionPanel.add(new Image(Examsmanagement.resources.correct_image()));
			else
				questionPanel.add(new Image(Examsmanagement.resources.incorrect_image()));
			questionPanel.add(questionLabel);
			
			VerticalPanel answersPanel = new VerticalPanel();
			for(int j=0 ; j<model.getExam().getQuestion(i).getAnswerCount() ; j++)
			{
				CheckBox answerBox = new CheckBox();
				answerBox.setHTML(model.getExam().getQuestion(i).getAnswer(j).getContent());
				answerBox.setValue(model.getExam().getQuestion(i).getAnswer(j).isMarked());
				answerBox.setEnabled(false);
				
				if(	model.getExam().getQuestion(i).getAnswer(j).isCorrect())
				{
					answerBox.setStyleName("correctAnswer");
				}
				else if(model.getExam().getQuestion(i).getAnswer(j).isMarked()  && 
						!model.getExam().getQuestion(i).getAnswer(j).isCorrect())
				{
					answerBox.setStyleName("incorrectAnswer");
				}
				answersPanel.add(answerBox);
			}
			questionPanel.add(answersPanel);
			
			resultsPanel.add(questionPanel);
		}
		
		scroll.setWidget(resultsPanel);
		panel.add(okButton);
	}

	public void showError(String error) 
	{
		logLabel.setHTML(error);
		logLabel.setStyleName("alert alert-error");
		logLabel.setText(error);
		panel.add(logLabel);
		panel.add(okButton);
	}
}
