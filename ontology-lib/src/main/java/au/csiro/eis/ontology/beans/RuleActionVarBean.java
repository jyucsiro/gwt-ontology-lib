package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class RuleActionVarBean extends RuleVarBean implements Serializable, IsSerializable {
	/**
	 * List of rule atoms 
	 */
	private static final long serialVersionUID = 1L;
	
	String action;
	
	public RuleActionVarBean() {
		
	}

	public RuleActionVarBean(String value, String action) {
		super.value = value;
		this.action = action;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	
}
