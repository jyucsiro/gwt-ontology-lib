package au.csiro.eis.ontology.beans;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class RuleIVarBean extends RuleVarBean implements Serializable, IsSerializable {
	/**
	 * List of rule atoms 
	 */
	private static final long serialVersionUID = 1L;
	
	String iri;
	OwlIndividualBean indiv;
	
	public RuleIVarBean() {
		
	}

	public RuleIVarBean(OwlIndividualBean indiv) {
		this.indiv = indiv;
		
		this.setValue(indiv.getName());
		this.setIri(indiv.getIri());
		
		
		
	}

	public RuleIVarBean(String value, String iri) {
		super.value = value;
		this.iri = iri;
	}


	public String getIri() {
		return iri;
	}


	public void setIri(String iri) {
		this.iri = iri;
	}

	
}
