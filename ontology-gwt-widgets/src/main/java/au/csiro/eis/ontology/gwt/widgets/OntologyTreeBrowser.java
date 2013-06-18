package au.csiro.eis.ontology.gwt.widgets;

import java.util.ArrayList;
import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.OntologyTreePanel.KeyProvider;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlClassBeanProperties;


import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.Element;  
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.DataProxy;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tips.QuickTip;
import com.sencha.gxt.widget.core.client.tree.Tree;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckCascade;

/**
 * 
 * @author yu021
 *
 */
public class OntologyTreeBrowser implements IsWidget {
	OntologyQueryServiceAsync ontologyQueryService;

	OwlOntologyBean ontologyBean;
	OntologyClassInfoTemplate infoTemplate;

	Tree<OwlClassBean, String> tree;
	TreeLoader<OwlClassBean> treeloader;
	TreeStore<OwlClassBean> store;  

	final String OWL_THING_IRI = "http://www.w3.org/2002/07/owl#Thing";

	FlowLayoutContainer modelBrowserContainer;
	int width;
	int height;
	
	boolean loadIndividuals;

	private boolean loadParents;
	boolean loadChildren;

	class KeyProvider implements ModelKeyProvider<OwlClassBean> {
		public String getKey(OwlClassBean item) {
			return item.getIri();
		}
	}

	
	public OntologyTreeBrowser(OntologyQueryServiceAsync ontologyQueryService) throws OntologyInitException {
		
		this.loadIndividuals = false;
		this.loadChildren = true;
		this.loadParents = false;

		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		queryForOntologyBean();
		infoTemplate = null;
		this.initTree(this.OWL_THING_IRI);
		setDefaultSize();
		
	}
	
	public OntologyTreeBrowser(OntologyQueryServiceAsync ontologyQueryService, OwlOntologyBean bean) throws OntologyInitException {
		this.loadIndividuals = false;
		this.loadChildren = true;
		this.loadParents = false;

		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		//queryForOntologyBean();
		setOntologyBean(bean);

		infoTemplate = null;
		this.initTree(this.OWL_THING_IRI);
		setDefaultSize();
		
	}

	public OntologyTreeBrowser(OntologyQueryServiceAsync ontologyQueryService, String iri) throws OntologyInitException {
		this.loadIndividuals = false;
		this.loadChildren = true;
		this.loadParents = false;

		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		queryForOntologyBean();
		infoTemplate = null;
		this.initTree(iri);
		setDefaultSize();
		
	}

	public OntologyTreeBrowser(OntologyQueryServiceAsync ontologyQueryService, List<String> iri_list) throws OntologyInitException {
		this.loadIndividuals = false;
		this.loadChildren = true;
		this.loadParents = false;

		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		queryForOntologyBean();
		infoTemplate = null;
		this.initTree(iri_list);
		setDefaultSize();

	}


	public Widget asWidget(){  
		//super.onRender(parent, index);  

		//final Html html = new Html("This tree is handling 1 child. Expand to get more!");  
		//html.setStyleName("pad-text");  

		ContentPanel cp = new ContentPanel();
		//cp.setLayout(new FitLayout());
		//cp.setLayout(new RowLayout(Orientation.HORIZONTAL));
		//cp.setHeight(800);  
		cp.setHeaderVisible(true);
		cp.setHeadingText("Ontology Tree Browser");


		cp.setCollapsible(true);  

		modelBrowserContainer = new FlowLayoutContainer();  
		//modelBrowserContainer.setSize(300, 350);  
		modelBrowserContainer.setPixelSize(this.width, this.height);
		modelBrowserContainer.setBorders(true);  
		//modelBrowserContainer.setLayout(new FitLayout());
		//final Html treehtml = new Html("Model browser (expand to navigate)");  
		//modelBrowserContainer.add(treehtml);  

		modelBrowserContainer.add(this.tree);  




		//cp.add(modelBrowserContainer, new RowData(-1, 1, new Margins(4)));
		cp.add(modelBrowserContainer);

		
		
		return cp;
	}  

	public void initTreeWidgets(DataProxy<OwlClassBean, List<OwlClassBean>> rpcProxy) {
		this.store = new TreeStore<OwlClassBean>(new KeyProvider());

		OwlClassBeanProperties props = GWT.create(OwlClassBeanProperties.class);
		this.store.addSortInfo(new StoreSortInfo<OwlClassBean>(props.name(), SortDir.ASC));
		this.treeloader = new TreeLoader<OwlClassBean>(rpcProxy) {
			@Override
			public boolean hasChildren(OwlClassBean curr) {
				System.out.println("In treeLoader - for curr - " + curr.getIri() + " - hasChildren(): " + curr.hasChildren());
				return curr.hasChildren();
			}


		};
		ChildTreeStoreBinding<OwlClassBean> binding = new ChildTreeStoreBinding<OwlClassBean>(this.store) ;

		this.treeloader.addLoadHandler(binding);
		this.treeloader.load();


		this.tree = new Tree<OwlClassBean, String>(store, new ValueProvider<OwlClassBean, String>() {

			public String getValue(OwlClassBean object) {
				return object.getName();
			}

			public void setValue(OwlClassBean object, String value) {
			}

			public String getPath() {
				return "name";
			}
		});
		//tree.setWidth(300);
		this.tree.getStyle().setLeafIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
		//tree.getStyle().setJointCloseIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
		//tree.getStyle().setJointOpenIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
		this.tree.getStyle().setNodeCloseIcon(OntologyClassesTreeBundle.INSTANCE.concept());
		this.tree.getStyle().setNodeOpenIcon(OntologyClassesTreeBundle.INSTANCE.concept());

		this.tree.setLoader(treeloader);

	}
	

	public void initTree(final List<String> iri_list) {
		RpcProxy<OwlClassBean, List<OwlClassBean>> rpcProxy = new RpcProxy<OwlClassBean,List<OwlClassBean>>() {
			@Override
			public void load(OwlClassBean loadConfig,
					AsyncCallback<List<OwlClassBean>> callback) {
				String iri_string = OWL_THING_IRI;

				if(loadConfig != null) {
					iri_string = loadConfig.getIri();
					System.out.println("iri_string in rpc: " + iri_string);
					
					boolean isDirect = true;
					boolean loadIndiv = true;
					boolean loadParents = false;
					boolean loadChildren = true;
					
					ontologyQueryService.getOntSubClasses(iri_string, isDirect, loadIndiv, loadParents, loadChildren, callback);
					
				}
				else if(iri_list != null) {
					System.out.println("iri_string in rpc: " + iri_list);

					try {
						ontologyQueryService.getOntClasses(iri_list, loadIndividuals, loadParents, loadChildren, callback);
					} catch (OntologyInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		};
		
		this.initTreeWidgets(rpcProxy);
	}

	public void initTree(final String iri) {
		RpcProxy<OwlClassBean, List<OwlClassBean>> rpcProxy = new RpcProxy<OwlClassBean,List<OwlClassBean>>() {
			@Override
			public void load(OwlClassBean loadConfig,
					AsyncCallback<List<OwlClassBean>> callback) {
				String iri_string = OWL_THING_IRI;

				if(loadConfig != null) {
					iri_string = loadConfig.getIri();
					System.out.println("iri_string in rpc: " + iri_string);

					boolean isDirect = true;
					boolean loadIndiv = true;
					boolean loadParents = false;
					boolean loadChildren = true;
					
					ontologyQueryService.getOntSubClasses(iri_string, isDirect, loadIndiv, loadParents, loadChildren, callback);
				}
				else if(iri != null) {
					System.out.println("iri_string in rpc: " + iri);

					List<String> list = new ArrayList<String>();
					list.add(iri_string);
					
					try {
						ontologyQueryService.getOntClasses(list, loadIndividuals, loadParents, loadChildren, callback);
					} catch (OntologyInitException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

			}
		};
		
		this.initTreeWidgets(rpcProxy);
	}




	public void setDefaultSize() {
		setSize(300,350);
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}



	public Tree<OwlClassBean, String> getTree() {
		return this.tree;
	}


	private void queryForOntologyBean() throws OntologyInitException {
		AsyncCallback<OwlOntologyBean> callback = new AsyncCallback<OwlOntologyBean>() {
			public void onFailure(Throwable caught) {
				String details = caught.getMessage();
				if (caught instanceof OntologyInitException) {
					details = "Ontology '" + ((OntologyInitException)caught).getOntologyId() + "' could not be read";
				}
			}

			public void onSuccess(OwlOntologyBean result) {
				setOntologyBean(result);
			}
		};

		ontologyQueryService.getOntologyBean(callback);
	}

	

	/* TODO: refactor the listener
	public void addDefaultClickListener() {
		this.addClickListener(new Listener<TreePanelEvent<BeanModel>>() {
			public void handleEvent(TreePanelEvent<BeanModel> be) {

				if(be.getItem() != null) {
					System.out.println("name: " +be.getItem().get("name"));
					System.out.println("id: " +be.getItem().get("id"));
					System.out.println("hasChildren? " +be.getItem().get("hasChildren"));

					System.out.println("hasIndividuals? " +be.getItem().get("hasIndividuals"));
				}
			};
		});
	}

	public void addClickListener(Listener<TreePanelEvent<BeanModel>> listener) {
		tree.addListener(Events.OnClick, listener);
	}
	*/

	
	private void setOntologyBean(OwlOntologyBean bean) {
		this.ontologyBean = bean;
	}

	public boolean isLoadIndividuals() {
		return loadIndividuals;
	}

	public void setLoadIndividuals(boolean loadIndividuals) {
		this.loadIndividuals = loadIndividuals;
	}
	
	
	
}

