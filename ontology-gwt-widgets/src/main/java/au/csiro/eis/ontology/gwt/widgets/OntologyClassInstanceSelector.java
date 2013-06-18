package au.csiro.eis.ontology.gwt.widgets;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlIndividualBeanProperties;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.dnd.core.client.ListViewDragSource;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;


/**
 * LayoutContainer for a set of DnD entities relating to the class
 * @author yu021
 *
 */
public class OntologyClassInstanceSelector extends OntologyClassViewContainer {
	OntologyQueryServiceAsync ontologyQueryService;
	OwlClassBean classBean;
	ContentPanel classViewPanel; 
	ButtonBar toolBar;
	Button addPropConstraintBtn;
	OwlIndividualBeanProperties props;

	boolean hasFnButtonBar;
	
	ListStore<OwlIndividualBean> individualsListStore;;
	ListView<OwlIndividualBean, String> individualsView;
	
	public OntologyClassInstanceSelector(OntologyQueryServiceAsync ontologyQueryService)  {
		this.ontologyQueryService = ontologyQueryService;
		classBean = null;
		classViewPanel = null;
		toolBar = null;
		this.hasFnButtonBar = false;
		props = GWT.create(OwlIndividualBeanProperties.class);
		this.initClassViewPanel();
	}

	public OntologyClassInstanceSelector(OntologyQueryServiceAsync ontologyQueryService, boolean hasButtonBar)  {
		this.ontologyQueryService = ontologyQueryService;
		classBean = null;
		classViewPanel = null;
		toolBar = null;
		
		this.hasFnButtonBar = hasButtonBar;
		props = GWT.create(OwlIndividualBeanProperties.class);
		this.initClassViewPanel();
	}


	public Widget asWidget() {  
		//super.onRender(parent, index);  
		if(classBean != null) {
			initClassViewPanel();
			updateClassViewPanel();
		}  
		
		FlowLayoutContainer con = new FlowLayoutContainer();
		con.add(this.classViewPanel);
		
		return con;
	}


	public OwlIndividualBean getSelectedIndividual() {
		OwlIndividualBean selected = this.individualsView.getSelectionModel().getSelectedItem();
		
		if(selected != null) {
			return selected;
		}
		
		return null;		
	}

	public void initClassViewPanel() {
		if(this.classViewPanel == null) {
			this.classViewPanel = new ContentPanel();
		}

		//this.classViewPanel.setLayout(new RowLayout(Orientation.VERTICAL));
		//this.classViewPanel.setLayout(new FlowLayout());
		//this.classViewPanel.setLayout(new FitLayout());
		//classViewPanel.setFrame(false);
		
		this.individualsListStore = new ListStore<OwlIndividualBean>(this.props.key());
		this.individualsView = getListView(this.individualsListStore, "name");

		/*
		this.individualsView.getSelectionModel().addSelectionChangedListener(new SelectionChangedListener<BeanModel>() {

			@Override
			public void selectionChanged(SelectionChangedEvent<BeanModel> se) {
				se.getSelectedItem();
				
			}
			
		});
		*/
		
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
		
		ContentPanel cp2 = new ContentPanel();
		cp2.setHeaderVisible(false);
		
		//cp2.setHeading("Instances");

		boolean hasIndiv = this.classBean.hasIndividuals();
		boolean loadIndiv = this.classBean.isLoadIndividuals();
		if(hasIndiv && loadIndiv) {
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

		if(hasIndiv) {
			vp.add(new Label("Instances"));
			vp.add(cp2);
		}
		else {
			vp.add(new Label("No instances for selected class"));
		}

		this.classViewPanel.add(vp);
		
		//add buttons for Toolbar
		toolBar = getButtonBar();
		this.classViewPanel.add(toolBar);

		//this.layout();

	}
	
	
	
	
	public ButtonBar getButtonBar() {
	
		if(this.toolBar != null) {
			return this.toolBar;
		}
		
		ButtonBar toolBar = new ButtonBar();
		return toolBar;
	}
	
	public void setFnButtonBar(ButtonBar toolBar) {
		this.toolBar  = toolBar;
	}
	

	public ListView<OwlIndividualBean, String> getListView(ListStore<OwlIndividualBean> listStore, String prop) {

	    ListView<OwlIndividualBean, String> view = new ListView<OwlIndividualBean, String>(listStore, props.name());  
	    view.setBorders(true);  
	    //view.setSimpleTemplate("{" + prop + "}");  
	    //view.setDisplayProperty(prop);  
	    ListViewDragSource<OwlIndividualBean> dragTarget = new ListViewDragSource<OwlIndividualBean>(view);
	    //dragTarget.setOperation(DND.Operation.COPY);
	    
	    return view;
	}

	public ListView<OwlIndividualBean,String> getListView(ListStore<OwlIndividualBean> listStore, String[] propList) {

	    ListView<OwlIndividualBean, String> view = new ListView<OwlIndividualBean,String>(listStore, props.name());  
	    view.setBorders(true);  

	    String templateStr= "";
	    int counter = 0;
	    for(String p:propList) {
	    	if(counter > 0) {
	    		templateStr = templateStr + ",";	
	    	}
	    	templateStr = templateStr + "{" + p +  "}";
	    	counter++;
	    }

	    //System.out.println("prop template ("+ counter + "): " + templateStr);
	    //view.setSimpleTemplate(templateStr);  
	    //view.setDisplayProperty(prop);  
	    ListViewDragSource<OwlIndividualBean> dragTarget = new ListViewDragSource<OwlIndividualBean>(view);
	    //dragTarget.setOperation(DND.Operation.COPY);
	    
	    return view;
	}

	public OwlClassBean getClassBean() {
		return classBean;
	}

	public void setClassBean(OwlClassBean classBean) {
		this.classBean = classBean;
	}

	public ButtonBar getToolBar() {
		return toolBar;
	}

	public void setToolBar(ButtonBar toolBar) {
		this.toolBar = toolBar;
	}

	public ListStore<OwlIndividualBean> getIndividualsListStore() {
		return individualsListStore;
	}

	public void setIndividualsListStore(ListStore<OwlIndividualBean> individualsListStore) {
		this.individualsListStore = individualsListStore;
	}

	public ListView<OwlIndividualBean,String> getIndividualsView() {
		return individualsView;
	}

	public void setIndividualsView(ListView<OwlIndividualBean,String> individualsView) {
		this.individualsView = individualsView;
	}

	
	


}


