package au.csiro.eis.ontology.gwt.widgets;

import au.csiro.eis.ontology.beans.OwlDataPropertyBean;
import au.csiro.eis.ontology.beans.OwlIndividualBean;
import au.csiro.eis.ontology.beans.OwlLiteralBean;
import au.csiro.eis.ontology.beans.OwlObjectPropertyBean;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Label;
import com.sencha.gxt.widget.core.client.Dialog;
import com.sencha.gxt.widget.core.client.Dialog.PredefinedButton;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;

public class OntologyIndividualInfoDialog {

	public static Dialog createDialog(OwlIndividualBean indiv) {
		if(indiv == null) {
			System.out.println("Indiv is null");
			return null;
		}
		
		final Dialog simple = new Dialog();
		simple.setHeadingText("Info: " + indiv.getName());
		simple.setBodyStyleName("pad-text");
		simple.setHeight(300);
		simple.setWidth(400);
		simple.setPredefinedButtons(PredefinedButton.OK);
		simple.setHideOnButtonClick(true);

		if(indiv.getIri() != null) {
			Label iriLbl = new Label(indiv.getIri());
			simple.add(iriLbl);
			System.out.println("Adding iri");
		}
		
		if(indiv.getLabel() != null) {
			
			simple.add(new Label("Label: " +indiv.getLabel()));
			System.out.println("Adding label");
		}

		if(indiv.getRdfsComment() != null) {
			Label cmtLbl = new Label(indiv.getRdfsComment());
			simple.add(new Label("Comment: " + cmtLbl));
			System.out.println("Adding comment");
		}
		
		if(indiv.getObjectProperties() != null) {
			VerticalLayoutContainer c = new VerticalLayoutContainer();
		    simple.add(c);
		    for(OwlObjectPropertyBean key : indiv.getObjectProperties().keySet()) {
		    	String proplabel = (key.getProperty()) != null ? key.getProperty() : key.getPropertyIri();
	    		
		    	for(final OwlIndividualBean obj : indiv.getObjectProperties().get(key)) {
			    	
		    		Label a = new Label(proplabel + ":" + obj.getName());
			    	a.addClickHandler(new ClickHandler() {

						public void onClick(ClickEvent event) {
							Dialog dlg = createDialog(obj);
							dlg.show();
						}
			    		
			    	});
			    	 
		    		c.add(a);		    		
		    	}
		    	
		    }
		}
		else if(indiv.getDataProperties() != null) {
			VerticalLayoutContainer c = new VerticalLayoutContainer();
		    simple.add(c);
		    for(OwlDataPropertyBean key : indiv.getDataProperties().keySet()) {
		    	String proplabel = (key.getProperty()) != null ? key.getProperty() : key.getPropertyIri();
	    		
		    	for(final OwlLiteralBean obj : indiv.getDataProperties().get(key)) {
			    	Label a = new Label(proplabel + ":" + obj.getLiteral());
			    	c.add(a);		    		
		    	}
		    	
		    }
		}

		simple.getBody().addClassName("pad-text");
		simple.setHideOnButtonClick(true);

		return simple;
	}
}
