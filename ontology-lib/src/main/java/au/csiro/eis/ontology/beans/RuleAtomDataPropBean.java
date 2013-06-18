package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class RuleAtomDataPropBean extends RuleAtomBean 
				implements Comparable<RuleAtomDataPropBean>, Serializable, IsSerializable {
	/**
	 * propA( ?x, ?y )
	 */
	private static final long serialVersionUID = 1L;
	
	String name;
	RuleVarBean var1;
	RuleVarBean var2;
	OwlDataPropertyBean dataPropBean;
	
	public RuleAtomDataPropBean() {
		this.name = null;
		this.var1 = null;
		this.var2 = null;
		dataPropBean = null;
	}

	/*
	public RuleAtomDataPropBean() {
		this.name = null;
		this.var1 = null;
		this.var2 = null;
	}

	public RuleAtomDataPropBean(String name, String var1, String var2) {
		this.name = name;
		this.var1 = this.getRuleVarBean(var1);
		this.var2 = this.getRuleVarBean(var2);
		super.setRenderedLabel(this.toString());

	}

	public RuleAtomDataPropBean(String name, RuleVarBean var1, RuleDataValueVarBean var2) {
		this.name = name;
		this.var1 = var1;
		this.var2 = var2;
		super.setRenderedLabel(this.toString());
	}

*/
	public RuleAtomDataPropBean(OwlDataPropertyBean prop, RuleVarBean var1, RuleDataValueVarBean var2) {
		this.dataPropBean = prop;
		
		if(prop != null) {
			this.name = prop.getProperty();			
		}
		
		this.var1 = var1;
		this.var2 = var2;
		super.setRenderedLabel(this.toString());
	}

	public RuleAtomDataPropBean(OwlDataPropertyBean prop, RuleVarBean var1, RuleVarBean var2) {
		this.dataPropBean = prop;
		
		if(prop != null) {
			this.name = prop.getProperty();			
		}
		
		this.var1 = var1;
		this.var2 = var2;
		super.setRenderedLabel(this.toString());
	}

	
	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
		super.setRenderedLabel(this.toString());

	}




	public RuleVarBean getVar1() {
		return var1;
	}

	public void setVar1(RuleVarBean var1) {
		this.var1 = var1;
	}

	public RuleVarBean getVar2() {
		return var2;
	}

	public void setVar2(RuleVarBean var2) {
		this.var2 = var2;
	}

	public void updateRenderedLabel() {
		super.setRenderedLabel(this.toString());
	}


	public RuleTextVarBean getRuleVarBean(String text) {
		RuleTextVarBean textBean = new RuleTextVarBean();
		textBean.setValue(text);
		
		return textBean;		
	}
	


	public String toString() {
		return this.name + "(" + this.var1.toString() + "," + this.var2.toString() + ")"; 
	}


	
	
	
	
	public OwlDataPropertyBean getDataPropBean() {
		return dataPropBean;
	}

	public void setDataPropBean(OwlDataPropertyBean dataPropBean) {
		this.dataPropBean = dataPropBean;
	}

	/*
	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("name", this.getName());
		m.set("var1", this.getVar1());
		m.set("var2", this.getVar2());
		m.set("prop", this.getDataPropBean());
		
		return m;
	}
	*/
	
	public int compareTo(RuleAtomDataPropBean o) {
		return this.name.compareTo(o.name);
	}

}
