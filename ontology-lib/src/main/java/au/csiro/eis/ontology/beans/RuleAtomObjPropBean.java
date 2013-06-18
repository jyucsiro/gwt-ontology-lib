package au.csiro.eis.ontology.beans;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;


public class RuleAtomObjPropBean extends RuleAtomBean 
				implements Comparable<RuleAtomObjPropBean>, Serializable, IsSerializable {
	/**
	 * propA( ?x, ?y )
	 */
	private static final long serialVersionUID = 1L;
	
	String name;
	RuleVarBean var1;
	RuleVarBean var2;
	OwlObjectPropertyBean objPropBean;
	
	public RuleAtomObjPropBean() {
		this.name = null;
		this.var1 = null;
		this.var2 = null;
		objPropBean = null;
	}

	/*
	
	public RuleAtomObjPropBean() {
		this.name = null;
		this.var1 = null;
		this.var2 = null;
	}

	public RuleAtomObjPropBean(String name, String var1, String var2) {
		this.name = name;
		this.var1 = this.getRuleVarBean(var1);
		this.var2 = this.getRuleVarBean(var2);
		super.setRenderedLabel(this.toString());
	}
	

	

	public RuleAtomObjPropBean(String name, RuleVarBean var1, String strVar2) {
		this.name = name;
		this.var1 = var1;
		this.var2 = this.getRuleVarBean(strVar2);
		super.setRenderedLabel(this.toString());
	}

	public RuleAtomObjPropBean(String name, RuleVarBean var1, RuleVarBean var2) {
		this.name = name;
		this.var1 = var1;
		this.var2 = var2;
		super.setRenderedLabel(this.toString());
	}

	*/
	public RuleAtomObjPropBean(OwlObjectPropertyBean prop, RuleVarBean var1, RuleVarBean var2) {
		this.objPropBean = prop;
		
		if(prop != null) {
			this.name = prop.getProperty();
		}
		this.var1 = var1;
		this.var2 = var2;
		super.setRenderedLabel(this.toString());
	}

	public RuleAtomObjPropBean(OwlObjectPropertyBean prop, String var1, String var2) {
		this.objPropBean = prop;
		
		if(prop != null) {
			this.name = prop.getProperty();
		}
		
		this.var1 = this.getRuleVarBean(var1);
		this.var2 = this.getRuleVarBean(var2);
		super.setRenderedLabel(this.toString());
	}

	public RuleAtomObjPropBean(OwlObjectPropertyBean prop, RuleVarBean var1, String strVar2) {
		this.objPropBean = prop;
		
		if(prop != null) {
			this.name = prop.getProperty();
		}
		this.var1 = var1;
		this.var2 = this.getRuleVarBean(strVar2);
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
		StringBuffer sb = new StringBuffer();
		
		if(this.name != null) {
			sb.append(this.name);
		}
		
		if(this.var1 != null || this.var2 != null) {
			sb.append("(");
		}
		
		if(this.var1 != null) {
			sb.append(this.var1.toString());
		}
		
		if(this.var1 != null || this.var2 != null) {
			sb.append(",");
		}
		
		if(this.var2 != null) {
			sb.append(this.var2.toString());
		}

		if(this.var1 != null || this.var2 != null) {
			sb.append(")");
		}
		
		//return this.name + "(" + this.var1.toString() + "," + this.var2.toString() + ")";
		return sb.toString();
	}
	
	


	public OwlObjectPropertyBean getObjPropBean() {
		return objPropBean;
	}

	public void setObjPropBean(OwlObjectPropertyBean objPropBean) {
		this.objPropBean = objPropBean;
	}

	/*
	public ModelData asModelData() {
		ModelData m = new BaseModelData();  
		m.set("name", this.getName());
		m.set("var1", this.getVar1());
		m.set("var2", this.getVar2());
		
		return m;
	}
	*/
	
	public int compareTo(RuleAtomObjPropBean o) {
		return this.name.compareTo(o.name);
	}

}
