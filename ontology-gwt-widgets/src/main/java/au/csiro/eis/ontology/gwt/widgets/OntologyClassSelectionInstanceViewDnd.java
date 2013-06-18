package au.csiro.eis.ontology.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlIndividualBeanProperties;
import au.csiro.eis.ontology.gwt.widgets.xtemplates.OwlIndividualRenderer;


import com.google.gwt.core.shared.GWT;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;


/**
 * LayoutContainer for a set of DnD entities relating to the class
 * @author yu021
 *
 */
public class OntologyClassSelectionInstanceViewDnd extends OntologyClassViewContainer {
	OntologyQueryServiceAsync ontologyQueryService;


	ContentPanel classViewPanel; 

	ListStore<OwlIndividualBean> individualsListStore;
	ListView<OwlIndividualBean,OwlIndividualBean> individualsView;
	
	OwlIndividualBeanProperties indivProps;

	
	public OntologyClassSelectionInstanceViewDnd(OntologyQueryServiceAsync ontologyQueryService)  {
		this.ontologyQueryService = ontologyQueryService;
		classBean = null;
		classViewPanel = null;

		indivProps = GWT.create(OwlIndividualBeanProperties.class);

		
		this.individualsListStore = new ListStore<OwlIndividualBean>(indivProps.key());
		//this.individualsView = getListView(this.individualsListStore, "name", indivProps.name());
		//this.individualsView = ListViewFactory.createListView(individualsListStore, indivProps.name());
		this.individualsView = ListViewFactory.createListView(individualsListStore);
		this.individualsView.setCell(new SimpleSafeHtmlCell<OwlIndividualBean>(new AbstractSafeHtmlRenderer<OwlIndividualBean>() {
		     public SafeHtml render(OwlIndividualBean object) {
		       OwlIndividualRenderer r = GWT.create(OwlIndividualRenderer.class);
		       return r.render(object);
		     }
		 }));
		
		
		this.initClassViewPanel();
	}


	public Widget asWidget() {  
		//super.onRender(parent, index);  
		//this.setLayout(new FlowLayout());

		
		if(classBean != null) {
			initClassViewPanel();

			updateClassViewPanel();
		}  

		return this.classViewPanel;
		
		
		//return con;
	}

	

	public void initClassViewPanel() {
		if(this.classViewPanel == null) {
			this.classViewPanel = new ContentPanel();
		}

		//this.classViewPanel.setLayout(new RowLayout(Orientation.VERTICAL));
		//this.classViewPanel.setLayout(new FlowLayout());
		//this.classViewPanel.setLayout(new FitLayout());
		//classViewPanel.setFrame(false);
		this.classViewPanel.setHeaderVisible(false);
		
		//this.add(this.classViewPanel);
	}


	public void updateClassViewPanel() {
		System.out.println("Updating class view panel ");
		if(this.classBean == null) {
			return;
		}
		
		if(this.classViewPanel != null) { 
			this.classViewPanel.clear();
		}
		else {
			initClassViewPanel();
		}
		
		
		boolean hasIndiv = this.classBean.hasIndividuals();
		boolean isLoadIndiv = this.classBean.isLoadIndividuals();
		if(hasIndiv ) {
			System.out.println("This class has individuals...");
			if(isLoadIndiv) {
				System.out.println("This class has LOADED individuals...");
				List<OwlIndividualBean> indivList = this.classBean.getIndividuals();

				System.out.println("Updating with individuals list");
				update(indivList);
			}
			else {
				System.out.println("This class has NOT LOADED individuals...");
				AsyncCallback<List<OwlIndividualBean>> callback = new AsyncCallback<List<OwlIndividualBean>>() {

					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub
						
					}

					public void onSuccess(List<OwlIndividualBean> result) {
						// TODO Auto-generated method stub
						update(result);
						
					}
					
				};
				System.out.println("Querying individuals for " +  this.classBean.getName()  + "...");
				this.ontologyQueryService.getOntIndividualsForClass(this.classBean.getIri(), true, callback );
			}

		}
		else {
			System.out.println("This class does NOT have individuals...");

		}
		
		
		//this.classViewPanel.add(cp1);

		

	}
	
	public void update(List<OwlIndividualBean> indivList) {
		System.out.println("Num indiv: " + indivList.size());
		
		//this.restrictrictionsListStore = new ListStore<ModelData>();
		
		this.individualsListStore.clear();
		
		for(OwlIndividualBean i : indivList) {
			//System.out.println("Adding Indiv: " + i.getLabel());
			System.out.println("Adding Indiv: " + i.getName());
			this.individualsListStore.add(i);			
		}
		
		this.individualsView.refresh();
		
		//ListView<ModelData> restrictionsView = getListView(listStore, "name");
		
		if(indivList!=null) {
			this.classViewPanel.clear();
			this.classViewPanel.add(individualsView);
		}
		/*
		for(ModelData indiv : indivList) {

			cp2.add(new Label("Inst: " + indiv.get("name")));

		} */
		//this.layout();
	}
	
	/* deprecated
	public List<BeanModel> convertClassesToBeanModel(List<OwlClassBean> cList) {
		List<BeanModel> bList = this.classBeanFactory.createModel(cList);
		return bList;
	}

	public List<BeanModel> convertIndividualsToBeanModel(List<OwlIndividualBean> iList) {
		List<BeanModel> bList = this.indivBeanFactory.createModel(iList);
		return bList;
	}
	*/
	
	

/*
	public ListView<BeanModel> getListView(ListStore<BeanModel> listStore, String[] propList) {

	    ListView<BeanModel> view = new ListView<BeanModel>();  
	    view.setBorders(true);  
	    view.setStore(listStore);  

	    String templateStr= "";
	    int counter = 0;
	    for(String p:propList) {
	    	if(counter > 0) {
	    		templateStr = templateStr + ",";	
	    	}
	    	templateStr = templateStr + "{" + p +  "}";
	    	counter++;
	    }

	    System.out.println("prop template ("+ counter + "): " + templateStr);
	    view.setSimpleTemplate(templateStr);  
	    //view.setDisplayProperty(prop);  
	    ListViewDragSource dragTarget = new ListViewDragSource(view);
	    //dragTarget.setOperation(DND.Operation.COPY);
	    
	    return view;
	}

*/

	public ListStore<OwlIndividualBean> getIndividualsListStore() {
		return individualsListStore;
	}


	public void setIndividualsListStore(ListStore<OwlIndividualBean> individualsListStore) {
		this.individualsListStore = individualsListStore;
	}


	public ListView<OwlIndividualBean,OwlIndividualBean> getIndividualsView() {
		return individualsView;
	}


	public void setIndividualsView(ListView<OwlIndividualBean,OwlIndividualBean> individualsView) {
		this.individualsView = individualsView;
	}


	
	
}


