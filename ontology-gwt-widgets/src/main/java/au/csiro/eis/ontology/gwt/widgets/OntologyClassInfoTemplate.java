package au.csiro.eis.ontology.gwt.widgets;

import au.csiro.eis.ontology.beans.OwlClassBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.beans.OwlRestrictionBean;
import au.csiro.eis.ontology.gwt.rpc.client.ontologyService.OntologyQueryServiceAsync;
import au.csiro.eis.ontology.gwt.widgets.xtemplates.OwlClassRenderer;
import au.csiro.eis.ontology.gwt.widgets.xtemplates.OwlRestrictionRenderer;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.Window;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class OntologyClassInfoTemplate implements IsWidget {
	  OntologyQueryServiceAsync ontologyQueryService;

	private ColumnModel cm;
    public final int NUM_GRID_ROWS = 10;
    final ContentPanel xpanel = new ContentPanel();  
    VerticalPanel vp = new VerticalPanel();  
    OwlClassBean classBean;
    Window individualViewWindow;

    OwlClassRenderer renderer;
    
    /* deprecated
	public OntologyClassInfoTemplate(ModelData modelData,  OntologyQueryServiceAsync ontologyQueryService) {
		this.modelData = modelData;
		this.classBean = null;
		
		individualViewWindow = this.instantiateInstanceViewWindow();
		this.ontologyQueryService= ontologyQueryService; 
	}
	*/
	
	public OntologyClassInfoTemplate(OwlClassBean classBean,  OntologyQueryServiceAsync ontologyQueryService) {
		//this.modelData = classBean.asModelData();
		this.classBean = classBean;
		
		individualViewWindow = this.instantiateInstanceViewWindow();
		this.ontologyQueryService = ontologyQueryService;
	}

	public OntologyClassInfoTemplate(OntologyQueryServiceAsync ontologyQueryService) {
		//this.modelData = classBean.asModelData();
		this.classBean = null;
		
		individualViewWindow = this.instantiateInstanceViewWindow();
		this.ontologyQueryService = ontologyQueryService;

	}
	
	private Window instantiateInstanceViewWindow() {
		//popup window
        final Window window = new Window();  
        window.setPixelSize(500, 300);  
        //window.setPlain(true);  
        window.setModal(true);  
        window.setBlinkModal(true);  
        window.setHeadingText("Individual");  
        //window.setLayout(new FitLayout());  
        
        return window;
	}
	
	private void modifyInstanceViewWindow(Widget w) {
		this.individualViewWindow.clear();
		this.individualViewWindow.add(w);
	}
	
	
	private void showInstanceViewWindow() {
		this.individualViewWindow.show();
	}
	private void hideInstanceViewWindow() {
		this.individualViewWindow.show();
	}

	
	public Widget asWidget() {
		//super.onRender(parent, index);  
	    		
		//setLayout(new FitLayout());
		//setLayout(new RowLayout());
		
		//addbits to the infoPanel
        FlowLayoutContainer con = new FlowLayoutContainer();

		if(this.classBean != null) {
			xpanel.setHeaderVisible(false);
		    //xpanel.setHeadingText("Selected item details");  
		    xpanel.setBodyStyleName("pad-text"); 
		    
		    updatePanel(this.classBean);
			con.add(xpanel);
		}
		return con;
	}
	
	
	public void updatePanel(OwlClassBean classBean) {
		this.classBean = classBean;
		xpanel.clear();
		
		FlowLayoutContainer con = new FlowLayoutContainer();
		
		/*
		con.add(new Label("Class: " + classBean.getName()));
		con.add(new Label("IRI: " + classBean.getIri()));
		
		if(this.classBean.hasObjectProperties()) {
			for(OwlObjectPropertyBean op : classBean.getObjectTypeRelations()) {
				con.add(new Label("- ObjProp: " + op.toString()));
			}
		}
		
		if(this.classBean.hasRestrictions()) {
			for(OwlRestrictionBean r : classBean.getRestrictions()) {
				
				String label = r.getType() + "(" + r.getProperty() + ", " + r.getFiller() + ")";
				
				con.add(new Label("- Restriction: " + label));

			}
		}
		
		if(this.classBean.hasIndividuals()) {
			for(OwlIndividualBean indiv: classBean.getIndividuals()) {
				con.add(new Label("- Indiv: " + indiv.toString()));

			}
		}
		*/
		if(this.renderer == null)
			this.renderer = GWT.create(OwlClassRenderer.class);

		OwlRestrictionRenderer restriction_renderer = GWT.create(OwlRestrictionRenderer.class);
		final HTML html = new HTML(renderer.renderTemplate(classBean));
		con.add(html);
		
		if(classBean.getLabel() != null ) {
			final HTML label 
			= new HTML("<p style='font:10px arial,sans-serif'>label: " + classBean.getLabel() + "</p>");
			con.add(label);
		}
		
		if(classBean.getRdfsComment() != null) {
			final HTML comments 
				= new HTML("<p style='font:10px arial,sans-serif'>comment: " + classBean.getRdfsComment() + "</p>");
			con.add(comments);
		}
		
		
		
		if(classBean.hasRestrictions()) {
			con.add(new HTML("<br/><p>Property Restrictions:"));

	
			for(OwlRestrictionBean restriction : classBean.getRestrictions()){
				System.out.println("Restiction Type: '" + restriction.getType() + "'");
				final HTML restriction_html = new HTML("<div style=\"font:11px arial,sans-serif\">- "+ restriction.toString() + "</div>");
				con.add(restriction_html);			
			}
			con.add(new HTML("</p>"));

		}
		
		if(classBean.hasIndividuals()) {
			con.add(new HTML("<br/><p>Defined instances:"));
	
			for(OwlIndividualBean indiv : classBean.getIndividuals()){
				System.out.println("Instance: '" + indiv.getIri() + "'");
				
				String renderedLabel = "";
				if(indiv.getLabel() != null && indiv.getLabel().length()>0) {
					renderedLabel = indiv.getLabel();
				}
				else if(indiv.getName() != null && indiv.getName().length()>0) {
					renderedLabel = indiv.getName();
				}
				else if(indiv.getLocalName() != null && indiv.getLocalName().length()>0) {
					renderedLabel = indiv.getLocalName();
				}
				else {
					renderedLabel = indiv.getIri();
				}
				
				final HTML restriction_html = new HTML("<div style=\"font:11px arial,sans-serif\">- "+ renderedLabel + "</div>");
				con.add(restriction_html);			
			}
			con.add(new HTML("</p>"));

		}
		
		xpanel.add(con);
		//xpanel.add(new Label("Class: " + classBean.getName()));
		//xpanel.add(new Label("Class: " + classBean.getName()));

		/* TODO: Migrate to gxt 3
		XTemplate tpl = XTemplate.create(getTemplate());
		XTemplate indivtpl = XTemplate.create(getIndividualTemplate());
			
		
		xpanel.clear();
        xpanel.addText(tpl.applyTemplate(Util.getJsObject(modelData, 3)));
    	//xpanel.addText(indivtpl.applyTemplate(Util.getJsObject(modelData, 3)));

    	
        if(this.classBean != null) { */
        	/* old
        	List<OwlIndividualBean> individualBeans = (List<OwlIndividualBean>) this.classBean.getIndividuals();
        	
        	if(individualBeans != null) {
            	BeanModelReader beanModelReader = new BeanModelReader();
            	
            	MemoryProxy<OwlIndividualBean> proxy = new MemoryProxy<OwlIndividualBean>(individualBeans);
            	
            	ListLoader<ListLoadResult<OwlIndividualBean>> loader = new BaseListLoader<ListLoadResult<OwlIndividualBean>>(  
            			proxy, beanModelReader);  
            	
                ListStore<BeanModel> store = new ListStore<BeanModel>(loader);
                loader.load();

                ListView<BeanModel> view = new ListView<BeanModel>() {
                    @Override  
                    protected BeanModel prepareData(BeanModel model) {  
                        
                      return model;  
                    }  
                
                  };  
                
                  view.setTemplate(getIndivListTemplate());  
                  view.setStore(store);  
                  view.setItemSelector("div.thumb-wrap");
                 
                  
                  
                  view.getSelectionModel().addListener(Events.SelectionChange,  
                      new Listener<SelectionChangedEvent<BeanModel>>() {  
                
                        public void handleEvent(SelectionChangedEvent<BeanModel> be) {  
                            GWT.log("Selection size: " +  be.getSelection().size()) ;
                          
                        }  
                
                      });  
                  
                  LayoutContainer lc = new LayoutContainer();
                  
                  ContentPanel cp = new ContentPanel();
                  cp.setHeading("Individuals:");
                  cp.setHeaderVisible(true);
                  
                  cp.setWidth(100);
                  //view.setWidth(300);
                  
                  
                  cp.add(view);
                  lc.add(cp);
                  
                  xpanel.add(lc);
        	}
        	*/
		/* TODO: migrate to gxt 3 cont... 
        }
        
        else {
        	
        	
        	List<ModelData> listOfIndividualModelData = modelData.get("individuals");
        	
        	boolean isLoadIndividuals = modelData.get("isLoadIndividuals");
        	
        	if(isLoadIndividuals == false) { 
        	//if(listOfIndividualModelData != null) {
        
        		final String class_iri = modelData.get("id"); //class iri
        		
        		 // proxy and reader  OwlIndividualBean
        	    RpcProxy<List<OwlIndividualBean>> proxy = new RpcProxy<List<OwlIndividualBean>>() {  
        	      @Override  
        	      public void load(Object loadConfig, AsyncCallback<List<OwlIndividualBean>> callback) {  
              		ontologyQueryService.getOntIndividuals(class_iri, callback);
  
        	      }  
        	    };  
        	    BeanModelReader reader = new BeanModelReader();  
        	  
        	    // loader and store  
        	    ListLoader<ListLoadResult<ModelData>> loader = new BaseListLoader<ListLoadResult<ModelData>>(proxy, reader);  
        	    ListStore<BeanModel> store = new ListStore<BeanModel>(loader);  
        	  
        	    loader.load();  
        	    
        	    
        		
                ListView<BeanModel> view = new ListView<BeanModel>() {
                    @Override  
                    protected BeanModel prepareData(BeanModel model) {  
                        
                      return model;  
                    }  
                
                  };  
                
                  view.setTemplate(getIndivListTemplate());  
                  view.setStore(store);  
                  view.setItemSelector("div.thumb-wrap");  
                  
                  
                  
                 
                  
                  view.getSelectionModel().addListener(Events.SelectionChange,  
                      new Listener<SelectionChangedEvent<BeanModel>>() {  
                
                        public void handleEvent(SelectionChangedEvent<BeanModel> be) {  
                            GWT.log("Selection size: " +  be.getSelection().size()) ;
                            //expect a owlIndividualBean
                            
                            if(be.getSelectedItem().getBean() instanceof OwlIndividualBean) {
                            	OwlIndividualBean indivBean = (OwlIndividualBean) be.getSelectedItem().getBean(); 
                            	OntologyIndividualInfoTemplate indivTemplate = new OntologyIndividualInfoTemplate(indivBean);
                            	
    	            			
                            	
                            	modifyInstanceViewWindow(indivTemplate);
                            	
                            	individualViewWindow.show();  
                            	
                            }
                		  
                            

                        }  
                
                      });  
                  LayoutContainer lc = new LayoutContainer();
                  
                  ContentPanel cp = new ContentPanel();
                  cp.setHeadingText("Individuals:");
                  cp.setHeaderVisible(true);
                  
                  cp.setWidth(400);
                  //view.setWidth(300);
                  
                  
                  cp.add(view);
                  lc.add(cp);
                  
                  xpanel.add(lc);

        	} else {
        		xpanel.addText(indivtpl.applyTemplate(Util.getJsObject(modelData, 3)));
        	}
        	
        	
        }
        
        
        xpanel.setStyleName("wqcep-ontology-info-panel",true);
        
        
        //render individuals
		//OntologyIndividualInfoTemplate ontClassInfoTemplate = new OntologyIndividualInfoTemplate(modelData);
		//add(ontClassInfoTemplate);
        
        //add individual 
        xpanel.layout();
        */
	}

	private native String getIndivListTemplate() /*-{ 
	return ['<tpl for=".">', 
	'<div class="thumb-wrap" id="{name}">', 
	'<span class="x-editable">{name}</span></div>', 
	'</tpl>', 
	'<div class="x-clear"></div>'].join(""); 
	 
	}-*/;  
		
	
	private native String getTemplate() /*-{ 
    var html = [ 
    '<p>Name: {name}</p>', 
    '<p>ID: {id}</p>',
    '<tpl if="hasObjectProperties">', 
    '<br/>',    
    '<p>Object Properties:</p>' ,
    	'<tpl for="objectProperties">', 
    	'<p>',
    		'{#}. \'<span style="font-style: italics">{prop}</span>\'  -  <span style="font-style: italics">{filler}</span>',
    	' </p>',
    	'</tpl>', 
    '</tpl>', 
    '<tpl if="hasRestrictions">',
    	'<br/>', 
        '<p>Restrictions:</p>' ,
    	'<tpl for="restrictions">', 
    	'<p>',
    		'<tpl if="restrictionType == \'ObjectAllValuesFrom\' ">',
    			'{#}. \'<span style="font-style: italics">{prop}</span>\' property must be a type of <span style="font-style: italics">{filler}</span>',
    		'</tpl>',
    		'<tpl if="restrictionType == \'ObjectSomeValuesFrom\' ">',
    			'{#}. \'<span style="font-style: italics">{prop}</span>\' property must be a type of <span style="font-style: italics">{filler}</span>',
    		'</tpl>',
    		'<tpl if="restrictionType == \'DataAllValuesFrom\' ">',
    			'{#}. \'<span style="font-style: italics">{prop}</span>\' property must be a type of <span style="font-style: italics">{filler}</span>',
    		'</tpl>',
    		'<tpl if="restrictionType == \'DataSomeValuesFrom\' ">',
    			'{#}. \'<span style="font-style: italics">{prop}</span>\' property must be a type of <span style="font-style: italics">{filler}</span>',
    		'</tpl>',
    	' </p>',
    	'</tpl>', 
    '</tpl>',
    '<tpl if="hasChildren">',
    '<br/>', 
        '<p>Children:</p>' ,
    	'<tpl for="children">', 
    	'<p>{#}. {name}</p>',
    	'</tpl>', 
    '</tpl>' 
    ]; 
    return html.join(""); 
  }-*/;  
	

	private native String getIndividualTemplate() /*-{ 
    var html = [ 
    '<tpl if="hasIndividuals">',
       '<tpl if="isLoadIndividuals">',
       '<br/>', 
           '<p>Individuals:</p>' ,
    	   '<tpl for="individuals">', 
    	   '<p>{#}. {name}</p>',
    	   '</tpl>', 
    	'</tpl>',
    '</tpl>' 
    ]; 
    return html.join(""); 
  }-*/;  
}
