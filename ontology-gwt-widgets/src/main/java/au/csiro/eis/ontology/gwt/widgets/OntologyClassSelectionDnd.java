package au.csiro.eis.ontology.gwt.widgets;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.beans.OwlRestrictionBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlIndividualBeanProperties;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlRestrictionBeanProperties;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;


/**
 * LayoutContainer for a set of DnD entities relating to the class
 * @author yu021
 *
 */
public class OntologyClassSelectionDnd extends OntologyClassViewContainer {
	OntologyQueryServiceAsync ontologyQueryService;

	
	OwlClassBean classBean;
	ContentPanel classViewPanel; 

	ListStore<OwlRestrictionBean> restrictionsListStore;;
	ListView<OwlRestrictionBean,String> restrictionsView;
	
	ListStore<OwlIndividualBean> individualsListStore;;
	ListView<OwlIndividualBean,String> individualsView;
	
	OwlIndividualBeanProperties indivProps;
	OwlRestrictionBeanProperties restrProps;

	public OntologyClassSelectionDnd(OntologyQueryServiceAsync ontologyQueryService)  {
		this.ontologyQueryService = ontologyQueryService;
		classBean = null;
		classViewPanel = null;
		indivProps = GWT.create(OwlIndividualBeanProperties.class);
		restrProps = GWT.create(OwlRestrictionBeanProperties.class);

		this.initClassViewPanel();
	}


	public Widget asWidget() {  
		//super.onRender(parent, index);  
		//this.setLayout(new FlowLayout());

		
		if(classBean != null) {
			initClassViewPanel();

			updateClassViewPanel();
		}  

		FlowLayoutContainer con = new FlowLayoutContainer();
		con.add(this.classViewPanel);
		
		return con;
	}

	

	public void initClassViewPanel() {
		if(this.classViewPanel == null) {
			this.classViewPanel = new ContentPanel();
		}

		//this.classViewPanel.setLayout(new RowLayout(Orientation.VERTICAL));
		//this.classViewPanel.setLayout(new FlowLayout());
		//this.classViewPanel.setLayout(new FitLayout());
		//classViewPanel.setFrame(false);
		
		this.restrictionsListStore = new ListStore<OwlRestrictionBean>(restrProps.key());
		this.restrictionsView = getListView(restrictionsListStore, new String[]{"property", "filler"}, restrProps.property());
		
		this.individualsListStore = new ListStore<OwlIndividualBean>(indivProps.key());
		this.individualsView = getListView(this.individualsListStore, "name", indivProps.name());
		
		//this.add(this.classViewPanel);
	}


	public void setOwlClassBean(OwlClassBean c) {
		this.classBean = c;
	}

	public void updateClassViewPanel() {
		if(this.classBean == null) {
			return;
		}
		
		String className =  this.classBean.getName();
		if(this.classViewPanel != null) { 
			this.classViewPanel.clear();
		}
		else {
			initClassViewPanel();
		}
		
		this.classViewPanel.setHeadingText("Selected class: " + className);
		
		//RowData data = new RowData(1, 1);  
		//data.setMargins(new Margins(1));  

		VerticalPanel vp = new VerticalPanel();
		//vp.setSpacing(5);
		
		ContentPanel cp1 = new ContentPanel();
		cp1.setHeaderVisible(false);
		
		boolean hasRestrictions = this.classBean.hasRestrictions();
		if(hasRestrictions) {
			List<OwlRestrictionBean> rList = this.classBean.getRestrictions();
			
			this.restrictionsListStore.clear();
			this.restrictionsListStore.addAll(rList);
			this.restrictionsView.refresh();
			
			
			cp1.add(restrictionsView);
		}
		
		ContentPanel cp2 = new ContentPanel();
		cp2.setHeaderVisible(false);
		
		//cp2.setHeading("Instances");

		boolean hasIndiv = this.classBean.hasIndividuals();
		if(hasIndiv) {
			List<OwlIndividualBean> indivList = this.classBean.getIndividuals();

			//this.restrictrictionsListStore = new ListStore<ModelData>();
			
			this.individualsListStore.clear();
			this.individualsListStore.addAll(indivList);
			this.individualsView.refresh();
			
			//ListView<ModelData> restrictionsView = getListView(listStore, "name");
			cp2.add(individualsView);
			/*
			for(ModelData indiv : indivList) {

				cp2.add(new Label("Inst: " + indiv.get("name")));

			} */

		}

		
		//vp.add(new Label("Name: " + className));
		vp.add(new Label("Properties: "));
		vp.add(cp1);
		if(hasIndiv) {
			vp.add(new Label("Instances"));
			vp.add(cp2);
		
		}

		this.classViewPanel.add(vp);

		//this.layout();

	}
	
	
	


	public <M> ListView<M,String> getListView(ListStore<M> listStore, String prop, ValueProvider<M,String> prov) {

	    ListView<M,String> view = new ListView<M,String>(listStore, prov);  
	    view.setBorders(true);  
	    //view.setSimpleTemplate("{" + prop + "}");  
	    //view.setDisplayProperty(prop);  
	    ListViewDragSource<M> dragTarget = new ListViewDragSource<M>(view);
	    //dragTarget.setOperation(DND.Operation.COPY);
	    
	    return view;
	}

	public <M> ListView<M,String> getListView(ListStore<M> listStore, String[] propList,ValueProvider<M,String> prov) {

	    ListView<M,String> view = new ListView<M,String>(listStore, prov);  
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
	    //view.setSimpleTemplate(templateStr);  
	    //view.setDisplayProperty(prop);  
	   // ListViewDragSource dragTarget = new ListViewDragSource(view);
	    //dragTarget.setOperation(DND.Operation.COPY);
	    
	    return view;
	}


}


