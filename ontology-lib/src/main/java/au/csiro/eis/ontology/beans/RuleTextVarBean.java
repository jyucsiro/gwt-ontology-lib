package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class RuleTextVarBean extends RuleVarBean implements Serializable, IsSerializable {
	/**
	 * List of rule atoms 
	 */
	private static final long serialVersionUID = 1L;

	
	public RuleTextVarBean() {
		
	}

	public RuleTextVarBean(String val) {
		super.value = val;
	}


		
}
