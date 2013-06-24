package au.csiro.eis.client.view;

import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Format;
import com.sencha.gxt.core.client.util.Params;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.MultiLinePromptMessageBox;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

import au.csiro.eis.client.event.UiEvent;
import au.csiro.eis.client.presenter.Presenter;
import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.beans.RdfResourceBean;
import au.csiro.eis.ontology.beans.SparqlSelectResultBean;
import au.csiro.eis.ontology.beans.SparqlSelectResultSetBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.OntologyClassInfoTemplate;
import au.csiro.eis.ontology.gwt.widgets.OntologyClassSelectionDnd;
import au.csiro.eis.ontology.gwt.widgets.OntologyIndividualInfoTemplate;
import au.csiro.eis.ontology.gwt.widgets.OntologySelectedClassViewFromTreepanelContentPanel;
import au.csiro.eis.ontology.gwt.widgets.OntologyTreeBrowser;
import au.csiro.eis.ontology.gwt.widgets.OntologyTreePanel;

public class SparqlPortletViewImpl implements  IsWidget, EntryPoint,OntologyView {

	Presenter<OwlOntologyBean> presenter;
	//OntologyTreeBrowser ontTreeBrowser;
	private OntologyQueryServiceAsync ontologyService;
	HasWidgets container;

	int height = 600;
	int width = 850;

	PortalLayoutContainer portalLayout;
	VerticalLayoutContainer layout;


	int numPortlets;
	int maxRows;
	int maxCols;

	Map<Portlet, String> sparqlQueryIdx = new HashMap<Portlet,String>();

	//Dialog sparqlQueryDlg;
	Dialog addPortletDlg ;
	protected int portletid = 0;

	public SparqlPortletViewImpl() throws OntologyInitException {
		this.presenter = null;
		numPortlets = 0;
		maxRows = 3;

		maxCols = 1;

	}

	public Presenter<OwlOntologyBean> getPresenter() {
		return this.presenter;
	}

	public void setPresenter(Presenter<OwlOntologyBean> presenter) {
		this.presenter = presenter;
	}


	public Widget asWidget() {
		setupAddPortletDlg();

		layout = new VerticalLayoutContainer();
		layout.setHeight(height);
		layout.setWidth(width);


		ToolBar toolBar = new ToolBar();

		TextButton item1 = new TextButton("Add SPARQL query portlet");
		
		
		item1.addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				showAddPortletDlg();
			}

		});

		toolBar.add(item1);

		

		layout.add(toolBar);
		layout.add(this.portalLayout);



		return this.layout;
	}

	public void setupAddPortletDlg() {

		/*
		addPortletDlg = new MultiLinePromptMessageBox("Sparql query", "Please enter a SPARQL select query:");
		   addPortletDlg.addHideHandler(new HideHandler() {
	          public void onHide(HideEvent event) {
	            String sparqlQuery = addPortletDlg.getValue();
	            //String sparqlQuery = Format.substitute("You entered '{0}'", new Params(v));
				addPortlet(sparqlQuery);

	          }
	        });
	        //box.show();

		 */

		this.addPortletDlg = new Dialog();
		addPortletDlg.setBodyBorder(false);
		addPortletDlg.setHeadingText("SPARQL query dialog");
		addPortletDlg.setWidth(400);
		addPortletDlg.setHeight(400);
		addPortletDlg.setHideOnButtonClick(true);
		addPortletDlg.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);

		VerticalLayoutContainer con = new VerticalLayoutContainer();

		OwlOntologyBean ontologyBean = (OwlOntologyBean) this.presenter.getModel();
		
		String prefixString = null;
		if(ontologyBean !=null) {
			Map<String,String> prefixes = ontologyBean.getPrefixes();
			StringBuffer sb = new StringBuffer();
			
			for(String key : prefixes.keySet()) {
				String prefix = prefixes.get(key);
				
				String value = "PREFIX " + key + ": <" + prefix + ">\n";
				System.out.println(value);
				sb.append(value);
				prefixString = "PREFIX xml:<http://www.w3.org/XML/1998/namespace>\n" +
				"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
				"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n" +
				"PREFIX owl:<http://www.w3.org/2002/07/owl#>\n" +
				"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
				"PREFIX skos:<http://www.w3.org/2004/02/skos/core#>\n\n" +
				sb.toString();
			}
		}
		else {
			prefixString = 	"PREFIX xml:<http://www.w3.org/XML/1998/namespace>\n" +
					"PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>\n" +
					"PREFIX xsd:<http://www.w3.org/2001/XMLSchema#>\n" +
					"PREFIX owl:<http://www.w3.org/2002/07/owl#>\n" +
					"PREFIX rdf:<http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
					"PREFIX skos:<http://www.w3.org/2004/02/skos/core#>\n\n";
		
		}
		
		con.setScrollMode(ScrollMode.AUTO);
		final TextArea queryTextArea = new TextArea();
		queryTextArea.setText(
						prefixString + 
						"SELECT ?resource\n" +
						"WHERE {\n" +
						"?resource rdf:type owl:Class\n" +
						"}\n" +
						"LIMIT 10 \n"
				);

		con.add(queryTextArea, new VerticalLayoutData(1,1));



		addPortletDlg.add(con);
		addPortletDlg.addHideHandler(new HideHandler() {

			public void onHide(HideEvent event) {
				if(addPortletDlg.getHideButton().getText().toLowerCase().equals("ok")) {


					//create new portlet
					System.out.println("Invoking add portlet");
					addPortlet(queryTextArea.getText());
				}
			}

		});
	}

	public void showAddPortletDlg() {


		addPortletDlg.show();




	}

	public void addPortlet(final String sparqlQuery) {
		if(sparqlQuery == null) {
			Info.display("Sparql query", "Empty string");

			return;
		}

		System.out.println("Executing sparql");

		//execute sparql query
		this.ontologyService.performSparqlSelectQuery(sparqlQuery, new AsyncCallback<SparqlSelectResultSetBean>() {

			public void onFailure(Throwable caught) {
				System.out.println("Sparql query failed");

				Info.display("Sparql query", "Failed");

			}

			public void onSuccess(SparqlSelectResultSetBean result) {
				System.out.println("Sparql query result returned");

				
				if(result == null) {
					Info.display("Sparql query", "Failed");
					System.out.println("Sparql query failed");
					return;
				}

				Portlet portlet = createPortlet(result, sparqlQuery);
				System.out.println("Creating portlet");
				portlet.setHeadingText("Sparql result #" + portletid ++);
				sparqlQueryIdx.put(portlet, sparqlQuery);
				portalLayout.add(portlet, getPortletRowPos());
				numPortlets++;						
				System.out.println("Add portlet for Sparql query ");
			}

		});

		/*
		Portlet portlet = new Portlet();
		portlet.setHeadingText(selectedClass.getName());
		configPanel(portlet);
		portlet.add(new HTML("Label: " + selectedClass.getLabel()));
		 */


	}

	private Portlet createPortlet(SparqlSelectResultSetBean sparqlResultBean, String origQuery) {
		Portlet portlet = new Portlet();
		//portlet.setHeadingText(selectedClass.getName());
		configPanel(portlet);
		//portlet.add(new HTML("Label: " + selectedClass.getLabel()));

		sparqlResultBean.getResultSet();

		Grid grid = new Grid(sparqlResultBean.getResultSet().size()+1, sparqlResultBean.getVarNames().size());

		// Add images to the grid
		int numRows = grid.getRowCount();
		int numColumns = grid.getColumnCount();

		//header row
		for (int col = 0; col < numColumns; col++) {
			String var = sparqlResultBean.getVarNames().get(col);

			grid.setWidget(0, col, new HTML(var));
		}


		for (int row = 1; row < numRows; row++) {
			SparqlSelectResultBean result = sparqlResultBean.getResultSet().get(row-1);
			for (int col = 0; col < numColumns; col++) {
				final String data = result.getEntryList().get(col);

				if(data != null && data.startsWith("http://")) {
					Anchor dataUriAnchor = new Anchor(data);

					dataUriAnchor.addClickHandler(new ClickHandler() {

						public void onClick(ClickEvent event) {
							showUriInfoDlg(data);

						}

					});

					grid.setWidget(row, col, dataUriAnchor);

				}
				else if(data != null){
					grid.setWidget(row, col, new HTML(data));
				}
			}
		}


		portlet.add(grid);

		return portlet;
	}

	private void showUriInfoDlg(String uri) {
		Dialog rdfResourceInfoDlg = new Dialog();
		rdfResourceInfoDlg.setBodyBorder(false);
		rdfResourceInfoDlg.setHeadingText("Resource info dialog");
		rdfResourceInfoDlg.setWidth(400);
		rdfResourceInfoDlg.setHeight(400);
		rdfResourceInfoDlg.setHideOnButtonClick(true);
		rdfResourceInfoDlg.setPredefinedButtons(PredefinedButton.OK);

		VerticalLayoutContainer con = new VerticalLayoutContainer();

		//query for uri 

		//con.add(queryTextArea, new VerticalLayoutData(1,1));

		populateContainerForRdfResource(uri, con);
		con.setScrollMode(ScrollMode.AUTO);

		rdfResourceInfoDlg.add(con);
		
		rdfResourceInfoDlg.show();

	}

	private void populateContainerForRdfResource(String uri, final VerticalLayoutContainer con) {

		//query for rdf resource uri
		
		ontologyService.getRdfResource(uri, new AsyncCallback<RdfResourceBean>() {
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			public void onSuccess(RdfResourceBean result) {
				// TODO Auto-generated method stub
				
				if(result != null && result instanceof OwlClassBean) {
					populateContainerForClass( (OwlClassBean) result, con);
				}
				else if(result != null && result instanceof OwlIndividualBean) {
					populateContainerForIndividual( (OwlIndividualBean) result, con);

				}
			}
		});
	}

	private void populateContainerForClass(OwlClassBean classBean, VerticalLayoutContainer con) {
		OntologyClassInfoTemplate infoTemplate;

		infoTemplate = new OntologyClassInfoTemplate(classBean, ontologyService);

		con.add(infoTemplate.asWidget(), new VerticalLayoutData(1,1));
	}

	private void populateContainerForIndividual(OwlIndividualBean indivBean, VerticalLayoutContainer con) {

		OntologyIndividualInfoTemplate infoTemplate;

		infoTemplate = new OntologyIndividualInfoTemplate(indivBean);

		con.add(infoTemplate.asWidget(),  new VerticalLayoutData(1,1));
	}

	private void decrementPortletCount() {
		this.numPortlets--;
	}

	private int getPortletRowPos() {
		return this.numPortlets % this.maxCols;
	}

	private void configPanel(final Portlet panel) {
		panel.setCollapsible(true);
		panel.setAnimCollapse(false);

		panel.getHeader().addTool(new ToolButton(ToolButton.QUESTION, new SelectHandler() {

			public void onSelect(SelectEvent event) {
				String query  = sparqlQueryIdx.get(panel);
				if(query != null) {
					final Dialog simple = new Dialog();
					simple.setHeadingText("SPARQL Query");
					simple.setBodyStyleName("pad-text");
					simple.setHeight(400);
					simple.setWidth(600);
					
					VerticalLayoutContainer con = new VerticalLayoutContainer();
					
					final TextArea queryTextArea = new TextArea();
					queryTextArea.setText(query);
					
					con.add(queryTextArea, new VerticalLayoutData(1,1));

					
					simple.add(con);
					
					
					simple.getBody().addClassName("pad-text");
					simple.setHideOnButtonClick(true);
					//simple.setWidth(300);
					simple.show();
				}


			}
		}));
		//panel.getHeader().addTool(new ToolButton(ToolButton.GEAR));
		panel.getHeader().addTool(new ToolButton(ToolButton.CLOSE, new SelectHandler() {

			public void onSelect(SelectEvent event) {
				panel.removeFromParent();
				decrementPortletCount();
			}
		}));

		panel.setBodyStyle("font-size: small; background-color: white;");
	}

	public void setupPortal() {
		this.portalLayout = new PortalLayoutContainer(this.maxCols);
		this.portalLayout.setHeight(this.height);
		this.portalLayout.setWidth(this.width);
		portalLayout.getElement().getStyle().setBackgroundColor("silver");

		portalLayout.setScrollMode(ScrollMode.AUTOY);

		if(this.maxCols == 3) {
			portalLayout.setColumnWidth(0, .40);
			portalLayout.setColumnWidth(1, .30);
			portalLayout.setColumnWidth(2, .30);
		}

		else if(this.maxCols == 2) {
			portalLayout.setColumnWidth(0, .50);
			portalLayout.setColumnWidth(1, .50);			
		}
		else if(this.maxCols == 1) {
			portalLayout.setColumnWidth(0, 1);
		}

	}



	public void init(HasWidgets container)  {
		setupPortal();
		this.setContainer(container);

		this.getContainer().clear();
		this.getContainer().add(this.asWidget());

		this.presenter.viewInitComplete();		
	}

	public void display(HasWidgets container) {
		setupPortal();
		this.setContainer(container);

		this.getContainer().clear();
		this.getContainer().add(this.asWidget());

		this.presenter.viewDisplayComplete();			
	}

	public void update() {
		System.out.println("in OntologyTreeViewImpl: update");
		System.out.println("in OntologyTreeViewImpl: setup treebrowser");

		setupPortal();

		this.getContainer().clear();
		this.getContainer().add(this.asWidget());

		//onUiEvent(new OntologyUIEvent(OntologyUIEvent.Predefined.UPDATE));
		presenter.viewUpdateComplete();
	}

	public void onModuleLoad() {

	}

	public void setOntologyService(OntologyQueryServiceAsync ontologyService) {
		this.ontologyService = ontologyService;

	}

	public HasWidgets getContainer() {
		return container;
	}

	public void setContainer(HasWidgets container) {
		this.container = container;
	}

	public void reload() {

		//reload the ontology and everything else
		update();

	}



	public void onUiEvent(UiEvent event) {
		// TODO Auto-generated method stub

	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}



}
