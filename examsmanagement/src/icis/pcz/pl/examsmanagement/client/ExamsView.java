package icis.pcz.pl.examsmanagement.client;


import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class ExamsView extends Composite
{
	private ExamsModel model;
	private ExamsController controller;
	private VerticalPanel panel;
	
	private ScrollPanel scroll;
	private CellTable<Exam> cellTableExams;
	private Button newExamButton;
	private Button deleteExamButton;
	private SingleSelectionModel<Exam> selectionModel;
	private ListDataProvider<Exam> dataProvider;
	private HTML logLabel;
	
	public ExamsView(String userName)
	{
		model = new ExamsModel(userName);
		controller = new ExamsController(model, this);
		
		logLabel = new HTML();
		
		cellTableExams = new CellTable<Exam>();
		newExamButton = new Button("Stwórz nowy test");
		deleteExamButton = new Button("Usuń zaznaczony test");
		panel = new VerticalPanel();
		
		
		scroll = new ScrollPanel();
				
		init();
		initWidget(panel);
		initNewExamDialog();
	}
	
	private void init()
	{
		scroll.setSize("300px","400px");
		scroll.setWidget(cellTableExams);
		
		panel.add(scroll);
		HorizontalPanel buttonsPanel = new HorizontalPanel();
		buttonsPanel.add(newExamButton );
		buttonsPanel.add(deleteExamButton);
		buttonsPanel.addStyleName("buttonsPanel");
		
		newExamButton.setStyleName("btn btn-success btn-small");
		deleteExamButton.setStyleName("btn btn-danger btn-small");
		
		panel.add(buttonsPanel);
		panel.add(logLabel);
		logLabel.setVisible(false);
		
		deleteExamButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event)
			{
				if(selectionModel.getSelectedObject() != null)
				{
					if(Window.confirm("czy na pewno chcesz usunąć zaznaczony test?" +
							"wraz z nim zostaną usunięte również wyniki."))
					{
						controller.deleteExam(selectionModel.getSelectedObject().getId());
					}
				}
			}
		});
	}

	private void initNewExamDialog()
	{
		newExamButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				final NewExamView view = new NewExamView(model.getUserName(), controller); 
				view.setAnimationEnabled(true);
				view.center();
				view.show();
			}
		});
		
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

	public void redirectToLoginPage()
	{
		Window.Location.replace("http://student.icis.pcz.pl");
	}
	
	public void showExams()
	{
		TextColumn<Exam> nameColumn = new TextColumn<Exam>(){
			@Override
			public String getValue(Exam exam){
				return exam.getName();
			}
		};
		
		Column<Exam, ImageResource> imageColumn = new Column<Exam, ImageResource>(new ImageResourceCell()) {
			
			@Override
			public ImageResource getValue(Exam exam)
			{
				return Examsmanagement.resources.exam_image();
			}
		}; 
		cellTableExams.setVisibleRange(0, 100);
		cellTableExams.addColumn(imageColumn);
		cellTableExams.addColumn(nameColumn);
		
		if(dataProvider == null)
		{
			dataProvider = new ListDataProvider<Exam>();
			dataProvider.addDataDisplay(cellTableExams);
		}
		dataProvider.setList(model.getExamList());
		
		selectionModel = new SingleSelectionModel<Exam>();
		cellTableExams.setSelectionModel(selectionModel);
		//selectionModel.setSelected(model.getExamList().get(0), true);
		selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
		      public void onSelectionChange(SelectionChangeEvent event)
		      {
			        Exam selected = selectionModel.getSelectedObject();
			        if (selected != null) 
			        {
			        	controller.updateRegView(selected.getId());
			        	controller.updateEditView(selected);
			        	controller.updateResultsView(selected.getId());
			        	deleteExamButton.setEnabled(true);
			        	logLabel.setVisible(false);
			        }
			        else
			        	deleteExamButton.setEnabled(false);
		      }
		    });
	}
	
	public ExamsController getController()
	{
		return this.controller;
	}

	public Exam getSelectedExam()
	{
		return selectionModel.getSelectedObject();
	}

	public void refresh() 
	{
		if(model.getExamList() != null)
		{
			if(dataProvider == null)
			{
				dataProvider = new ListDataProvider<Exam>();
				dataProvider.addDataDisplay(cellTableExams);
			}
			dataProvider.setList(model.getExamList());
		}
		deleteExamButton.setEnabled(false);
		logLabel.setVisible(false);
	}
}
