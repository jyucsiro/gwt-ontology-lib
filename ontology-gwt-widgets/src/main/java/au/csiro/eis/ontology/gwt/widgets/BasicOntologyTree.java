package au.csiro.eis.ontology.gwt.widgets;

import java.util.List;


import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlClassBeanProperties;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class BasicOntologyTree implements IsWidget, EntryPoint {
	OntologyQueryServiceAsync ontologyService;
	final String OWL_THING_IRI = "http://www.w3.org/2002/07/owl#Thing";
	class KeyProvider implements ModelKeyProvider<OwlClassBean> {
		public String getKey(OwlClassBean item) {

			return item.getIri();
		}
	}
	
	public Tree<OwlClassBean, String> getTree() {
		RpcProxy<OwlClassBean, List<OwlClassBean>> rpcProxy = new RpcProxy<OwlClassBean,List<OwlClassBean>>() {
			@Override
			public void load(OwlClassBean loadConfig,
					AsyncCallback<List<OwlClassBean>> callback) {
				String iri_string = OWL_THING_IRI;

				if(loadConfig != null) {
					iri_string = loadConfig.getIri();
				}
				System.out.println("iri_string in rpc: " + iri_string);

				boolean isDirect = true;
				boolean loadIndiv = true;
				boolean loadParents = false;
				boolean loadChildren = true;
				
				ontologyService.getOntSubClasses(iri_string, isDirect, loadIndiv, loadParents, loadChildren, callback);

			}

		};
		TreeStore<OwlClassBean> store = new TreeStore<OwlClassBean>(new KeyProvider());
		
		OwlClassBeanProperties props = GWT.create(OwlClassBeanProperties.class);
		store.addSortInfo(new StoreSortInfo<OwlClassBean>(props.name(), SortDir.ASC));
		TreeLoader<OwlClassBean> treeloader = new TreeLoader<OwlClassBean>(rpcProxy) {
			@Override
			public boolean hasChildren(OwlClassBean curr) {
				System.out.println("In treeLoader - for curr - " + curr.getIri() + " - hasChildren(): " + curr.hasChildren());
				return curr.hasChildren();
			}


		};
		ChildTreeStoreBinding<OwlClassBean> binding = new ChildTreeStoreBinding<OwlClassBean>(store) ;

		treeloader.addLoadHandler(binding);
		treeloader.load();

		
		final Tree<OwlClassBean, String> tree = new Tree<OwlClassBean, String>(store, new ValueProvider<OwlClassBean, String>() {

			public String getValue(OwlClassBean object) {
				return object.getName();
			}

			public void setValue(OwlClassBean object, String value) {
			}

			public String getPath() {
				return "name";
			}
		});
		tree.setWidth(300);
		tree.getStyle().setLeafIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
		//tree.getStyle().setJointCloseIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
	    //tree.getStyle().setJointOpenIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
	    tree.getStyle().setNodeCloseIcon(OntologyClassesTreeBundle.INSTANCE.concept());
	    tree.getStyle().setNodeOpenIcon(OntologyClassesTreeBundle.INSTANCE.concept());
	    
		tree.setLoader(treeloader);
		
		return tree;
	}

	public Widget asWidget() {
		FlowLayoutContainer con = new FlowLayoutContainer();
		con.addStyleName("margin-10");

		final Tree<OwlClassBean, String> tree = getTree();

		ButtonBar buttonBar = new ButtonBar();

		TextButton b1 = new TextButton("Expand All");
		b1.addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				tree.expandAll();
			}
		});

		TextButton b2 =new TextButton("Collapse All", new SelectHandler() {
			public void onSelect(SelectEvent event) {
				tree.collapseAll();
			}

		});

		buttonBar.add(b1);
		buttonBar.add(b2);

		buttonBar.setLayoutData(new MarginData(4));
		con.add(buttonBar);
		con.add(tree);
		return con;
	}

	public void onModuleLoad() {
		RootPanel.get().add(asWidget());
	}



	public OntologyQueryServiceAsync getOntologyService() {
		return ontologyService;
	}

	public void setOntologyService(OntologyQueryServiceAsync ontologyService) {
		this.ontologyService = ontologyService;
	}


}