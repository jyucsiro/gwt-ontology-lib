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
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class OntologySubclassComboBox implements IsWidget {
	ComboBox<OwlClassBean> subclassesComboBox;
	String parentClassIri;
	OntologyQueryServiceAsync ontologyQueryService;
	
	public static String NAME_MODE = "NAME";
	public static String LONG_LABEL_MODE = "LONG_LABEL";
	

	OwlClassBeanKeyProvider kp = new OwlClassBeanKeyProvider();
	//OwlClassBeanProperties props = GWT.create(OwlClassBeanKeyProvider.class);
	
	public OntologySubclassComboBox(final String parentClassIri, final OntologyQueryServiceAsync ontologyQueryService) {	
		this.create(parentClassIri, ontologyQueryService, NAME_MODE); //default
	}

	public OntologySubclassComboBox(final String parentClassIri, final OntologyQueryServiceAsync ontologyQueryService, String mode) {
		this.create(parentClassIri, ontologyQueryService, mode); //default	
	}
	
	
	private void create(final String parentClassIri, final OntologyQueryServiceAsync ontologyQueryService, String mode) {
		this.parentClassIri = parentClassIri;
		this.ontologyQueryService = ontologyQueryService;
	
		
		System.out.println("Adding subclasses combobox to render!");
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
		
		subclassesComboBox = new ComboBox<OwlClassBean>(store, props.nameLabel()); 
		  
		/* TODO implement the modes 
		if(mode == NAME_MODE) {
			subclassesComboBox.setDisplayField("name");   
			subclassesComboBox.setName("Name"); 
			subclassesComboBox.setFieldLabel("Name"); 
		}
		else if(mode == LONG_LABEL_MODE) {
			subclassesComboBox.setDisplayField("longLabel");   
			subclassesComboBox.setName("Name"); 
			subclassesComboBox.setFieldLabel("Name"); 
		}
		else {
			subclassesComboBox.setDisplayField("name");   
			subclassesComboBox.setName("Name"); 
			subclassesComboBox.setFieldLabel("Name"); 
		}
		*/
		//subclassesComboBox.setWidth(150); 
		subclassesComboBox.setAllowBlank(false); 
		subclassesComboBox.setEditable(false); 
		subclassesComboBox.setStore(store);
		
		subclassesComboBox.setTriggerAction(TriggerAction.ALL);
	    
		
		AsyncCallback<OwlClassBean> callback = new AsyncCallback<OwlClassBean>() {
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			public void onSuccess(OwlClassBean result) {
				// TODO Auto-generated method stub
				subclassesComboBox.setEmptyText("Select a " + result.getLocalName());		
			}
		};
		
		ontologyQueryService.getOntClass(parentClassIri, callback);
	}
		
	/* TODO fix listener to handlers
	public void addListener(SelectionChangedListener<BeanModel> listener) {
		this.subclassesComboBox.addSelectionChangedListener(listener);
	}
	*/

	public ComboBox<OwlClassBean> getSubclassesComboBox() {
		return subclassesComboBox;
	}

	public void setSubclassesComboBox(ComboBox<OwlClassBean> subclassesComboBox) {
		this.subclassesComboBox = subclassesComboBox;
	}

	public Widget asWidget() {

		return this.subclassesComboBox;
	}	

	public void setWidth(int width) {
		this.subclassesComboBox.setWidth(width);
	}
	
}
