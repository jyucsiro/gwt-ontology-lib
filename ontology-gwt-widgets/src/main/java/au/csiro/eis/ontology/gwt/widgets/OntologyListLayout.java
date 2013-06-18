package au.csiro.eis.ontology.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryService;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;

import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.Element;  
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;


/**
 * 
 */
public class OntologyListLayout extends FlowLayoutContainer {  
	private FlexTable classesFlexTable;
	
	private HorizontalPanel buttonPanel;
	private Button goButton;
	private Button clearButton;
	private ArrayList<String> classes;
	
		private Label errorMsgLabel;
	private Label consoleMsgLabel;

	private OntologyQueryServiceAsync ontologyQueryService ;


	public OntologyListLayout(OntologyQueryServiceAsync ontologyQueryService) {
		this.ontologyQueryService = ontologyQueryService;
		
	}

	@Override  
	public Widget asWidget() {

		//super.onRender(parent, index);  
		//setLayout(new FitLayout());

		initialise();

		
		ContentPanel mainData = ContentPanelFactory.createPlainContentPanel("Classes List");  
		mainData.setBorders(true);  
		mainData.setBodyStyle("fontSize: 12px; padding: 6px");   
		mainData.setPixelSize(600, 300);

		mainData.add(buttonPanel);
		mainData.add(classesFlexTable);

		add(mainData);
		return this;
	}  

	

	public void initialise() {
		//create the objects
		classes = new ArrayList<String>();
		goButton = new Button("Populate classes");
		clearButton = new Button("Clear");
		classesFlexTable = new FlexTable();
		buttonPanel = new HorizontalPanel();
		errorMsgLabel = new Label();
		consoleMsgLabel = new Label();


		classesFlexTable.setText(0, 0, "Classes");
		// Assemble go/clear Stock panel.
		buttonPanel.add(goButton);
		buttonPanel.add(clearButton);

		// Listen for mouse events on the Add button.
		goButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				consoleMsgLabel.setText("Populating classes...");
				consoleMsgLabel.setVisible(true);

				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						Window.alert("Code download failed");
					}

					public void onSuccess() {
						
						// Setup timer to refresh list automatically.
						Timer refreshTimer = new Timer() {
							@Override
							public void run() {
								String text = consoleMsgLabel.getText();
								consoleMsgLabel.setText(text + ".");
							}
						};
						refreshTimer.scheduleRepeating(1000);


						populateClasses();
					}
				});
				
			}
		});

		clearButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				consoleMsgLabel.setText("Clearing list of classes...");
				consoleMsgLabel.setVisible(true);

				GWT.runAsync(new RunAsyncCallback() {
					public void onFailure(Throwable caught) {
						Window.alert("Classes clear button failed");
					}

					public void onSuccess() {
						
						// Setup timer to refresh list automatically.
						Timer refreshTimer = new Timer() {
							@Override
							public void run() {

								String text = consoleMsgLabel.getText();
								consoleMsgLabel.setText(text + ".");

							}
						};
						refreshTimer.scheduleRepeating(1000);

						clearClasses();
					}
				});

			}
		});
	}

	public void populateClasses() {
		// Initialize the service proxy.
		if (ontologyQueryService == null) {
			ontologyQueryService = GWT.create(OntologyQueryService.class);
		}

		// Set up the callback object.
		AsyncCallback<List<String>> callback = new AsyncCallback<List<String>>() {
			public void onFailure(Throwable caught) {

				// If the stock code is in the list of delisted codes, display an error message.
				String details = caught.getMessage();
				if (caught instanceof OntologyInitException) {
					details = "Ontology '" + ((OntologyInitException)caught).getOntologyId() + "' could not be read";
				}

				errorMsgLabel.setText("Error: " + details);
				errorMsgLabel.setVisible(true);

				consoleMsgLabel.setText("");
				consoleMsgLabel.setVisible(false);
			}

			public void onSuccess(List<String> result) {
				consoleMsgLabel.setText("");
				consoleMsgLabel.setVisible(false);
				
				addClassesToTable(result);
			}
		};

		ontologyQueryService.getClasses(callback);
	}
	
	
	private void addClassesToTable(List<String> classes) {

		// Create a CellList.
		CellList<String> cellList = new CellList<String>(new TextCell());

		// Add a cellList to a data provider.
		ListDataProvider<String> dataProvider = new ListDataProvider<String>();
		List<String> data = dataProvider.getList();

		for (int i = 0; i < classes.size(); i++) {
			data.add(classes.get(i));
		}
		//for (int i = 0; i < 200; i++) {
		//  data.add("Item " + i);
		//}
		dataProvider.addDataDisplay(cellList);

		// Create a SimplePager.
		SimplePager pager = new SimplePager();

		// Set the cellList as the display.
		pager.setDisplay(cellList);

		// Add the pager and list to the page.
		//VerticalPanel vPanel = new VerticalPanel();
		//vPanel.add(pager);
		//vPanel.add(cellList);

		int row = classesFlexTable.getRowCount();
		classesFlexTable.setWidget(row++, 0, pager);
		classesFlexTable.setWidget(row++, 0, cellList);



		//for (int i = 0; i < classes.length; i++) {
		//	addClass(classes[i]);
		//}
		// Clear any errors.
		errorMsgLabel.setVisible(false);
	}

	public void clearClasses() {
		this.classes = new ArrayList<String>();

		classesFlexTable.removeAllRows();
		classesFlexTable.setText(0, 0, "Symbol");

		consoleMsgLabel.setText("");
		consoleMsgLabel.setVisible(false);
	}

}