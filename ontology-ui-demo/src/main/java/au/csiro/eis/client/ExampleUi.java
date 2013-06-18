package au.csiro.eis.client;

import au.csiro.eis.client.event.AppEvent;
import au.csiro.eis.client.event.AppEventHandler;
import au.csiro.eis.client.event.AppEvents;
import au.csiro.eis.client.presenter.ViewOntologyPresenter;
import au.csiro.eis.client.view.ClassInfoViewImpl;
import au.csiro.eis.client.view.ModelBrowserViewImpl;
import au.csiro.eis.client.view.SparqlPortletViewImpl;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryService;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.rpc.shared.ConfigBean;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.ToggleGroup;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.box.AutoProgressMessageBox;
import com.sencha.gxt.widget.core.client.box.ProgressMessageBox;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer.VerticalLayoutData;
import com.sencha.gxt.widget.core.client.container.Viewport;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.form.FieldSet;
import com.sencha.gxt.widget.core.client.form.Radio;
import com.sencha.gxt.widget.core.client.form.TextArea;
import com.sencha.gxt.widget.core.client.info.Info;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ExampleUi implements EntryPoint, AppEventHandler, IsWidget, ValueChangeHandler<String> {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final OntologyQueryServiceAsync ontologyService = GWT.create(OntologyQueryService.class);
	HandlerManager eventBus = new HandlerManager(null);
	VerticalLayoutContainer content = new VerticalLayoutContainer();

	ContentPanel north = new ContentPanel();
	ContentPanel west = new ContentPanel();
	ContentPanel center = new ContentPanel();
	ConfigBean config;
	//private final Messages messages = GWT.create(Messages.class);

	VerticalLayoutContainer navContainer;
	private Label lbl = new Label();


	private final String CONFIG_FILE = "example_ontology_config_skos.json";
	private final String NAV_CLASS_TREE_BROWSER = "class-tree-browser";
	private final String NAV_CLASS_INFO_PORTLET = "class-info-portlet";
	private final String NAV_FORM_UI = "form-ui";
	private final String NAV_SPARQL_PORTLETS = "sparql-ui";
	private final String SWITCH_ONTOLOGY = "switch-ontology";

	final ProgressMessageBox box = new ProgressMessageBox("Please wait", "Loading services...");
	
	public ExampleUi() {
		config = new ConfigBean();
		config.setConfigFileName(CONFIG_FILE);
		config.setUserIri(null);
	}

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Viewport viewport = new Viewport();
		viewport.add(asWidget());
		RootPanel.get().add(viewport);


		box.setProgressText("Initializing...");
		box.show();
		box.updateProgress(.5,  "{0}% Complete");


		ontologyService.initialiseSvc(config, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(Boolean result) {
				eventBus.fireEvent(new AppEvent(AppEvents.OntologySvnInit, Boolean.toString(result)));
				box.updateProgress(1,  "{0}% Complete");
				box.hide();
			}

		});

		// If the application starts with no history token, redirect to a new
		// 'baz' state.
		String initToken = History.getToken();
		if (initToken.length() == 0) {
			History.newItem("init");
		}


		History.addValueChangeHandler(this);

		// Now that we've setup our listener, fire the initial history state.
		History.fireCurrentHistoryState();
	}

	public void setupNavPanel() {
		this.navContainer = new VerticalLayoutContainer();

		this.west.setWidget(navContainer);		

		this.navContainer.add(new Hyperlink("Class hierarchy browser", this.NAV_CLASS_TREE_BROWSER));
		this.navContainer.add(new Hyperlink("Class info boxes", this.NAV_CLASS_INFO_PORTLET));
		this.navContainer.add(new Hyperlink("Form ui", this.NAV_FORM_UI));
		this.navContainer.add(new Hyperlink("SPARQL portlets", this.NAV_SPARQL_PORTLETS));

		this.navContainer.add(new Hyperlink("Switch ontology", this.SWITCH_ONTOLOGY));

	}

	public void onValueChange(ValueChangeEvent<String> event) {
		// This method is called whenever the application's history changes. Set
		// the label to reflect the current history token.
		lbl.setText("The current history token is: " + event.getValue());

		System.out.println("The current history token is: " + event.getValue());

		this.content.clear();

		if(event.getValue().equals(this.NAV_CLASS_TREE_BROWSER)) {
			System.out.println("Setting up model browser");
			Info.display("Nav item","Setting up class tree browser");
			this.setupModelBrowser(this.content);
		}
		else if(event.getValue().equals(this.NAV_CLASS_INFO_PORTLET) ) {
			Info.display("Nav item","Setting up class info portlet");
			setupClassInfoPortlet(this.content);
		}
		else if(event.getValue().equals(this.NAV_FORM_UI) ) {

		}
		
		else if(event.getValue().equals(this.NAV_SPARQL_PORTLETS) ) {
			Info.display("Nav item","Sparql portlet");
			this.setupSparqlPortlet(content);
		}
		
		else if(event.getValue().equals(this.SWITCH_ONTOLOGY) ) {
			Info.display("Nav item","Switch ontology");
			setupSwitchOntologyNav();
		}

	}

	private void setupSwitchOntologyNav() {

		final Dialog simple = new Dialog();
		simple.setHeadingText("Switch ontology");
		simple.setBodyStyleName("pad-text");
		simple.setHeight(300);
		simple.setWidth(400);
		simple.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		simple.setHideOnButtonClick(true);

		VerticalLayoutContainer con = new VerticalLayoutContainer();

		FieldSet fieldSet = new FieldSet();
		fieldSet.setHeadingText("Select from available ontologies:");
		
		final Radio radio1 = new Radio();
		radio1.setBoxLabel("Ocean colour vocabs (SKOS)");
		radio1.setData("config", "example_ontology_config_skos.json");

		final Radio radio2 = new Radio();
		radio2.setBoxLabel("Pizza ontology (OWL)");
		radio2.setData("config", "example_ontology_config_pizza.json");
		
		final Radio radio3 = new Radio();
		radio3.setBoxLabel("Event detection ontologies(OWL)");
		radio3.setData("config", "example_ontology_config.json");

		con.add(radio1);
		con.add(radio2);
		con.add(radio3);

		 ToggleGroup group = new ToggleGroup();
		 group.add(radio1);
		 group.add(radio2);
		 group.add(radio3);
		 
		// con.add(group);
		 
		fieldSet.add(con);

		simple.add(fieldSet);


		simple.getBody().addClassName("pad-text");
		simple.setHideOnButtonClick(true);

		simple.addHideHandler(new HideHandler() {

			public void onHide(HideEvent event) {
				// TODO Auto-generated method stub
				if(simple.getHideButton().getText().toLowerCase().equals("ok")) {

					//determine which radio button is selected
					if(radio1.getValue() == true) {
						doSwitchOntology((String) radio1.getData("config"));
					}
					else if(radio2.getValue() == true) {
						doSwitchOntology((String) radio2.getData("config"));

					}
					else if(radio3.getValue() == true) {
						doSwitchOntology((String) radio3.getData("config"));

					}


				}

			}

		});

		//simple.setWidth(300);
		simple.show();
	}
	
	public void doSwitchOntology(String configFile) {
		
		config.setConfigFileName(configFile);
		
		box.setProgressText("Switching ontology...");
		box.show();
		box.updateProgress(.5,  "{0}% Complete");

		ontologyService.switchDomain(config, new AsyncCallback<Boolean>() {

			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub

			}

			public void onSuccess(Boolean result) {
				eventBus.fireEvent(new AppEvent(AppEvents.OntologySvnInit, Boolean.toString(result)));
				box.updateProgress(1,  "{0}% Complete");
				box.hide();
				
			}

		});
		
	}

	public void onAppEvent(AppEvent event) {
		if(event.getEventType().equals(AppEvents.OntologySvnInit)) {
			Info.display("Ontology loaded", (String) event.getData());
		}

	}

	public ViewOntologyPresenter setupModelBrowser(HasWidgets con) {
		ModelBrowserViewImpl modelTreeView;
		//final AutoProgressMessageBox box = new AutoProgressMessageBox("Progress", "Loading class tree browser, please wait...");
		//box.setProgressText("Loading...");
		//box.auto();

		final ProgressMessageBox box = new ProgressMessageBox("Please wait", "Loading class tree widget...");
		box.setProgressText("Initializing...");
		box.show();
		box.updateProgress(.5,  "{0}% Complete");

		try {

			modelTreeView = new ModelBrowserViewImpl();
			modelTreeView.setOntologyService(ontologyService);

			modelTreeView.setTREE_PANEL_HEIGHT(600);
			modelTreeView.setTREE_PANEL_WIDTH(800);

			ViewOntologyPresenter modelBrowserPresenter 
			= new ViewOntologyPresenter(ontologyService,  this.eventBus, modelTreeView, con);


			modelBrowserPresenter.load();

			System.out.println("createAndDisplayPresenter - storing view ontology presenter");

			box.setProgressText("Done");

			box.hide();

			return modelBrowserPresenter;


		} catch (OntologyInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		box.setProgressText("Loading... failed");

		box.hide();

		return null;
	}

	public ViewOntologyPresenter setupClassInfoPortlet(HasWidgets con) {
		ClassInfoViewImpl classInfoView;


		final ProgressMessageBox box = new ProgressMessageBox("Please wait", "Loading class tree widget...");
		box.setProgressText("Initializing...");
		box.show();
		box.updateProgress(.5,  "{0}% Complete");

		try {

			classInfoView = new ClassInfoViewImpl();
			classInfoView.setOntologyService(ontologyService);



			ViewOntologyPresenter classInfoPresenter 
			= new ViewOntologyPresenter(ontologyService,  this.eventBus, classInfoView, con);


			classInfoPresenter.load();

			System.out.println("createAndDisplayPresenter - storing view ontology presenter");

			box.setProgressText("Done");

			box.hide();

			return classInfoPresenter;


		} catch (OntologyInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		box.setProgressText("Loading... failed");

		box.hide();

		return null;
	}
	public ViewOntologyPresenter setupSparqlPortlet(HasWidgets con) {
		SparqlPortletViewImpl sparqlView;


		final ProgressMessageBox box = new ProgressMessageBox("Please wait", "Loading SPARQL portlet...");
		box.setProgressText("Initializing...");
		box.show();
		box.updateProgress(.5,  "{0}% Complete");

		try {

			sparqlView = new SparqlPortletViewImpl();
			sparqlView.setOntologyService(ontologyService);



			ViewOntologyPresenter classInfoPresenter 
			= new ViewOntologyPresenter(ontologyService,  this.eventBus, sparqlView, con);


			classInfoPresenter.load();

			System.out.println("createAndDisplayPresenter - storing view ontology presenter");

			box.setProgressText("Done");

			box.hide();

			return classInfoPresenter;


		} catch (OntologyInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		box.setProgressText("Loading... failed");

		box.hide();

		return null;
	}

	public Widget asWidget() {

		eventBus.addHandler(AppEvent.getType(), this);

		setupContentPanels();


		content.setHeight(700);
		content.setWidth(700);

		final BorderLayoutContainer con = new BorderLayoutContainer();
		con.setBorders(true);

		center.add(content);

		BorderLayoutData northData = new BorderLayoutData(50);
		northData.setMargins(new Margins(5));
		northData.setCollapsible(true);
		northData.setSplit(true);
		VerticalLayoutContainer header = new VerticalLayoutContainer();
		header.add(new HTML("<div style='font-size: large'>Ontology-driven UI API demo</div>"));
		header.add(lbl);
		//header.set
		this.north.setWidget(header);
		//northData.set

		BorderLayoutData westData = new BorderLayoutData(150);
		westData.setCollapsible(true);
		westData.setSplit(true);
		westData.setCollapseMini(true);
		westData.setMargins(new Margins(0, 5, 0, 5));

		MarginData centerData = new MarginData();

		con.setNorthWidget(north, northData);
		con.setWestWidget(west, westData);
		con.setCenterWidget(center, centerData);


		//root.add(con);

		//con.setEastWidget(east, eastData);
		//con.setSouthWidget(south, southData);	    

		/*
		VerticalLayoutContainer c = new VerticalLayoutContainer();
	    c.add(content, new VerticalLayoutData(1, 1, new Margins(4)));
		root.add(c);
		 */


		/*
		PortalLayoutContainer portal = new PortalLayoutContainer(3);
		portal.getElement().getStyle().setBackgroundColor("white");
		portal.setColumnWidth(0, .40);
		portal.setColumnWidth(1, .30);
		portal.setColumnWidth(2, .30);

		Portlet portlet = new Portlet();
		portlet.setHeadingText("Grid in a Portlet");
		portlet.setHeight(250);
		portal.add(portlet, 0);

		root.add(portal);
		 */
		return con;
	}

	public void setupContentPanels() {
		this.north.setHeaderVisible(false);

		this.west.setHeaderVisible(true);
		this.west.setHeadingText("Nav");
		this.setupNavPanel();

		this.center.setHeaderVisible(false);

		content.setScrollMode(ScrollMode.AUTO);
	}


}
