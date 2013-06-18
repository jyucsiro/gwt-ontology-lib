package au.csiro.eis.ontology.gwt.widgets;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class OntologyInstanceSelectionDialog extends Dialog {

	int defaultWidth = 700;
	int defaultHeight = 550;
	
	int defaultTreePanelWidth = 300;
	int defaultTreePanelHeight = 410;
	
	int classViewWidth = 700;
	int classViewHeight = 450;

	int treeBrowserWidth = 250;
	int instanceBrowserWidth = 200;
	
	
	OntologySelectedClassViewFromTreepanelContentPanel scv; 
	OntologyTreeBrowser tbcp;
	OntologyClassSelectionInstanceViewDnd instanceView;	
	
	
	OntologyQueryServiceAsync ontologyQueryService;
	List<String> iri_list;
	
	public OntologyInstanceSelectionDialog(OntologyQueryServiceAsync ontologyQueryService, List<String> iri_list) throws OntologyInitException {
		this.ontologyQueryService = ontologyQueryService;
		this.iri_list = iri_list;
		init();
	}
	
	public void init() throws OntologyInitException {
		
		this.instanceView	= new OntologyClassSelectionInstanceViewDnd(this.ontologyQueryService);
		this.scv =  new OntologySelectedClassViewFromTreepanelContentPanel(this.ontologyQueryService, instanceView); 
		
		//this.tbcp = new OntologyTreeBrowserContentPanel(this.ontologyQueryService, iri_list);
		this.tbcp = new OntologyTreeBrowser(this.ontologyQueryService, iri_list);
		//tbcp.setHeaderVisible(false);
		//tbcp.setHeading("Model browser");
		tbcp.setSize(this.defaultTreePanelWidth, this.defaultTreePanelHeight);
		
		//TODO: Migrate tree click listener
		//tbcp.addTreeClickListener(scv.getDefaultTreeClickListener());
		
		addHandlersToTree(this.tbcp.getTree());
		
		
		this.setBodyBorder(false);  
		this.setHeadingText("Class instances browser dialog");  
		this.setWidth(this.defaultWidth);  
		this.setHeight(this.defaultHeight);  
		this.setHideOnButtonClick(true);
		
		this.setPredefinedButtons(PredefinedButton.OK, PredefinedButton.CANCEL);
		 

		this.setModal(true);
		
		ContentPanel cp = new ContentPanel();
		cp.setHeadingText("Model browser");
		
		HorizontalLayoutContainer classViewPanel = new HorizontalLayoutContainer();
		classViewPanel.setPixelSize(this.classViewWidth, this.classViewHeight);
		classViewPanel.add(tbcp.asWidget(), new HorizontalLayoutData(250,400,new Margins(0)));
		classViewPanel.add(scv.asWidget(), new HorizontalLayoutData(200,400,new Margins(0)));

		cp.add(classViewPanel);
		cp.setPixelSize(this.defaultWidth, this.defaultHeight);
		
		this.add(cp);
		
		
	}
	

	@Override  
	public Widget asWidget() {
		//super.onRender(parent, index);  

		//init();

		return this;
	}

	public OntologySelectedClassViewFromTreepanelContentPanel getScv() {
		return scv;
	}

	public void setScv(OntologySelectedClassViewFromTreepanelContentPanel scv) {
		this.scv = scv;
	}

	public OntologyTreeBrowser getTbcp() {
		return tbcp;
	}

	public void setTbcp(OntologyTreeBrowser tbcp) {
		this.tbcp = tbcp;
	}

	public OntologyClassSelectionInstanceViewDnd getInstanceView() {
		return instanceView;
	}

	public void setInstanceView(OntologyClassSelectionInstanceViewDnd instanceView) {
		this.instanceView = instanceView;
	}

	public List<String> getIri_list() {
		return iri_list;
	}

	public void setIri_list(List<String> iri_list) {
		this.iri_list = iri_list;
	}  

	public void updateInstancesView(OwlClassBean selectedClassFromTree) {
		this.scv.showSelectedClass(selectedClassFromTree);
	}
	
	public void addHandlersToTree(final Tree<OwlClassBean, String> tree) {
		
		SimpleSafeHtmlCell<String> cell = new SimpleSafeHtmlCell<String>(SimpleSafeHtmlRenderer.getInstance(), "click") {
		      @Override
		      public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
		          ValueUpdater<String> valueUpdater) {
		        super.onBrowserEvent(context, parent, value, event, valueUpdater);
		        if ("click".equals(event.getType())) {
		        	
		        	OwlClassBean bean = tree.getSelectionModel().getSelectedItem();
		        	if(bean != null) {
		        	
		        		Info.display("Click", "You clicked \"" + value + "\"! Tree selectedItem: " 
		        		  	+ bean.getName());
		        		
		        		System.out.println("Processing click: updating instance view for  " + bean.getName());
		        		updateInstancesView(bean);	
		        	}
		          
		          
		        }
		        
		      }
		    };
		tree.setCell(cell);
	}

	
}
