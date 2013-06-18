package au.csiro.eis.ontology.gwt.widgets;



import au.csiro.eis.ontology.beans.OwlIndividualBean;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.XTemplates.XTemplate;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.FlowLayoutContainer;
import com.sencha.gxt.widget.core.client.grid.ColumnModel;

public class OntologyIndividualInfoTemplate extends FlowLayoutContainer{

	private ColumnModel cm;
    public final int NUM_GRID_ROWS = 10;
    final ContentPanel xpanel = new ContentPanel();  
    VerticalPanel vp = new VerticalPanel();  
    OwlIndividualBean individualBean;


	public OntologyIndividualInfoTemplate(OwlIndividualBean individualBean) {
		this.individualBean = individualBean;
	}
	

	
	@Override  
	public Widget asWidget() {
		//super.onRender(parent, index);  
	    		
		//setLayout(new FitLayout());
		
		//addbits to the infoPanel
	    xpanel.setHeadingText("Individual details");  
	    xpanel.setBodyStyleName("pad-text"); 
	    
	    updatePanel();
        
		add(xpanel);
		return this;
	}
	
	public void updatePanel() {
		
		/* TODO: fix the use of XTemplate 
		XTemplate tpl = XTemplate.create(getTemplate());
		XTemplate objPropTpl = XTemplate.create(getObjPropTemplate());

		
		xpanel.clear();
        xpanel.addText(tpl.applyTemplate(Util.getJsObject(modelData, 3)));
        xpanel.addText(objPropTpl.applyTemplate(Util.getJsObject(modelData, 3)));
        //add individual 
        xpanel.layout();
        */
	}
	
	public void updatePanel(OwlIndividualBean individualBean) {
		this.individualBean = individualBean;
		
		/* TODO: fix the use of XTemplate 
		XTemplate tpl = XTemplate.create(getTemplate());
		XTemplate objPropTpl = XTemplate.create(getObjPropTemplate());

		
		xpanel.removeAll();
        xpanel.addText(tpl.applyTemplate(Util.getJsObject(modelData, 3)));
        xpanel.addText(objPropTpl.applyTemplate(Util.getJsObject(modelData, 3)));

        xpanel.setStyleName("wqcep-ontology-info-panel",true);
        
        
        //add individual 
        xpanel.layout();
        */
	}

	
	private native String getObjPropTemplate() /*-{ 
    var html = [ 
    '<tpl if="objectProperties">',
    '<br/>', 
        '<p>Object properties:</p>' ,
    	'<tpl for="objectProperties">', 
    	'<p>{#}. {prop} , {filler}</p>',
    	'</tpl>', 
    '</tpl>' 
    ]; 
    return html.join(""); 
  }-*/;  
	
	private native String getTemplate() /*-{ 
    var html = [ 
    '<p>Name: {name}</p>', 
    '<p>ID: {id}</p>'    
    ]; 
    return html.join(""); 
  }-*/;  
}
