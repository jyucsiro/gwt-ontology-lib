package au.csiro.eis.ontology.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.FramedPanel;

public class OntologyTreeBrowserContentPanel implements IsWidget {
	OntologyQueryServiceAsync ontologyQueryService;
	List<String> iri_list = new ArrayList<String>();

	//Listener<TreePanelEvent<OwlClassBean>> classBrowserClickListener;
	
	OntologyTreeBrowser treeLayout ;
	
	int TREE_PANEL_WIDTH;
	int TREE_PANEL_HEIGHT;	
	
	public OntologyTreeBrowserContentPanel(OntologyQueryServiceAsync ontologyQueryService, List<String> iri_list) {
		this.iri_list = iri_list;

		this.ontologyQueryService = ontologyQueryService;
		
		try {
			this.treeLayout = new OntologyTreeBrowser(this.ontologyQueryService, this.iri_list);
		} catch (OntologyInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Widget asWidget() {
		//super.onRender(parent, index);  
		
		//this.setHeaderVisible(true);
		//this.setHeading("Model browser");
		//this.setAutoHeight(true);

		//this.setScrollMode(Scroll.AUTOY);  
		
		ContentPanel panel = new ContentPanel();
		
		panel.setCollapsible(true);
		panel.setHeaderVisible(false);

		panel.setBodyStyle("backgroundColor: white;");

		this.treeLayout.setSize(this.TREE_PANEL_WIDTH, this.TREE_PANEL_HEIGHT);
		panel.add(treeLayout);  

		return panel;
	}

	
	public void setSize(int width, int height) {
		this.TREE_PANEL_WIDTH = width;
		this.TREE_PANEL_HEIGHT = height;
	}

	
	
	
	/* TODO: handle click listener 
	public void addTreeClickListener(Listener<TreePanelEvent<OwlClassBean>> listener) {
		if(this.treeLayout != null) {
			this.treeLayout.addClickListener(listener);
		}
	}
	*/

	public void addTreeSelectHandler(
			SelectionHandler<OwlClassBean> defaultSelectionHandler) {
		if(this.treeLayout != null) {
			this.treeLayout.getTree().getSelectionModel().addSelectionHandler(defaultSelectionHandler);
		}		
	}

	
	
	public OntologyQueryServiceAsync getOntologyQueryService() {
		return ontologyQueryService;
	}

	public void setOntologyQueryService(
			OntologyQueryServiceAsync ontologyQueryService) {
		this.ontologyQueryService = ontologyQueryService;
	}

	public List<String> getIri_list() {
		return iri_list;
	}

	public void setIri_list(List<String> iri_list) {
		this.iri_list = iri_list;
	}

	/*
	public Listener<TreePanelEvent<OwlClassBean>> getClassBrowserClickListener() {
		return classBrowserClickListener;
	}
	*/

	public OntologyTreeBrowser getTreeLayout() {
		return treeLayout;
	}

	public void setTreeLayout(OntologyTreeBrowser treeLayout) {
		this.treeLayout = treeLayout;
	}

	public int getTREE_PANEL_WIDTH() {
		return TREE_PANEL_WIDTH;
	}

	public void setTREE_PANEL_WIDTH(int tREE_PANEL_WIDTH) {
		TREE_PANEL_WIDTH = tREE_PANEL_WIDTH;
	}

	public int getTREE_PANEL_HEIGHT() {
		return TREE_PANEL_HEIGHT;
	}

	public void setTREE_PANEL_HEIGHT(int tREE_PANEL_HEIGHT) {
		TREE_PANEL_HEIGHT = tREE_PANEL_HEIGHT;
	}

	
	
	
}
