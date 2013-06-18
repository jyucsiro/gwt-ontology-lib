package au.csiro.eis.ontology.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;



/**
 * Intended to capture the antedecent and consequent and serialize 
 * in a JSON format. This can then be used to interface with backend
 * using SWRL rules generation/execution... 
 * @author yu021
 *
 */
public class WqCepRule implements Serializable, IsSerializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	List<RuleAtomBean> antecedent;
	List<RuleAtomBean> consequent;
	String name;
	boolean enabled;
	String renderedLabel;
	
	int userid;
	int ruleid;	
	
	
	//Map <String, RuleAtomBean> ruleAtomIdx;
	//Map <RuleAtomBean, String> reverseRuleAtomIdx;

	

	public WqCepRule() {
		//this.ruleAtomIdx = new HashMap<String, RuleAtomBean>();
		//this.reverseRuleAtomIdx = new HashMap<RuleAtomBean, String>();
	}
	
	
	public List<RuleAtomBean> getAntecedent() {
		return antecedent;
	}


	public void setAntecedent(List<RuleAtomBean> antecedent) {
		this.antecedent = antecedent;
		this.setRenderedLabel(this.toString());
		
		if(antecedent != null) {
			//clear index
			//this.reverseRuleAtomIdx.clear();
			//this.ruleAtomIdx.clear();
			
			/*
			for(RuleAtomBean curr : antecedent) {
				this.indexRuleAtomBean(curr);				
			}
			*/
		}
		
	}


	public List<RuleAtomBean> getConsequent() {
		return consequent;
	}


	public void setConsequent(List<RuleAtomBean> consequent) {
		this.consequent = consequent;
		this.setRenderedLabel(this.toString());
	}


	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
	public String toString() {
		StringBuffer sb  = new StringBuffer();
		int count = 0;
		
		//sb.append("Rule: " + this.getName() + " | ");
		
		if(this.getAntecedent() != null) {
			for(RuleAtomBean curr : this.getAntecedent()) {
				if(count++ > 0) {
					sb.append(" & ");
				}
				sb.append(curr.toString());
			}
		}
		
		
		
		if(this.getConsequent() != null) {
			sb.append(" -> ");
			
			for(RuleAtomBean curr : this.getConsequent()) {				
				if(count++ > 0) {
					sb.append(" & ");
				}
				sb.append(curr.toString());
			}
		}
		
		return sb.toString();
	}


	public String getRenderedLabel() {
		return renderedLabel;
	}


	public void setRenderedLabel(String renderedLabel) {
		this.renderedLabel = renderedLabel;
	}

	
	public void addAntecedent(RuleAtomBean atom) {
		if(this.antecedent == null) {
			this.antecedent = new ArrayList<RuleAtomBean>();
		}

		this.antecedent.add(atom);
		/*
		//check for duplicates
		if(this.reverseRuleAtomIdx.get(atom) == null) {
			this.antecedent.add(atom);
			indexRuleAtomBean(atom);
		}
		*/
	}
	
	public void addConsequent(RuleAtomBean atom) {
		if(this.consequent == null) {
			this.consequent = new ArrayList<RuleAtomBean>();
		}
		
		this.consequent.add(atom);
	}


	
	/**
	 * For now just index classDescriptionBeans
	 * @param b
	 */
	/*
	public void indexRuleAtomBean(RuleAtomBean b) {
		if(b instanceof RuleAtomClassDescriptionBean) {
			//get the var
			RuleAtomClassDescriptionBean cdb = (RuleAtomClassDescriptionBean) b;
			String var = cdb.getVar1().getValue();
			
			this.addRuleAtomBeanToIndex(var, cdb);			
		}
		
		this.addRuleAtomBeanToReverseIndex(b);
	}
	
	public void addRuleAtomBeanToIndex(String var, RuleAtomBean b) {
		this.ruleAtomIdx.put(var, b);
	}

	public void addRuleAtomBeanToReverseIndex(RuleAtomBean b) {
		this.reverseRuleAtomIdx.put(b, "");
	}

	public void resetRuleAtomBeanIdx() {
		this.ruleAtomIdx.clear();
	}
	
	public RuleAtomBean getRuleAtomBeanFromIdx(String var) {
		return this.ruleAtomIdx.get(var);
	}

*/
	
	public RuleAtomBean findClassDescripInAntecedent(String varname) {
		RuleAtomBean target = null;
		
		if(this.getAntecedent() == null)
			return null;
		
		for(RuleAtomBean b : this.getAntecedent()) {
			if(b instanceof RuleAtomClassDescriptionBean) {
				RuleAtomClassDescriptionBean cd = (RuleAtomClassDescriptionBean) b;
				if(cd.getVar1().getValue().equals(varname)) {
					target = cd;
				}
			}
		}
		
		return target;
	}


	public int getUserid() {
		return userid;
	}


	public void setUserid(int userid) {
		this.userid = userid;
	}


	
	public int getRuleid() {
		return ruleid;
	}


	public void setRuleid(int ruleid) {
		this.ruleid = ruleid;
	}
	
	
}
