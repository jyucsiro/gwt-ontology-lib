package au.csiro.eis.ontology.gwt.widgets;

import au.csiro.eis.ontology.beans.OwlClassBean;

import com.google.gwt.user.client.ui.IsWidget;

public abstract class OntologyClassViewContainer implements IsWidget {
	OwlClassBean classBean;
	
	public void setOwlClassBean(OwlClassBean c) {
		this.classBean = c;
	}
	
	public OwlClassBean getOwlClassBean() {
		return this.classBean;
	}

	public abstract void updateClassViewPanel();
		

}
