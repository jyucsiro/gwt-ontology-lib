package au.csiro.eis.ontology.gwt.widgets;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

public class OntologySelectedClassViewFromListContentPanel extends FlowLayoutContainer {
	OntologyQueryServiceAsync ontologyQueryService;
	OntologyClassViewContainer classViewSelection; 
	//Listener<ListViewEvent<BeanModel>> classBrowserClickListener;
	
	public OntologySelectedClassViewFromListContentPanel(OntologyQueryServiceAsync ontologyQueryService, OntologyClassViewContainer classViewSelection) {
		this.ontologyQueryService = ontologyQueryService;
		this.classViewSelection = classViewSelection;
	}

	@Override  
	public Widget asWidget(){
		//super.onRender(parent, index);  
		//this.setScrollMode(Scroll.AUTOY);		
		//this.setLayout(new FitLayout());
		this.add(this.classViewSelection);
		return this;
	}
	
	public SelectionHandler<OwlClassBean> getDefaultListViewHandler() {
		return new SelectionHandler<OwlClassBean>() {
			public void onSelection(SelectionEvent<OwlClassBean> event) {
				// TODO Auto-generated method stub
				
				if(event.getSelectedItem() != null ) {
					
					OwlClassBean c = event.getSelectedItem();
					
					showSelectedClass(c);
					//classViewSelection.setOwlClassBean(c);
					//classViewSelection.updateClassViewPanel();
					
					//GWT.log("Selected class: " + c.getName());
					
					//layout();
				}	
				
			}
		};
		
	}
	
	
	public void showSelectedClass(OwlClassBean c) {
		this.classViewSelection.setOwlClassBean(c);
		this.classViewSelection.updateClassViewPanel();
		
		GWT.log("Selected class: " + c.getName());
		
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

	
	
}
