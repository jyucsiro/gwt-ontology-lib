package au.csiro.eis.client.view;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;
import com.sencha.gxt.widget.core.client.tree.Tree;

import au.csiro.eis.client.event.UiEvent;
import au.csiro.eis.client.presenter.Presenter;
import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.OntologyClassInfoTemplate;
import au.csiro.eis.ontology.gwt.widgets.OntologyClassSelectionDnd;
import au.csiro.eis.ontology.gwt.widgets.OntologySelectedClassViewFromTreepanelContentPanel;
import au.csiro.eis.ontology.gwt.widgets.OntologyTreeBrowser;
import au.csiro.eis.ontology.gwt.widgets.OntologyTreePanel;

public class ClassInfoViewImpl implements  IsWidget, EntryPoint,OntologyView {

	Presenter<OwlOntologyBean> presenter;
	//OntologyTreeBrowser ontTreeBrowser;
	private OntologyQueryServiceAsync ontologyService;
	HasWidgets container;
	
	int height = 600;
	int width = 850;

	PortalLayoutContainer portalLayout;
	VerticalLayoutContainer layout;
	
	OntologyTreePanel ontTreePanel;
	//OntologyTreeBrowser ontTreePanel;

	int numPortlets;
	int maxRows;
	
	Dialog classSelectionDlg;
	
	
	public ClassInfoViewImpl() throws OntologyInitException {
		this.presenter = null;
		numPortlets = 0;
		maxRows = 3;
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
		 
	    TextButton item1 = new TextButton("Add portlet");
	    
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
		this.classSelectionDlg = new Dialog();
		classSelectionDlg.setBodyBorder(false);
		classSelectionDlg.setHeadingText("Select Class Dialog");
		classSelectionDlg.setWidth(400);
		classSelectionDlg.setHeight(225);
		classSelectionDlg.setHideOnButtonClick(true);
		classSelectionDlg.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		
	    VerticalLayoutContainer con = new VerticalLayoutContainer();
	    con.setScrollMode(ScrollMode.AUTO);
	    try {
			ontTreePanel = new OntologyTreePanel(ontologyService, false, true, false);
		} catch (OntologyInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    con.add(ontTreePanel.asWidget());
		
	    /*
	    OntologyClassSelectionDnd classView = new OntologyClassSelectionDnd(ontologyService);
		
		OntologySelectedClassViewFromTreepanelContentPanel selectionPanel 
				= new OntologySelectedClassViewFromTreepanelContentPanel(ontologyService, classView);
		con.add(selectionPanel);
		*/
	    
	    classSelectionDlg.add(con);
	    classSelectionDlg.addHideHandler(new HideHandler() {

			public void onHide(HideEvent event) {
				if(classSelectionDlg.getHideButton().getText().toLowerCase().equals("ok")) {
					
					Tree<OwlClassBean, String> tree = ontTreePanel.getTree();
					OwlClassBean selectedClass = tree.getSelectionModel().getSelectedItem();
					
					Info.display("Selected", selectedClass.getIri());
					//create new portlet
					addPortlet(selectedClass);
				}
			}
	    	
	    });
	}

	public void showAddPortletDlg() {
		
	    
		classSelectionDlg.show();
	    
		
	    
	    
	}
	
	public void addPortlet(OwlClassBean selectedClass) {
		if(selectedClass == null) {
			return;
		}

		/*
		Portlet portlet = new Portlet();
		portlet.setHeadingText(selectedClass.getName());
		configPanel(portlet);
		portlet.add(new HTML("Label: " + selectedClass.getLabel()));
		*/
		
		Portlet portlet = createPortlet(selectedClass);
		

		this.portalLayout.add(portlet, getPortletRowPos());
		this.numPortlets++;		
	}
	
	private Portlet createPortlet(OwlClassBean selectedClass) {
		Portlet portlet = new Portlet();
		portlet.setHeadingText(selectedClass.getName());
		configPanel(portlet);
		//portlet.add(new HTML("Label: " + selectedClass.getLabel()));

		
		System.out.println("name: " +selectedClass.getName());
		System.out.println("id: " +selectedClass.getIri());
		System.out.println("hasChildren? " +selectedClass.hasChildren());

		System.out.println("hasIndividuals? " +selectedClass.hasIndividuals());

		OntologyClassInfoTemplate infoTemplate;

		infoTemplate = new OntologyClassInfoTemplate(selectedClass, ontologyService);

		portlet.add(infoTemplate.asWidget());

		return portlet;
	}
	
	private void decrementPortletCount() {
		this.numPortlets--;
	}

	private int getPortletRowPos() {
		return this.numPortlets % this.maxRows;
	}

	private void configPanel(final Portlet panel) {
		panel.setCollapsible(true);
		panel.setAnimCollapse(false);
		
		
		//panel.getHeader().addTool(new ToolButton(ToolButton.GEAR));
		panel.getHeader().addTool(new ToolButton(ToolButton.CLOSE, new SelectHandler() {

			public void onSelect(SelectEvent event) {
				panel.removeFromParent();
				decrementPortletCount();
			}
		}));
	}

	public void setupPortal() {
		this.portalLayout = new PortalLayoutContainer(3);
		this.portalLayout.setHeight(this.height);
		this.portalLayout.setWidth(this.width);
		portalLayout.getElement().getStyle().setBackgroundColor("silver");
		
		portalLayout.setScrollMode(ScrollMode.AUTOY);
		
		portalLayout.setColumnWidth(0, .40);
		portalLayout.setColumnWidth(1, .30);
		portalLayout.setColumnWidth(2, .30);		
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
