package au.csiro.eis.ontology.beans;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class RuleDataValueVarBean extends RuleVarBean implements Serializable, IsSerializable {
	/**
	 * List of rule atoms 
	 */
	private static final long serialVersionUID = 1L;
	
	String typeIri;
	
	
	public RuleDataValueVarBean() {
		
	}

	public RuleDataValueVarBean(String value, String typeIri) {
		this.typeIri = typeIri;
		super.value = value;
	}

	public String getTypeIri() {
		return typeIri;
	}

	public void setTypeIri(String typeIri) {
		this.typeIri = typeIri;
	}



	
}
