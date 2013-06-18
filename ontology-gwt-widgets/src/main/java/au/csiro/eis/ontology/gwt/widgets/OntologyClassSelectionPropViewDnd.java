package au.csiro.eis.ontology.gwt.widgets;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlRestrictionBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlRestrictionBeanProperties;
import au.csiro.eis.ontology.gwt.widgets.xtemplates.OwlRestrictionRenderer;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.text.shared.AbstractSafeHtmlRenderer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.IdentityValueProvider;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;


/**
 * LayoutContainer for a set of DnD entities relating to the class
 * @author yu021
 *
 */
public class OntologyClassSelectionPropViewDnd extends OntologyClassViewContainer {
	OntologyQueryServiceAsync ontologyQueryService;

	
	ContentPanel classViewPanel; 

	ListStore<OwlRestrictionBean> restrictionsListStore;
	ListView<OwlRestrictionBean,OwlRestrictionBean> restrictionsView;
	OwlRestrictionBeanProperties restrProps;

	
	public OntologyClassSelectionPropViewDnd(OntologyQueryServiceAsync ontologyQueryService)  {
		this.ontologyQueryService = ontologyQueryService;
		classBean = null;
		classViewPanel = null;
		
		restrProps = GWT.create(OwlRestrictionBeanProperties.class);

		
		this.restrictionsListStore = new ListStore<OwlRestrictionBean>(restrProps.key());
		//this.restrictionsView = ListViewFactory.createListView(restrictionsListStore, new String[]{"property", "filler"}, restrProps.property());
		this.restrictionsView = getListView(this.restrictionsListStore);
		initClassViewPanel();
	}
	
	public ListView<OwlRestrictionBean, OwlRestrictionBean> getListView(ListStore<OwlRestrictionBean> store) {
		final OwlRestrictionRenderer r = GWT.create(OwlRestrictionRenderer.class);
	    

		ListView<OwlRestrictionBean, OwlRestrictionBean> view = ListViewFactory.createListView(store);
		view.setCell(new SimpleSafeHtmlCell<OwlRestrictionBean>(new AbstractSafeHtmlRenderer<OwlRestrictionBean>() {
			 
		      public SafeHtml render(OwlRestrictionBean object) {
		        return r.render(object);
		      }
		    }));
		
		return view;
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
		this.classViewPanel.setHeaderVisible(false);
		
		
		//this.add(this.classViewPanel);
	}


	public void updateClassViewPanel() {
		if(this.classBean == null) {
			return;
		}
		
		if(this.classViewPanel != null) { 
			this.classViewPanel.clear();
		}
		else {
			initClassViewPanel();
		}
		
		
		boolean hasRestrictions = this.classBean.hasRestrictions();
		if(hasRestrictions) {
			List<OwlRestrictionBean> rList = this.classBean.getRestrictions();
			
			this.restrictionsListStore.clear();
			this.restrictionsListStore.addAll(rList);
			this.restrictionsView.refresh();
			
			
			this.classViewPanel.add(restrictionsView);
		}
		
		
		//this.classViewPanel.add(cp1);

		//this.layout();

	}
	
	
	public void addViewSelectHandler(SelectionHandler<OwlRestrictionBean> handler) {
		this.restrictionsView.getSelectionModel().addSelectionHandler(handler);
	}
	
	/*
	public List<BeanModel> convertClassesToBeanModel(List<OwlClassBean> cList) {
		List<BeanModel> bList = this.classBeanFactory.createModel(cList);
		return bList;
	}
	
	public List<BeanModel> convertRestrictionsToBeanModel(List<OwlRestrictionBean> rList) {
		List<BeanModel> bList = this.restrictionBeanFactory.createModel(rList);
		return bList;
	}
	*/


	/*
	public ListView<BeanModel> getListView(ListStore<BeanModel> listStore, String prop) {

	    ListView<BeanModel> view = new ListView<BeanModel>();  
	    view.setBorders(true);  
	    view.setStore(listStore);  
	    view.setSimpleTemplate("{" + prop + "}");  
	    view.setDisplayProperty(prop);  
	    ListViewDragSource dragTarget = new ListViewDragSource(view);
	    //dragTarget.setOperation(DND.Operation.COPY);
	    
	    return view;
	}

	public ListView<BeanModel> getListView(ListStore<BeanModel> listStore, String[] propList) {

	    ListView<BeanModel> view = new ListView<BeanModel>();  
	    view.setBorders(true);  
	    view.setStore(listStore);  

	    String templateStr= "";
	    int counter = 0;
	    for(String p:propList) {
	    	if(counter > 0) {
	    		templateStr = templateStr + ": ";	
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

	public ListStore<OwlRestrictionBean> getRestrictionsListStore() {
		return restrictionsListStore;
	}


	public void setRestrictionsListStore(ListStore<OwlRestrictionBean> restrictionsListStore) {
		this.restrictionsListStore = restrictionsListStore;
	}


	public ListView<OwlRestrictionBean,OwlRestrictionBean> getRestrictionsView() {
		return restrictionsView;
	}


	public void setRestrictionsView(ListView<OwlRestrictionBean,OwlRestrictionBean> restrictionsView) {
		this.restrictionsView = restrictionsView;
	}

	
	

}


