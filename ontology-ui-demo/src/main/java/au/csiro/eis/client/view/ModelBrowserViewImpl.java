package au.csiro.eis.client.view;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

import au.csiro.eis.client.event.UiEvent;
import au.csiro.eis.client.presenter.Presenter;
import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.OntologyTreePanel;

public class ModelBrowserViewImpl implements  IsWidget, EntryPoint,OntologyView {

	Presenter<OwlOntologyBean> presenter;
	//OntologyTreeBrowser ontTreeBrowser;
	OntologyTreePanel ontTreePanel;
	private OntologyQueryServiceAsync ontologyService;
	HasWidgets container;
	
	int TREE_PANEL_HEIGHT = 300;
	int TREE_PANEL_WIDTH = 850;
	private int TREE_VIEWER_WIDTH;
	private int TREE_BROWSER_WIDTH;
	private int TREE_VIEWER_HEIGHT;
	private int TREE_BROWSER_HEIGHT;

	
	public ModelBrowserViewImpl() throws OntologyInitException {
		this.presenter = null;
		//ontTreeBrowser = null;
		
	}
	
	public Presenter<OwlOntologyBean> getPresenter() {
		return this.presenter;
	}

	public void setPresenter(Presenter<OwlOntologyBean> presenter) {
		this.presenter = presenter;
	}

	




	public Widget asWidget() {
		// TODO Auto-generated method stub
		final FlowLayoutContainer con = new FlowLayoutContainer();
		System.out.println("Adding label to container in OntologyTreeView");
		//con.add(new Label("OntologyTreeViewImpl placeholder"));
		
		if(ontTreePanel != null) {
			System.out.println("Adding ontTreeBrowser to container in OntologyTreeView");
			con.add(ontTreePanel.asWidget());
		}
		
		return con;
		
		
	}

	public void setupTreeBrowser() {
		System.out.println("in OntologyTreeViewImpl: get owlOntologyBean model from presenter");
		try {
			boolean doLoad= false;
			ontTreePanel = new OntologyTreePanel(ontologyService, doLoad);
			ontTreePanel.setPANEL_HEIGHT(this.TREE_PANEL_HEIGHT);
			ontTreePanel.setPANEL_WIDTH(this.TREE_PANEL_WIDTH);
			
			ontTreePanel.setMODEL_BROWSER_HEIGHT(this.TREE_BROWSER_HEIGHT);
			ontTreePanel.setMODEL_BROWSER_WIDTH(this.TREE_BROWSER_WIDTH);
			
			ontTreePanel.setMODEL_VIEWER_WIDTH(this.TREE_VIEWER_WIDTH);
			ontTreePanel.setMODEL_VIEWER_HEIGHT(this.TREE_VIEWER_HEIGHT);

		} catch (OntologyInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		
	}
	
	public void init(HasWidgets container)  {
		setupTreeBrowser();
		this.setContainer(container);

		this.getContainer().clear();
		this.getContainer().add(this.asWidget());
		
		this.presenter.viewInitComplete();		
	}

	public void display(HasWidgets container) {
		setupTreeBrowser();
		this.setContainer(container);

		this.getContainer().clear();
		this.getContainer().add(this.asWidget());
		
		this.presenter.viewDisplayComplete();			
	}
	
	public void update() {
		System.out.println("in OntologyTreeViewImpl: update");
		System.out.println("in OntologyTreeViewImpl: setup treebrowser");

		setupTreeBrowser();
		
		this.getContainer().clear();
		this.getContainer().add(this.asWidget());

		//onUiEvent(new OntologyUIEvent(OntologyUIEvent.Predefined.UPDATE));
		presenter.viewUpdateComplete();
	}
	
	public void onModuleLoad() {
		// TODO Auto-generated method stub
		
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

	public int getTREE_PANEL_HEIGHT() {
		return TREE_PANEL_HEIGHT;
	}

	public void setTREE_PANEL_HEIGHT(int tREE_PANEL_HEIGHT) {
		TREE_PANEL_HEIGHT = tREE_PANEL_HEIGHT;
	}

	public int getTREE_PANEL_WIDTH() {
		return TREE_PANEL_WIDTH;
	}

	public void setTREE_PANEL_WIDTH(int tREE_PANEL_WIDTH) {
		TREE_PANEL_WIDTH = tREE_PANEL_WIDTH;
	}

	public void onUiEvent(UiEvent event) {
		// TODO Auto-generated method stub
		
	}

	public void setTREE_VIEWER_HEIGHT(int i) {
		TREE_VIEWER_HEIGHT = i;
		
	}

	public void setTREE_VIEWER_WIDTH(int i) {

		TREE_VIEWER_WIDTH = i;
	}

	public void setTREE_BROWSER_HEIGHT(int i) {
		
		TREE_BROWSER_HEIGHT = i;
	}

	public void setTREE_BROWSER_WIDTH(int i) {
		TREE_BROWSER_WIDTH = i;
		
	}
	
	
	
	

}
