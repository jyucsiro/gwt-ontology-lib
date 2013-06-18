package au.csiro.eis.ontology.beans;


import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RuleAtomClassDescriptionBean extends RuleAtomBean 
				implements Comparable<RuleAtomClassDescriptionBean>, Serializable, IsSerializable {
	/**
	 * ClassA( ?x ) 
	 */
	private static final long serialVersionUID = 1L;
	
	
	String name;
	RuleVarBean var1;
	OwlClassBean classBean;
	
		
	public RuleAtomClassDescriptionBean() {
		this.name = null;
		this.var1 = null;
	}

	public RuleAtomClassDescriptionBean(OwlClassBean c, String var1) {
		this.setDescription(c, var1);
		this.classBean = c;
	}

	public RuleAtomClassDescriptionBean(OwlClassBean c, RuleVarBean var1) {
		this.var1 = var1;
		this.classBean = c;
		this.setDescription(c);
			
		super.setRenderedLabel(this.toString());
	}
	
	
	public RuleAtomClassDescriptionBean(OwlClassBean c, OwlIndividualBean indiv) {
		this.setDescription(c, indiv);
		this.classBean = c;
	}
	
	
	public void setDescription(OwlClassBean c) {
		if(c == null) {
			return;
		}
		
		this.name = c.getName();
		//this.var1 = "?x";
		if(this.var1 == null) {
			//create a new one...
			this.var1 = new RuleTextVarBean("?x");			
		}
		super.setRenderedLabel(this.toString());
		this.classBean = c;
	}
	

	/*
	public void setDescription(ModelData md) {
		if(md == null) {
			return;
		}
		
		this.name = md.get("name");
		//this.var1 = "?x";
		super.setRenderedLabel(this.toString());
	}
	*/
	
	public void setDescription(OwlClassBean c, String varInput) {
		if(c == null) {
			return;
		}
		
		this.name = c.getName();
		
		if(varInput == null) {
			//this.var1 = "?x";
		}
		else {
			RuleTextVarBean varBean =  new RuleTextVarBean(varInput);
			this.var1 = varBean;
		}
		super.setRenderedLabel(this.toString());
	}
	
	public void setDescription(OwlClassBean c, OwlIndividualBean indiv) {
		if(c == null || indiv == null) {
			return;
		}
		
		this.name = c.getName();
		
		if(indiv != null) {
			RuleIVarBean varBean =  new RuleIVarBean(indiv);
			this.var1 = varBean;
		}
		super.setRenderedLabel(this.toString());
	}

	
	/*
	public void setDescription(ModelData md, String varInput) {
		if(md == null) {
			return;
		}
		
		this.name = md.get("name");
		RuleTextVarBean varBean =  new RuleTextVarBean(varInput);
		this.var1 = varBean;
		super.setRenderedLabel(this.toString());
	}
	*/

	public void updateRenderedLabel(){
		super.setRenderedLabel(this.toString());
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RuleVarBean getVar1() {
		return this.var1;
	}

	
	
	public OwlClassBean getClassBean() {
		return classBean;
	}

	public void setClassBean(OwlClassBean classBean) {
		this.classBean = classBean;
	}

	public void setVar1(String varInput) {
		RuleTextVarBean varBean =  new RuleTextVarBean(varInput);
		this.var1 = varBean;
	}


	public void setVar1(RuleVarBean varInput) {
		this.var1 = varInput;
	}

	public String toString() {
		return this.name + "(" + this.var1 + ")"; 
	}
	
	/*
	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("name", this.getName());
		m.set("var1", this.getVar1());
		
		return m;
	}
	*/

	public int compareTo(RuleAtomClassDescriptionBean o) {
		return this.name.compareTo(o.name);
	}
}
