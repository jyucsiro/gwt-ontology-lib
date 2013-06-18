package au.csiro.eis.ontology.gwt.widgets;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.Element;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;

public class OntologySelectedClassViewFromTreepanelContentPanel extends FlowLayoutContainer {
	OntologyQueryServiceAsync ontologyQueryService;
	OntologyClassViewContainer classViewSelection; 
	//Listener<TreePanelEvent<BeanModel>> classBrowserClickListener;
	
	public OntologySelectedClassViewFromTreepanelContentPanel(OntologyQueryServiceAsync ontologyQueryService, OntologyClassViewContainer classViewSelection) {
		this.ontologyQueryService = ontologyQueryService;
		this.classViewSelection = classViewSelection;
		
		//this.setScrollMode(Scroll.AUTOY);		

		//this.setHeaderVisible(false);
		//this.setFrame(true);

		this.add(this.classViewSelection);

		
		/*
		//this.setHeaderVisible(false);
		
		//this.setBodyStyle("backgroundColor: white;");
		
		
		*/
	}

	public SelectionHandler<OwlClassBean> getDefaultSelectionHandler() {
		return new SelectionHandler<OwlClassBean>() {

			public void onSelection(SelectionEvent<OwlClassBean> event) {
				OwlClassBean c = event.getSelectedItem();
				if(c!= null) {
					classViewSelection.setOwlClassBean(c);
					classViewSelection.updateClassViewPanel();
				
					GWT.log("Selected class: " + c.getName());
				}
				//layout();
				
			}
			
		};
	}
		
	
	/*
	public Listener<TreePanelEvent<BeanModel>> getDefaultTreeClickListener() {
		return new Listener<TreePanelEvent<BeanModel>>() {
			public void handleEvent(TreePanelEvent<BeanModel> be) {

				if(be.getItem() != null) {
					BeanModel b = be.getItem();

					OwlClassBean c = (OwlClassBean) b.getBean();	
					classViewSelection.setOwlClassBean(c);
					classViewSelection.updateClassViewPanel();
					
					GWT.log("Selected class: " + c.getName());
					
					layout();
				}
			}
		};
		
	}
	*/
	
	
	public void showSelectedClass(OwlClassBean c) {
		classViewSelection.setOwlClassBean(c);
		classViewSelection.updateClassViewPanel();
		
		GWT.log("Selected class: " + c.getName());
		System.out.println("Selected class: " + c.getName());
		
		//layout();
	}
	
	

	
	public OntologyQueryServiceAsync getOntologyQueryService() {
		return ontologyQueryService;
	}

	public void setOntologyQueryService(
			OntologyQueryServiceAsync ontologyQueryService) {
		this.ontologyQueryService = ontologyQueryService;
	}

	public OntologyClassViewContainer getClassViewSelection() {
		return classViewSelection;
	}


	/*
	public Listener<TreePanelEvent<BeanModel>> getClassBrowserClickListener() {
		return classBrowserClickListener;
	}

	public void setClassBrowserClickListener(
			Listener<TreePanelEvent<BeanModel>> classBrowserClickListener) {
		this.classBrowserClickListener = classBrowserClickListener;
	}
	*/
	
}
