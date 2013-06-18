package au.csiro.eis.ontology.beans;


import java.io.Serializable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public abstract class RuleBean implements Serializable, IsSerializable{
	/**
	 * List of rule atoms 
	 */
	private static final long serialVersionUID = 1L;
	
	List<RuleAtomBean> antedecents;
	List<RuleAtomBean> consequents;
	
	public RuleBean() {
		antedecents = null;
		consequents = null;
	}

	public List<RuleAtomBean> getAntedecents() {
		return antedecents;
	}

	public void setAntedecents(List<RuleAtomBean> antedecents) {
		this.antedecents = antedecents;
	}

	public List<RuleAtomBean> getConsequents() {
		return consequents;
	}

	public void setConsequents(List<RuleAtomBean> consequents) {
		this.consequents = consequents;
	}
	
	

	
}
