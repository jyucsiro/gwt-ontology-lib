package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public abstract class RuleAtomBean implements Serializable, IsSerializable {
	/**
	 * List of rule atoms 
	 */
	private static final long serialVersionUID = 1L;
	
	String renderedLabel;
	String iri;
	
	public RuleAtomBean() {
		
	}


	

	public String getRenderedLabel() {
		return renderedLabel;
	}


	public void setRenderedLabel(String renderedLabel) {
		this.renderedLabel = renderedLabel;
	}
	
	

	
}
