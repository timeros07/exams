package icis.pcz.pl.examsmanagement.client;



import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.ImageResourceCell;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.view.client.ListDataProvider;

public class ResultsView extends Composite
{
	private VerticalPanel panel;
	private ResultsModel model;
	private ResultsController controller;
	
	private CellTable<Result> resultsCellTable;
	private HTML logLabel;
	
	private ListDataProvider<Result> dataProvider;
	
	public ResultsView()
	{
		model = new ResultsModel();
		controller = new ResultsController(this, model);
		panel = new VerticalPanel();
		initWidget(panel);
		
		resultsCellTable = new CellTable<Result>();
		logLabel = new HTML();
		
		ScrollPanel scroll = new ScrollPanel();
		scroll.add(resultsCellTable);
		panel.add(scroll);
		scroll.setHeight("500px");
		panel.add(logLabel);
		logLabel.setVisible(false);
		init();
	}

	private void init() 
	{
		Column<Result, ImageResource> imageColumn = new Column<Result, ImageResource>(new ImageResourceCell()) {
			
			@Override
			public ImageResource getValue(Result result)
			{
				return Examsmanagement.resources.result_image();
			}
		}; 
		
		TextColumn<Result> userNameColumn = new TextColumn<Result>(){
			@Override
			public String getValue(Result value)
			{
				return value.getUserName();
			}
		};
		
		TextColumn<Result> resultColumn = new TextColumn<Result>(){
			@Override
			public String getValue(Result value)
			{
				return Integer.toString(value.getResult()) + "/" + value.getQuestionCount();
			}
		};
		
		CellTableButton cellButton = new CellTableButton();
		Column <Result,String> showExamColumn = new Column <Result,String>(cellButton)
		{
			@Override
			public String getValue(Result r) 
			{
				return "zobacz odpowiedzi";
			}
		};
		
		TextColumn<Result> dateColumn = new TextColumn<Result>(){
			@Override
			public String getValue(Result value)
			{
				return value.getDate();
			}
		};
		
		showExamColumn.setFieldUpdater(new FieldUpdater<Result, String>() {
			
			@Override
			public void update(int index, Result object, String value) 
			{
				showAnswers(object.getUserName(), object.getResultId());
			}

		});
		
		resultsCellTable.addColumn(imageColumn);
		resultsCellTable.addColumn(userNameColumn, "nazwa użytkownika");
		resultsCellTable.addColumn(resultColumn, "wynik");
		resultsCellTable.addColumn(dateColumn, "data");
		resultsCellTable.addColumn(showExamColumn);
		
	}
	
	public void showResults()
	{
		dataProvider = new ListDataProvider<Result>();
		dataProvider.addDataDisplay(resultsCellTable);
		dataProvider.setList(model.getResultsList());
	}

	public ResultsController getController()
	{
		return controller;
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

	private void showAnswers(String selectedUserName, int resultId) 
	{
		AnswersView view = new AnswersView(model.getExamId(), selectedUserName, resultId);
		view.setAnimationEnabled(true);
		view.show();
		view.center();
		
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
				html = SafeHtmlUtils.fromTrustedString("");
			}
	        sb.append(html);
		}
	}
	
	public void clearLogLabel() 
	{
		logLabel.setVisible(false);
	}
}
