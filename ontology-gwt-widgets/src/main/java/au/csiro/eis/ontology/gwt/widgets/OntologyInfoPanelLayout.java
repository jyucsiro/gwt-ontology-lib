package au.csiro.eis.ontology.gwt.widgets;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;

import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.TabPanel;
import com.sencha.gxt.widget.core.client.container.Container;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

public class OntologyInfoPanelLayout extends FlowLayoutContainer{

	OwlClassBean classBean;
	OntologyQueryServiceAsync ontologyQueryService;
    TabPanel parentTabPanel = null;
    Container parentLayout = null;
    public final int NUM_GRID_ROWS = 10;
      
	public OntologyInfoPanelLayout(OwlClassBean bean, OntologyQueryServiceAsync ontologyQueryService, 
			TabPanel parent, Container parentLayout) {
		this.ontologyQueryService = ontologyQueryService;
	
		this.classBean = bean;
		this.parentTabPanel = parent;
		this.parentLayout = parentLayout;
	}
	
	@Override  
	public Widget asWidget() {
		//super.onRender(parent, index);  
	    
		//this.setLayout(new FitLayout());
		
		//ContentPanel cp = this.configureLayout();
		OntologyClassInfoTemplate ontClassInfoTemplate = new OntologyClassInfoTemplate(classBean, this.ontologyQueryService);
		add(ontClassInfoTemplate);
		//this.layout();
		
		//cp.add(ontClassInfoTemplate);

		
		//add(cp);
		return this;
	}

	/* deprecated?
	private ContentPanel configureLayout() {
	    //setLayout(new FitLayout());
	    final ContentPanel infoPanel = ContentPanelFactory.createPlainUndecoratedContentPanel();  
		
		ToolBar toolBar = this.setupToolBar();  
		infoPanel.add(toolBar);

		return infoPanel;
	}
	*/
	
	/* deprecated?
	private ToolBar setupToolBar() {
		ToolBar toolBar = new ToolBar();

		
		//Button button1 = new Button("Map to GSN Sensor", new SelectionListener<ButtonEvent>() {
	    //	public void componentSelected(ButtonEvent ce) {
	    		//getLatestData();
	    		
	    //	}
	    //});

		//toolBar.add(button1);
		
		
		return toolBar;
	}
	*/
}
