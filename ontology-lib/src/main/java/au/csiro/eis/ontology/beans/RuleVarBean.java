package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public abstract class RuleVarBean implements Comparable<RuleVarBean>, Serializable, IsSerializable {
	/**
	 * List of rule atoms 
	 */
	private static final long serialVersionUID = 1L;

	protected String value;
	
	public RuleVarBean() {
		
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString() {
		return value;
	}
	

	public int compareTo(RuleVarBean o) {
		return this.value.compareTo(o.value);
	}

		
}
