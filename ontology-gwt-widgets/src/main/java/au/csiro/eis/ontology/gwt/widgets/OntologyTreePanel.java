package au.csiro.eis.ontology.gwt.widgets;

import java.util.List;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlOntologyBean;
import au.csiro.eis.ontology.exception.OntologyInitException;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.bean.prop.OwlClassBeanProperties;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.text.shared.SimpleSafeHtmlRenderer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.cell.core.client.SimpleSafeHtmlCell;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.client.loader.RpcProxy;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.SortDir;
import com.sencha.gxt.data.shared.TreeStore;
import com.sencha.gxt.data.shared.Store.StoreSortInfo;
import com.sencha.gxt.data.shared.loader.ChildTreeStoreBinding;
import com.sencha.gxt.data.shared.loader.TreeLoader;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.button.ButtonBar;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;
import com.sencha.gxt.widget.core.client.info.Info;
import com.sencha.gxt.widget.core.client.tree.Tree;

public class OntologyTreePanel implements IsWidget {
	OntologyQueryServiceAsync ontologyQueryService;
 
	OwlOntologyBean ontologyBean;
	OntologyClassInfoTemplate infoTemplate;

	//Tree<OwlClassBean, String> tree;
	//TreeLoader<OwlClassBean> treeloader;
	//TreeStore<OwlClassBean> store;  

	FlowLayoutContainer modelBrowserContainer;
	FlowLayoutContainer modelViewerContainer;

	int PANEL_WIDTH;
	int PANEL_HEIGHT;

	int MODEL_BROWSER_HEIGHT;
	int MODEL_BROWSER_WIDTH;

	int MODEL_VIEWER_WIDTH;
	int MODEL_VIEWER_HEIGHT;

	final String OWL_THING_IRI = "http://www.w3.org/2002/07/owl#Thing";

	boolean hasViewer;
	boolean hasToolbar;
	
	Tree<OwlClassBean, String> tree;
	
	/*

	public OntologyTreePanel(OntologyQueryServiceAsync ontologyQueryService) throws OntologyInitException {
		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		queryForOntologyBean();
		infoTemplate = null;
		setupTreeData();
		setDefaultSize();
	}
	*/
	
	public OntologyTreePanel(OntologyQueryServiceAsync ontologyQueryService, boolean doLoad) throws OntologyInitException {
		this.init(ontologyQueryService, doLoad, true, true);
	}
	
	public OntologyTreePanel(OntologyQueryServiceAsync ontologyQueryService, boolean doLoad, boolean hasViewer, boolean hasToolbar) throws OntologyInitException {
		this.init(ontologyQueryService, doLoad, hasViewer, hasToolbar);
	}
	
	public void init(OntologyQueryServiceAsync ontologyQueryService, boolean doLoad, boolean hasViewer, boolean hasToolbar) throws OntologyInitException {
		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		infoTemplate = null;
		
		setDefaultSize();
		
		if(doLoad == true) {
			queryForOntologyBean();
			//setupTreeData();
		}
		
		this.hasToolbar = hasToolbar;
		this.hasViewer = hasViewer;
		
	}

	/*
	public OntologyTreePanel(OntologyQueryServiceAsync ontologyQueryService, String iri) throws OntologyInitException {
		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		queryForOntologyBean();
		infoTemplate = null;
		setupTreeData(iri);
		setDefaultSize();
	}

	public OntologyTreePanel(OntologyQueryServiceAsync ontologyQueryService, List<String> iri_list) throws OntologyInitException {
		this.ontologyQueryService = ontologyQueryService;
		ontologyBean = null;
		queryForOntologyBean();
		infoTemplate = null;
		setupTreeData(iri_list);
		setDefaultSize();
	}
	*/
	public void setDefaultSize() {
		PANEL_WIDTH = 1000;
		PANEL_HEIGHT = 600;


		MODEL_BROWSER_WIDTH = 400;
		MODEL_BROWSER_HEIGHT = 550;

		MODEL_VIEWER_WIDTH = 550;
		MODEL_VIEWER_HEIGHT = 550;

	}




	class KeyProvider implements ModelKeyProvider<OwlClassBean> {
		
		public String getKey(OwlClassBean item) {
			return item.getIri();
		}
	}

	/*
	private void setupTreeData() {
		setupTreeData("http://www.w3.org/2002/07/owl#Thing");
	}

	private void setupTreeData(final String iri) {
		// data proxy  
		RpcProxy<OwlClassBean, List<OwlClassBean>> proxy = new RpcProxy<OwlClassBean, List<OwlClassBean>>() {  
			@Override
			public void load(OwlClassBean loadConfig,
					AsyncCallback<List<OwlClassBean>> callback) {
				String iriToUse = iri;
				if(loadConfig != null){
					iriToUse = loadConfig.getIri();
				}
				
				System.out.println("Loading tree data: " + iriToUse);
				ontologyQueryService.getOntSubClasses(iriToUse, callback);

			}  
		};  
		
		System.out.println("setupTreeData: setup treeloader");

		treeloader = new TreeLoader<OwlClassBean>(proxy) {
			@Override
			public boolean hasChildren(OwlClassBean parent) {
				return parent.hasChildren();
			}
		}; 		 

		store = new TreeStore<OwlClassBean>(new KeyProvider());
		treeloader.addLoadHandler(new ChildTreeStoreBinding<OwlClassBean>(store));
		treeloader.load();
		
	}

	private void setupTreeData(final List<String> iri_list) {
		// data proxy  
		RpcProxy<OwlClassBean, List<OwlClassBean>> proxy = new RpcProxy<OwlClassBean, List<OwlClassBean>>() {

			@Override
			public void load(OwlClassBean loadConfig, AsyncCallback<List<OwlClassBean>> callback) {
				try {
					System.out.println("\n\n\nloading tree data: " + loadConfig);

					if(loadConfig == null) {
						ontologyQueryService.getOntClasses(iri_list, callback);  
					}
					else {
						if(loadConfig != null) {
							System.out.println("Model data: " + loadConfig.getLocalName());
							String iri = loadConfig.getIri();
							System.out.println("model id: " + iri);

							ontologyQueryService.getOntSubClasses(iri, callback);		  
						}

					}
				} catch (OntologyInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}  
		};  

		treeloader = new TreeLoader<OwlClassBean>(proxy) {
			@Override
			public boolean hasChildren(OwlClassBean parent) {
				return parent.hasChildren();
			}
		}; 		 

		store = new TreeStore<OwlClassBean>(new KeyProvider());
		treeloader.addLoadHandler(new ChildTreeStoreBinding<OwlClassBean>(store));
	}
	*/

	public Widget asWidget() {  
		//this.setupTreeData();
		tree = createTree();
		addHandlersToTree(tree);
		FlowLayoutContainer con = new FlowLayoutContainer() ;
		
		if(this.hasToolbar) {
			System.out.println("Adding button bar for Ontology TreePanel");
			con.add(this.getButtonBar(tree));  
		}
		
		System.out.println("Adding ContentPanel for Ontology TreePanel");
		con.add(createContentPanelForTree (tree));
		return con;
	}  

	public ContentPanel createContentPanelForTree(Tree<OwlClassBean, String> tree) {
		ContentPanel cp = new ContentPanel();
		//cp.setLayout(new HorizontalLayout()));
		//cp.setHeight(800);
		cp.setPixelSize(this.PANEL_WIDTH, this.PANEL_HEIGHT);

		cp.setHeaderVisible(false);

		cp.setCollapsible(true);  

		HorizontalLayoutContainer hc = new HorizontalLayoutContainer();
		
		modelBrowserContainer = new FlowLayoutContainer();  
		modelBrowserContainer.setPixelSize(this.MODEL_BROWSER_WIDTH, this.MODEL_BROWSER_HEIGHT);  
		modelBrowserContainer.setBorders(true);  
		//modelBrowserContainer.setLayout(new FitLayout());
		//final Html treehtml = new Html("Model browser (expand to navigate)");  
		//modelBrowserContainer.add(treehtml);  
		modelBrowserContainer.setScrollMode(ScrollMode.AUTOY);

		//modelBrowserContainer.add(getTreePanel());  
		modelBrowserContainer.add(tree);  
		hc.add(modelBrowserContainer, new HorizontalLayoutData(-1, 1, new Margins(4)));	


		if(this.hasViewer) {
			modelViewerContainer = new FlowLayoutContainer();  
			modelViewerContainer.setPixelSize(this.MODEL_VIEWER_WIDTH, this.MODEL_VIEWER_HEIGHT);  
			modelViewerContainer.setBorders(true);  
			modelViewerContainer.setScrollMode(ScrollMode.AUTOY);
			//modelViewerContainer.setLayout(new FitLayout());  
			
			hc.add(modelViewerContainer, new HorizontalLayoutData(-1, 1, new Margins(4)));
			
		}

		//cp.add(modelBrowserContainer, new RowData(-1, 1, new Margins(4)));
		//cp.add(modelViewerContainer, new RowData(-1, 1, new Margins(4)));

		cp.add(hc);
		return cp;
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

		//ontologyQueryService.getOntologyBean(SsnConstants.SSN_ONTOLOGY_URI, callback);

	}
	
	public ButtonBar getButtonBar(final Tree<OwlClassBean, String> tree) {
		ButtonBar buttonBar = new ButtonBar();  
		/*
		TextButton b0 = new TextButton("Load");
		b0.addSelectHandler(new SelectHandler() {  

			@Override
			public void onSelect(SelectEvent event) {
				treeloader.load();		
			}  
		});
		buttonBar.add(b0);
		*/
		/*
		TextButton b1 = new TextButton("Reload");
		b1.addSelectHandler(new SelectHandler() {  

			@Override
			public void onSelect(SelectEvent event) {
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {
						// TODO: Do something with errors.
					}

					public void onSuccess(Boolean result) {
						System.out.println("Ontology svc re-loaded!" + result);
						if(result.booleanValue()) {
							reloadTree();
						}
					}
				};

				ontologyQueryService.reloadOntologies(callback);				
			}  
		});
		buttonBar.add(b1);
		*/
		TextButton b2 = new TextButton("Expand All");
		b2.addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				tree.expandAll();  
			}

		});
		buttonBar.add(b2);

		TextButton b3 = new TextButton("Collapse All");
		b3.addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				tree.collapseAll();  
			}

		});
		buttonBar.add(b3);


		TextButton b4 = new TextButton("Save Model");
		b4.addSelectHandler(new SelectHandler() {

			public void onSelect(SelectEvent event) {
				AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
					public void onFailure(Throwable caught) {

						Info.display("Message", "Failed to save model");

					}

					public void onSuccess(Boolean result) {
						Info.display("Message", "Saved model");
					}
				};

				ontologyQueryService.saveModel(callback);					}

		});
		buttonBar.add(b4);
		return buttonBar;
	}
	
	public void addHandlersToTree(final Tree<OwlClassBean, String> tree) {
	
		SimpleSafeHtmlCell<String> cell = new SimpleSafeHtmlCell<String>(SimpleSafeHtmlRenderer.getInstance(), "click") {
		      @Override
		      public void onBrowserEvent(Context context, Element parent, String value, NativeEvent event,
		          ValueUpdater<String> valueUpdater) {
		        super.onBrowserEvent(context, parent, value, event, valueUpdater);
		        if ("click".equals(event.getType())) {
		          Info.display("Click", "You clicked \"" + value + "\"! Tree selectedItem: " 
		        		  	+ tree.getSelectionModel().getSelectedItem().getName());
		          
		          updateInfoContainer(tree.getSelectionModel().getSelectedItem());
		          
		        }
		        
		      }
		    };
		tree.setCell(cell);
	}
	
	public Tree<OwlClassBean, String> createTree() {
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
				
				ontologyQueryService.getOntSubClasses(iri_string, isDirect, loadIndiv, loadParents, loadChildren, callback);

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
		//tree.setWidth(300);
		tree.getStyle().setLeafIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
		//tree.getStyle().setJointCloseIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
		//tree.getStyle().setJointOpenIcon(OntologyClassesTreeBundle.INSTANCE.conceptLeaf());
		tree.getStyle().setNodeCloseIcon(OntologyClassesTreeBundle.INSTANCE.concept());
		tree.getStyle().setNodeOpenIcon(OntologyClassesTreeBundle.INSTANCE.concept());

		tree.setLoader(treeloader);

		return tree;
	}


/*
	private Tree<OwlClassBean, String> getTreePanel() {

		final Tree<OwlClassBean, String> tree = new Tree<OwlClassBean, String>(store, new ValueProvider<OwlClassBean, String>(){

			@Override
			public String getValue(OwlClassBean object) {
				return object.getName();				
			}

			@Override
			public void setValue(OwlClassBean object, String value) {

			}

			@Override
			public String getPath() {
				return "name";
			}  

		});
		tree.setWidth(300);

		tree.setTrackMouseOver(false);  
		tree.setCheckable(false);  
		//tree.setCheckStyle(CheckCascade.CHILDREN);  
		//tree.setDisplayProperty("name");  


		/*
		  tree.setLabelProvider(new ModelStringProvider<ModelData>() {

			@Override
			public String getStringValue(ModelData model, String property) {

				String template = "<span title=\"\" qtitle=\"" + model.get("name") +
                "\" qtip=\"" + model.get("comment") +
                "\">" + model.get(property) + "</span>";

				if(model.get("comment") == null) {
					template = "<span title=\"\" qtitle=\"" + model.get("name") +
			                "\" qtip=\"" + model.get("name") +
			                "\">" + model.get(property) + "</span>";

				}

				return template;
			}

		  });
		 */


		//QuickTip tip = new QuickTip(tree) ;

		/*
		  tree.setIconProvider(new IconProvider<OwlClassBean>() {  

		      public AbstractImagePrototype getIcon(ModelData model) {  



		        //return null;

		      }

			@Override
			public ImageResource getIcon(OwlClassBean model) {
				boolean hasChildren = model.get("hasChildren");

		        if (!hasChildren) {

	            	return IconHelper.createStyle("icon-concept-leaf");		            
		        }  else {
		            tree.getStyle().setLeafIcon(ExampleImages.INSTANCE.music());

		        	return IconHelper..createStyle("icon-concept");
		        }
			}  
		    });  
		 */

		//add(html);

		/* TODO: see if this event handling needs fixing...
		  tree.addListener(Events.OnClick, new Listener<TreePanelEvent<ModelData>>() {
	            public void handleEvent(TreePanelEvent<ModelData> be) {

	            	if(be.getItem() != null) {
	            		System.out.println("name: " +be.getItem().get("name"));
	            		System.out.println("id: " +be.getItem().get("id"));
	            		System.out.println("hasChildren? " +be.getItem().get("hasChildren"));

	            		System.out.println("hasIndividuals? " +be.getItem().get("hasIndividuals"));

	            		if(infoTemplate == null) {
	            			infoTemplate = new OntologyClassInfoTemplate(be.getItem(), ontologyQueryService);

	            			modelViewerContainer.add(infoTemplate);
	            			modelViewerContainer.layout();
	            		} else {
	            			infoTemplate.updatePanel(be.getItem());
	            			modelViewerContainer.layout();
	            		}
	            	}
	            };
	        });
		 */

		//tree.setToolTip(new ToolTipConfig());

		/*
		  tree.addListener(Events.OnFocus, new Listener<TreePanelEvent<ModelData>>() {
	            public void handleEvent(final TreePanelEvent<ModelData> be) {

	            	if(be.getItem() != null) {
	            		String name = be.getItem().get("name");
	            		System.out.println("Mouse over detected on item: " + name);

	    				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

	    					@Override
	    					public void execute() {
	    						Point target = be.getXY();
	    	    				int x = target.x + 22;
	    	    				int y = target.y + 22;
	    	    				target = new Point(x, y);

	    	    				ToolTipConfig config = new ToolTipConfig("Information", "Prints the current document");
	    						ToolTip tt = new ToolTip();

	    						tt.setToolTip(config);
	    	    				tt.showAt(target);

	    					}
	    				});
	            	}
	            };
	        });
		 */
		/*
			tree.setView(new TreePanelView<BeanModel>() {
				@Override
				protected void onMouseOver(TreePanelEvent ce) {
					System.out.println("Mouse over detected on: " + ce.getItem().get("name"));
				}
			});
		 */
/*
	return tree;
	}
*/
	private void setOntologyBean(OwlOntologyBean bean) {


		this.ontologyBean = bean;

	}

	public int getPANEL_WIDTH() {
		return PANEL_WIDTH;
	}

	public void setPANEL_WIDTH(int pANEL_WIDTH) {
		PANEL_WIDTH = pANEL_WIDTH;
	}

	public int getPANEL_HEIGHT() {
		return PANEL_HEIGHT;
	}

	public void setPANEL_HEIGHT(int pANEL_HEIGHT) {
		PANEL_HEIGHT = pANEL_HEIGHT;
	}

	public int getMODEL_BROWSER_HEIGHT() {
		return MODEL_BROWSER_HEIGHT;
	}

	public void setMODEL_BROWSER_HEIGHT(int mODEL_BROWSER_HEIGHT) {
		MODEL_BROWSER_HEIGHT = mODEL_BROWSER_HEIGHT;
	}

	public int getMODEL_BROWSER_WIDTH() {
		return MODEL_BROWSER_WIDTH;
	}

	public void setMODEL_BROWSER_WIDTH(int mODEL_BROWSER_WIDTH) {
		MODEL_BROWSER_WIDTH = mODEL_BROWSER_WIDTH;
	}

	public int getMODEL_VIEWER_WIDTH() {
		return MODEL_VIEWER_WIDTH;
	}

	public void setMODEL_VIEWER_WIDTH(int mODEL_VIEWER_WIDTH) {
		MODEL_VIEWER_WIDTH = mODEL_VIEWER_WIDTH;
	}

	public int getMODEL_VIEWER_HEIGHT() {
		return MODEL_VIEWER_HEIGHT;
	}

	public void setMODEL_VIEWER_HEIGHT(int mODEL_VIEWER_HEIGHT) {
		MODEL_VIEWER_HEIGHT = mODEL_VIEWER_HEIGHT;
	}

/*
	public void reloadTree() {
		store.clear();
		store.commitChanges();
		treeloader.load(null);			
	}

	public void load() {
		//queryForOntologyBean();
		setupTreeData();		
	}
*/

	
	public void updateInfoContainer(OwlClassBean selectedClass) {
    		System.out.println("name: " +selectedClass.getName());
    		System.out.println("id: " +selectedClass.getIri());
    		System.out.println("hasChildren? " +selectedClass.hasChildren());

    		System.out.println("hasIndividuals? " +selectedClass.hasIndividuals());

    		if(infoTemplate == null) {
    			infoTemplate = new OntologyClassInfoTemplate(selectedClass, ontologyQueryService);

    			modelViewerContainer.add(infoTemplate.asWidget());
    		} else {
    			infoTemplate.updatePanel(selectedClass);
    		}
    	
		
	}

	public Tree<OwlClassBean, String> getTree() {
		return tree;
	}

	public void setTree(Tree<OwlClassBean, String> tree) {
		this.tree = tree;
	}
	
	
}

