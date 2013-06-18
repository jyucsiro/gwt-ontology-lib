package au.csiro.eis.ontology.gwt.widgets;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlClassBeanKeyProvider;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlClassBeanProperties;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.loader.ListStoreBinding;
import com.sencha.gxt.data.shared.loader.Loader;
import com.sencha.gxt.widget.core.client.ListView;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class OntologyClassesListView implements IsWidget {
	OntologyQueryServiceAsync ontologyQueryService;
	String parentClassIri;
	ListView<OwlClassBean, String> listView;
	
	OwlClassBeanKeyProvider kp = new OwlClassBeanKeyProvider();
	
	public static String NAME_MODE = "NAME";
	public static String LONG_LABEL_MODE = "LONG_LABEL";

	
	public OntologyClassesListView(final String parentClassIri, final OntologyQueryServiceAsync ontologyQueryService) {	
		this.create(parentClassIri, ontologyQueryService, NAME_MODE); //default
	}

	public OntologyClassesListView(final String parentClassIri, final OntologyQueryServiceAsync ontologyQueryService, String mode) {
		this.create(parentClassIri, ontologyQueryService, mode); //default	
	}
	
	
	private void create(final String parentClassIri, final OntologyQueryServiceAsync ontologyQueryService, String mode) {
		this.parentClassIri = parentClassIri;
		this.ontologyQueryService = ontologyQueryService;
	
		
		System.out.println("Creating ontology list view!");
		RpcProxy<OwlClassBean,List<OwlClassBean>> md_proxy = new RpcProxy<OwlClassBean,List<OwlClassBean>>() {  
			@Override  
			public void load(OwlClassBean loadConfig, AsyncCallback<List<OwlClassBean>> callback) {  
				//String iriToUse = SsnConstants.OBSERVATION;
				String iriToUse = parentClassIri;
				
				boolean isDirect = true;
				boolean loadIndiv = true;
				boolean loadParents = false;
				boolean loadChildren = true;
				
				ontologyQueryService.getOntSubClasses(iriToUse, isDirect, loadIndiv, loadParents, loadChildren, callback);

			}  
		};  
		Loader<OwlClassBean,List<OwlClassBean>> loader = new Loader<OwlClassBean,List<OwlClassBean>>(md_proxy);
		ListStore<OwlClassBean> store = new ListStore<OwlClassBean>(kp);
	    loader.addLoadHandler(new ListStoreBinding<OwlClassBean, OwlClassBean, List<OwlClassBean>>(store));


		loader.load();
		OwlClassBeanProperties props = GWT.create(OwlClassBeanProperties.class);
		
		
		
		listView = new ListView<OwlClassBean, String>(store, props.name()); 
		listView.setStore(store);
		
	    
		
		AsyncCallback<OwlClassBean> callback = new AsyncCallback<OwlClassBean>() {
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			public void onSuccess(OwlClassBean result) {
				// TODO Auto-generated method stub
						
			}
		};
		
		ontologyQueryService.getOntClass(parentClassIri, false, false, false, callback);
	}

	public Widget asWidget() {
		//this.create(parentClassIri, ontologyQueryService, NAME_MODE);
		return this.listView;
	}

	public ListView<OwlClassBean, String> getListView() {
		return listView;
	}

	public void setListView(ListView<OwlClassBean, String> listView) {
		this.listView = listView;
	}

	
	
}
